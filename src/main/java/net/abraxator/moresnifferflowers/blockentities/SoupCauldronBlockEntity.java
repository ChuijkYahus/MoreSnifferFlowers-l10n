package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.SoupCauldronCraftPacket;
import net.abraxator.moresnifferflowers.nutrition.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SoupCauldronBlockEntity extends ModBlockEntity {
    public int beetroots;
    public List<ItemStack> ingredients = new ArrayList<>();
    public int itemRot;
    public ItemStack soup = ItemStack.EMPTY;
    public BlockPos center;
    public int soupCount = 0;
    public final int MAX_SOUP_COUNT = 6;
    private final int foodLimit = 8;
    private final int beetrootLimit = 4;
    int spoonRotation;
    boolean crafting;
    int craftingTimeRemaining;
    final int CRAFTING_TIME = 72;

    public SoupCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SOUP_CAULDRON.get(), pPos, pBlockState);
        this.center = this.getBlockPos();
    }
    
    public void craft(Player player) {
        //initialize all variables for soup creation
        if(ingredients.isEmpty()) {
            return;
        }
        
        Map<NutritionType, Integer> map = new HashMap<>();
        ItemStack soup = ModItems.GIANT_SOUP.get().getDefaultInstance();
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
        this.soupCount = beetroots + (ingredients / 4);
        int soupFood = 6 + (food / ingredients);
        float soupSat = 7 + ((float) sat / ingredients);
        
        //calculate neutral factor
        for(NutritionEntry nutritionEntry : entryList) {
            if(nutritionEntry.nutrition().equals(NutritionType.NEUTRAL)) {
                neutral = nutritionEntry.weight();
            }
        }
        
        //values into tag
        tag.putInt("soupFood", soupFood);
        tag.putFloat("soupSat", soupSat);
        tag.putInt("soupCount", Math.min(Math.max(soupCount, 1), 4));

        //effect init
        ListTag effectTag = new ListTag();
        for (NutritionEntry nutritionEntry : entryList) {
            int scale = nutritionEntry.weight() / (neutral / 2);
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
        this.ingredients.clear();
        this.soup = soup;
    }

    @Override
    public void clientTick(ClientLevel level) {
        this.itemRot++;
        if(this.crafting) {
            this.spoonRotation++;
            this.craftingTimeRemaining++;
            if(this.craftingTimeRemaining >= CRAFTING_TIME) {
                ModPacketHandler.CHANNEL.sendToServer(new SoupCauldronCraftPacket(this.getBlockPos()));
                this.crafting = false;
                this.craftingTimeRemaining = 0;
                this.spoonRotation = 0;
            }
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
    
    public void addItem(ItemStack itemStack, Player player) {
        if(itemStack.is(ModItems.CROPRESSED_BEETROOT.get()) && beetroots < beetrootLimit) {
            addBeetroot(itemStack, player);
        } else if (!itemStack.isEmpty() && this.ingredients.size() < foodLimit) {
            addIngredient(itemStack, player);
        } else if(itemStack.is(Items.BOWL) && hasSoup()) {
            giveSoup(itemStack, player);
        } else if(itemStack.isEmpty()) {
            this.crafting = true;
        }
    }

    private void giveSoup(ItemStack itemStack, Player player) {
        itemStack.shrink(1);
        player.addItem(this.soup);
    }

    private void addIngredient(ItemStack itemStack, Player player) {
        this.ingredients.add(new ItemStack(itemStack.getItem(), 1));
        itemStack.shrink(1);
        int ingredients = this.ingredients.size();
        this.soupCount = beetroots + (ingredients / 4);
    }
    
    private void addBeetroot(ItemStack itemStack, Player player) {
        beetroots++;
        int ingredients = this.ingredients.size();
        this.soupCount = beetroots + (ingredients / 4);
        
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
        
        itemStack.shrink(1);
    }
    
    public float getItemsRotation(float partialTick) {
        int speed = crafting ? -10 : -2;
        return (this.itemRot + partialTick) * speed;
    }
    
    public float getSpoonRotation(float partialTick) {
        if(this.crafting) {
            return (this.spoonRotation + partialTick) * 10;
        } 
        
        return 0;
    }
    
    private Vec3 getMiddle() {
        return this.getBlockPos().getCenter().add(-0.5, 1.5, 0.5);
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
            int slot = itemTag.getByte("Slot") & 255;
            if(slot < ingredients.size()) {
                this.ingredients.add(ItemStack.of(itemTag));
            }
        }
        this.soupCount = tag.getInt("soupCount");
        this.crafting = tag.getBoolean("crafting");
        this.craftingTimeRemaining = tag.getInt("craftingTime");
        
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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return AABB.ofSize(this.getBlockPos().getCenter(), 2, 2, 2);
    }
}
