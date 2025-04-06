package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.blocks.BerootCauldronBlock;
import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.networking.BerootCauldronCraftPacket;
import net.abraxator.moresnifferflowers.networking.BerootCauldronSuckPacket;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.nutrition.Nutrition;
import net.abraxator.moresnifferflowers.nutrition.NutritionEntry;
import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BerootCauldronBlockEntity extends ModBlockEntity {
    public int beetroots;
    public List<ItemStack> ingredients = new ArrayList<>();
    public int itemRot;
    public int soupAnimationFrame;
    public ItemStack soup = ItemStack.EMPTY;
    public BlockPos center;
    public int soupCount = 0;
    public boolean isCrafted = false;
    public final int MAX_SOUP_COUNT = 6;
    private final int foodLimit = 8;
    private final int beetrootLimit = 4;
    private final int spoonSpeed = 10;
    int spoonRotation;
    public boolean redSoup;
    boolean crafting;
    int craftingTimeRemaining;
    final int CRAFTING_TIME = 72;

    public BerootCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BEROOT_CAULDRON.get(), pPos, pBlockState);
        this.center = this.getBlockPos();
    }

    public InteractionResult addItem(ItemStack itemStack, Player player) {
        if(itemStack.is(ModItems.CROPRESSED_BEETROOT.get()) && this.beetroots < beetrootLimit && !this.isCrafted) {
            addBeetroot(itemStack, player);
            this.redSoup = true;
        } else if (!itemStack.isEmpty() && this.ingredients.size() < foodLimit && !Nutrition.getNutritionForItem(itemStack.getItem()).isEmpty() && !this.isCrafted && this.beetroots > 0) {
            addIngredient(itemStack, player);
            this.redSoup = false;
        } else if(itemStack.is(Items.BOWL) && hasSoup() && this.isCrafted) {
            giveSoup(itemStack, player);
        } else if (!ingredients.isEmpty() && !this.isCrafted && player != null) {
            this.crafting = true;
        } else return InteractionResult.PASS;

        return InteractionResult.SUCCESS;
    }
    
    public void craft(Player player) {
        //initialize all variables for soup creation
        if(ingredients.isEmpty()) {
            return;
        }

        this.isCrafted = true;

        Map<NutritionType, Integer> map = new HashMap<>();
        ItemStack soup = ModItems.ROOTED_SOUP.get().getDefaultInstance();
        CompoundTag tag = new CompoundTag();
        int neutral = 0;
        this.ingredients.forEach(stack -> {
                    Nutrition nutrition = Nutrition.getNutritionForItem(stack.getItem());
                    nutrition.getNutritionEntries().forEach(entry -> 
                            map.merge(entry.nutrition(), entry.weight(), Integer::sum));
                }
        );
        
        List<NutritionEntry> entryList = new ArrayList<>(map.entrySet()
                .stream()
                .map((Map.Entry<NutritionType, Integer> entry) -> new NutritionEntry(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(NutritionEntry::weight))
                .toList());
        int sat = ingredients.stream().mapToInt(value -> (int) value.getFoodProperties(player).getSaturationModifier()).sum();
        int food = ingredients.stream().mapToInt(value -> value.getFoodProperties(player).getNutrition()).sum();
        int ingredients = this.ingredients.size();
        this.soupCount = this.beetroots + (ingredients / 4);
        int soupFood = 6 + (food / ingredients);
        float soupSat = 7 + ((float) sat / ingredients);
        
        //calculate neutral factor
        for(NutritionEntry nutritionEntry : entryList) {
            if(nutritionEntry.nutrition().equals(NutritionType.NEUTRAL)) {
                neutral += nutritionEntry.weight();
            }
        }
        
        //values into tag
        tag.putInt("soupFood", soupFood);
        tag.putFloat("soupSat", soupSat);
        tag.putInt("soupCount", Math.min(Math.max(soupCount, 1), 4));

        //effect init
        ListTag effectTag = new ListTag();
        for (NutritionEntry nutritionEntry : entryList) {
            int scale = nutritionEntry.weight() / (neutral / 2 + 1);
            int dur = scale * 20;
            int amp = (int) (scale / 1.5);
            Boolean positive = null;

            if(scale > 0.75) {
                positive = false;
            } else if (scale > 0.5) {
                positive = true;
            }
            
            if(positive != null) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putInt("nutritionType", nutritionEntry.nutrition().ordinal());
                compoundTag.putBoolean("positive", positive);
                compoundTag.putInt("dur", dur);
                compoundTag.putInt("amp", amp);
                effectTag.add(compoundTag);
            }
        }
        tag.put("effects", effectTag);

        
        //soup creation
        soup.setTag(tag);
        this.soup = soup;
    }

    @Override
    public void tick(Level level){
        var pos = this.getBlockPos();
        if (pos.equals(this.center)) {
            suckInItems(level, this.center);
        }
    }

    @Override
    public void clientTick(ClientLevel level) {

        this.itemRot++;
        soupAnimFrame();
        if(this.crafting && this.craftingTimeRemaining < 9) {
            this.spoonRotation++;
            this.craftingTimeRemaining++;
            this.itemRot += 10;
            if(this.spoonRotation * spoonSpeed >= this.soupCount * 180) {
                 ModPacketHandler.CHANNEL.sendToServer(new BerootCauldronCraftPacket(this.getBlockPos()));
                this.crafting = false;
                this.isCrafted = true;
                this.craftingTimeRemaining = 0;
                this.spoonRotation = this.spoonRotation % 36;

                Vec3 center = getMiddle();
                for (int i = 0; i < 360; i++) {
                    if (i % 20 == 0) {
                        this.level.addParticle(
                                ParticleTypes.LAVA,
                                center.x, center.y, center.z,
                                Mth.cos(i), 0.5F, Mth.sin(i));
                    }
                }
            }
        } else {
            this.crafting = false;
            this.craftingTimeRemaining = 0;
        }
    }

    private boolean valuesClose(List<NutritionEntry> entryList, int tolerance) {
        List<Integer> weights = entryList.stream().map(NutritionEntry::weight).toList();
        int min = Collections.min(weights);
        int max = Collections.max(weights);
        
        return (max - min) <= tolerance;
    }
    
    public boolean hasSoup() {
        return this.soupCount > 0;
    }


    private void giveSoup(ItemStack itemStack, Player player) {
        itemStack.shrink(1);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemUtils.createFilledResult(player.getItemInHand(InteractionHand.MAIN_HAND), player, this.soup));
        this.soupCount -= 1;
        if (this.soupCount <= 0){
            this.soup = ItemStack.EMPTY;
            clearIngredients();
        }
    }

    private void addIngredient(ItemStack itemStack, Player player) {
        this.ingredients.add(new ItemStack(itemStack.getItem(), 1));
        int ingredients = this.ingredients.size();
        this.soupCount = this.beetroots + (ingredients / 4);

        if (level.isClientSide){
            Vec3 center = getMiddle();
            for (int i = 0; i < 360; i++) {
                if(i % 20 == 0) {
                    this.level.addParticle(
                            new DustParticleOptions(color(itemStack.getItem()).scale(1/255D).toVector3f(), 1.0F),
                            center.x, center.y, center.z,
                            Mth.cos(i), 0.5F, Mth.sin(i));
                }
            }
        }

        itemStack.shrink(1);
    }
    
    private void addBeetroot(ItemStack itemStack, Player player) {
        this.beetroots++;
        int ingredients = this.ingredients.size();
        this.soupCount = this.beetroots + (ingredients / 4);

        itemStack.shrink(1);

        if(!this.level.isClientSide) {
            return;
        }

        Vec3 center = getMiddle();
        for (int i = 0; i < 360; i++) {
            if(i % 20 == 0) {
                this.level.addParticle(
                        new DustParticleOptions(Vec3.fromRGB24(0x0f44336).toVector3f(), 1.0F),
                        center.x, center.y, center.z,
                        Mth.cos(i), 0.5F, Mth.sin(i));
            }
        }
    }


    public void suckInItems(Level level, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        switch (level.getBlockState(pos).getValue(HorizontalDirectionalBlock.FACING)){
            case EAST -> x +=1;
            case NORTH -> {
                x += 1;
                z -= 1;
            }
            case WEST -> z -= 1;
        }

        for(ItemEntity itementity : getItemsAtAndAbove(level, new BlockPos(x,y,z))) {
            ItemStack itemStack = itementity.getItem().copy();
            ItemStack itemStack1 = itemStack.copy();

            if (addItem(itemStack, null).equals(InteractionResult.SUCCESS)) {
                ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new BerootCauldronSuckPacket(itemStack1,this.getBlockPos()));
                itementity.setItem(itemStack);
            }
        }
    }

    public static List<ItemEntity> getItemsAtAndAbove(Level level, BlockPos pos) {
        return BerootCauldronBlock.makeShapeInside().toAabbs().stream().flatMap((p_155558_) -> level.getEntitiesOfClass(ItemEntity.class, p_155558_.move(pos.getX(), pos.getY(), pos.getZ() + 1.125), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }
    
    public float getItemsRotation(float partialTick) {
        if (this.crafting) partialTick *= 10;
        return (this.itemRot + partialTick) * 2;
    }

    public void soupAnimFrame() {
        int frameTime = this.crafting ? 5 : 15;
        int frame = 0;
        if (this.level.getGameTime() % frameTime == 0) this.soupAnimationFrame++;
        this.soupAnimationFrame = this.soupAnimationFrame % 5;
    }
    
    public float getSpoonRotation(float partialTick) {
        if(this.crafting) {
           if (this.spoonRotation % 9 != 0) return (this.spoonRotation + partialTick) * 10;
        }

        return this.spoonRotation*10;
    }
    
    private Vec3 getMiddle() {
        float x = -0.5F;
        float y = 1.5F;
        float z = 0.5F;

        switch (this.getBlockState().getValue(HorizontalDirectionalBlock.FACING)){
            case EAST -> x +=1;
            case NORTH -> {
                x += 1;
                z -= 1;
            }
            case WEST -> z -= 1;
        }

        return this.center.getCenter().add(x, y, z);
    }

    public void clearIngredients() {
        this.ingredients.clear();
        this.beetroots = 0;
        this.isCrafted = false;
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("beetroots", this.beetroots);
        tag.put("center", NbtUtils.writeBlockPos(this.center));
        ListTag items = new ListTag();
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack stack = ingredients.get(i);
            CompoundTag itemTag = new CompoundTag();
            itemTag.putByte("Slot", (byte) i);
            stack.save(itemTag);
            items.add(itemTag);
        }
        tag.put("items", items);
        tag.putInt("soupCount", this.soupCount);
        tag.putBoolean("crafting", this.crafting);
        tag.putInt("craftingTime", this.craftingTimeRemaining);
        tag.putBoolean("isCrafted", this.isCrafted);
        tag.putBoolean("redSoup", this.redSoup);
        tag.putInt("spoonRotation", this.spoonRotation);
        tag.putInt("soupAnimationFrame", this.soupAnimationFrame);



        if(!this.soup.isEmpty()) {
            CompoundTag soupTag = new CompoundTag();
            this.soup.save(soupTag);
            tag.put("soup", soupTag);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.ingredients = new ArrayList<>();
        this.beetroots = tag.getInt("beetroots");
        this.center = NbtUtils.readBlockPos(tag.getCompound("center"));
        ListTag items = tag.getList("items", 10);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag itemTag = items.getCompound(i);
                this.ingredients.add(ItemStack.of(itemTag));
        }
        this.soupCount = tag.getInt("soupCount");
        this.crafting = tag.getBoolean("crafting");
        this.craftingTimeRemaining = tag.getInt("craftingTime");
        this.isCrafted = tag.getBoolean("isCrafted");
        this.redSoup = tag.getBoolean("redSoup");
        this.spoonRotation = tag.getInt("spoonRotation");
        this.soupAnimationFrame = tag.getInt("soupAnimationFrame");


        if(tag.contains("soup")) {
            this.soup = ItemStack.of(tag.getCompound("soup"));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }


    public Vec3 color() {
        return color(null);
    }

    public Vec3 color(Item item) {
        int r = 0;
        int g = 0;
        int b = 0;

        var latestIngredient = !this.ingredients.isEmpty() ? this.ingredients.get(this.ingredients.size() -1).getItem() : null;
        if (item != null)
            latestIngredient = item;

        int loop = this.isCrafted ? this.ingredients.size() : 1;

        if (latestIngredient != null){
            for (int i  = 0; i < loop; i++) {
                if (this.isCrafted) latestIngredient = this.ingredients.get(i).getItem();
                NutritionType nutritionType = Nutrition.getLargestNutrition(latestIngredient);
                switch (nutritionType) {
                    case SOUR -> {
                        r += 255;
                        g += 205;
                        b += 0;
                    }
                    case SALTY -> {
                        r += 185;
                        g += 165;
                        b += 195;
                    }
                    case SPICY -> {
                        r += 187;
                        g += 67;
                        b += 48;
                    }
                    case SWEET -> {
                        r += 230;
                        g += 120;
                        b += 150;
                    }
                    case NEUTRAL -> {
                        r += 140;
                        g += 102;
                        b += 30;
                    }
                }
            }
        }

        if (this.isCrafted){
            r = r/ingredients.size();
            g = g/ingredients.size();
            b = b/ingredients.size();
        }

        if (this.redSoup && item == null && !this.isCrafted) {
            r = 164;
            g = 39;
            b = 44;
        }

        return new Vec3(r,g,b);
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return AABB.ofSize(this.center.getCenter(), 4, 4, 4);
    }
}
