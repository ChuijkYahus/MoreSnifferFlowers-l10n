package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.nutrition.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class SoupCauldronBlockEntity extends ModBlockEntity {
    public boolean beetroot = false;
    public List<NutritionStack> stacks = new ArrayList<>();
    
    public SoupCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SOUP_CAULDRON.get(), pPos, pBlockState);
    }
    
    public void craft() {
        Map<NutritionType, Integer> map = new HashMap<>();
        ItemStack soup = ModItems.GIANT_SOUP.get().getDefaultInstance();
        CompoundTag tag = new CompoundTag();
        this.stacks.forEach(stack -> 
                stack.nutrition.getNutritionEntries().forEach(entry -> 
                        map.merge(entry.nutrition(), entry.weight(), Integer::sum)
        ));
        map.entrySet()
            .stream()
            .sorted(Map.Entry.<NutritionType, Integer>comparingByValue().reversed())
            .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
        
        soup.setTag(tag);
        
        ItemEntity entity = new ItemEntity(this.level, getMiddle().x, getMiddle().y, getMiddle().z, soup);
        this.level.addFreshEntity(entity);
    }
    
    public void addItem(ItemStack itemStack) {
        if(itemStack.is(ModItems.CROPRESSED_BEETROOT.get())) {
            addBeetroot();
        } else if (this.stacks.size() < 5) {
            addIngredient(itemStack);
        }
        
        itemStack.shrink(1);
    }
    
    private void addIngredient(ItemStack itemStack) {
        Nutrition nutrition = Nutrition.getNutritionForItem(itemStack.getItem());

        this.stacks.add(new NutritionStack(itemStack, nutrition));

        if(this.stacks.size() == 5) {
            craft();
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
        for (int i = 0; i < stacks.size(); i++) {
            tag.put("stack" + i, stacks.get(i).serialize(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.stacks = new ArrayList<>();
        this.beetroot = tag.getBoolean("beetroot");
        for (int i = 0; i < tag.getInt("itemCount"); i++) {
            this.stacks.add(NutritionStack.deserialize(tag.getCompound("stack" + i)));
        }
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return AABB.ofSize(this.getBlockPos().getCenter(), 2, 2, 2);
    }
}
