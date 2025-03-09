package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModMobEffects;
import net.abraxator.moresnifferflowers.nutrition.Nutrition;
import net.abraxator.moresnifferflowers.nutrition.NutritionEntry;
import net.abraxator.moresnifferflowers.nutrition.NutritionStack;
import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SoupCauldronBlockEntity extends ModBlockEntity {
    public boolean beetroot = false;
    public List<NutritionStack> stacks = new ArrayList<>();
    public BlockPos center;


    public SoupCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SOUP_CAULDRON.get(), pPos, pBlockState);
        this.center = this.getBlockPos();
    }
    
    public void craft(Player player) {
        Map<NutritionType, Integer> map = new HashMap<>();
        ItemStack soup = ModItems.GIANT_SOUP.get().getDefaultInstance();
        MobEffect mobEffect = null;
        MobEffectInstance mobEffectInstance = null;
        CompoundTag tag = new CompoundTag();
        this.stacks.forEach(stack -> 
                stack.nutrition.getNutritionEntries().forEach(entry -> 
                        map.merge(entry.nutrition(), entry.weight(), Integer::sum)
        ));
       
        List<NutritionEntry> entryList = new ArrayList<>(map.entrySet()
                .stream()
                .map((Map.Entry<NutritionType, Integer> entry) -> new NutritionEntry(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(NutritionEntry::weight))
                .toList());
        
        while (entryList.get(entryList.size() - 1).weight() < entryList.get(0).weight() * 0.01) {
            entryList.remove(entryList.size() - 1);
        }
        
        int nutritionValuesSum = entryList.stream().mapToInt(NutritionEntry::weight).sum();
        
        if(valuesClose(entryList, 10)) {
            mobEffectInstance = new MobEffectInstance(ModMobEffects.MID.get(), nutritionValuesSum, nutritionValuesSum / 100);
            PotionUtils.setCustomEffects(soup, Collections.singletonList(mobEffectInstance));
        }
        
        soup.setTag(tag);
        ItemEntity entity = new ItemEntity(this.level, getMiddle().x, getMiddle().y, getMiddle().z, soup);
        player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(nutritionCapability -> {
            for (NutritionStack stack : this.stacks) {
                nutritionCapability.addItem(stack.stack.getItem());
            }
        });
        this.level.addFreshEntity(entity);
    }
    
    private boolean valuesClose(List<NutritionEntry> entryList, int tolerance) {
        List<Integer> weights = entryList.stream().map(NutritionEntry::weight).toList();
        int min = Collections.min(weights);
        int max = Collections.max(weights);
        
        return (max - min) <= tolerance;
    }
    
    public void addItem(ItemStack itemStack, Player player) {
        if(itemStack.is(ModItems.CROPRESSED_BEETROOT.get())) {
            addBeetroot();
        } else if (this.stacks.size() < 5) {
            addIngredient(itemStack, player);
        }
        
        itemStack.shrink(1);
    }
    
    private void addIngredient(ItemStack itemStack, Player player) {
        Nutrition nutrition = Nutrition.getNutritionForItem(itemStack.getItem());

        this.stacks.add(new NutritionStack(itemStack, nutrition));

        if(this.stacks.size() == 5) {
            craft(player);
        }
    }
    
    private void addBeetroot() {
        this.beetroot = true;

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
    
    private Vec3 getMiddle() {
        return this.getBlockPos().getCenter().add(-0.5, 1.5, 0.5);
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("beetroot", this.beetroot);
        tag.putInt("itemCount", stacks.size());
        tag.put("center", NbtUtils.writeBlockPos(this.center));
        for (int i = 0; i < stacks.size(); i++) {
            tag.put("stack" + i, stacks.get(i).serialize(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.stacks = new ArrayList<>();
        this.beetroot = tag.getBoolean("beetroot");
        this.center = NbtUtils.readBlockPos(tag.getCompound("center"));
        for (int i = 0; i < tag.getInt("itemCount"); i++) {
            this.stacks.add(NutritionStack.deserialize(tag.getCompound("stack" + i)));
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
