package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.recipes.RebrewedTippedArrowRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class SoupCauldronBlockEntity extends ModBlockEntity {
    public List<ItemStack> items = new ArrayList<>();
    
    public SoupCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SOUP_CAULDRON.get(), pPos, pBlockState);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return AABB.ofSize(this.getBlockPos().getCenter(), 2, 2, 2);
    }
    
    public boolean addItem(ItemStack item) {
        if(this.items.size() < 5) {
            this.items.add(item);
            return true;
        }

        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("itemCount", items.size());
        for (int i = 0; i < items.size(); i++) {
            tag.put("item" + i, items.get(i).save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = new ArrayList<>();
        for (int i = 0; i < tag.getInt("itemCount"); i++) {
            this.items.add(ItemStack.of(tag.getCompound("item" + i)));
        }
    }
}
