package net.slimevoid.greatSlopes.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.tileentity.TileEntityCamoBase;

import javax.annotation.Nonnull;

import static net.slimevoid.greatSlopes.block.BlockCamoSlope.BLOCKSTATES;

/**
 * Created by alcoo on 11/9/2016.
 *
 */
public class ItemCamoTool extends Item {
    public ItemCamoTool(int camoMultiplier){
        this.setMaxDamage(camoMultiplier);
        this.setMaxStackSize(1);
    }

    public IExtendedBlockState getPaintedState(ItemStack stack,EntityPlayer playerIn,EnumHand hand, IExtendedBlockState targetState,EnumFacing sideHit) {
        IExtendedBlockState ret = null;
        if(playerIn.isSneaking()){
            if(targetState.getValue(BLOCKSTATES[sideHit.ordinal()]) != null) {
                ret = targetState.withProperty(BLOCKSTATES[sideHit.ordinal()], null);
            }
        }else {
            ItemStack is = getCurrentCamo(stack, playerIn, hand);
            if (is != null) {
                //noinspection deprecation
                IBlockState camo = ((ItemBlock) is.getItem()).getBlock().getStateFromMeta(is.getMetadata());
                if(!camo.equals(targetState.getValue(BLOCKSTATES[sideHit.ordinal()]))) {
                    ret = targetState.withProperty(BLOCKSTATES[sideHit.ordinal()], camo);
                }
            }
        }
        return ret;
    }

    private ItemStack getCurrentCamo(ItemStack stack,EntityPlayer playerIn, EnumHand hand) {
        ItemStack internalBlock = ItemStack.loadItemStackFromNBT(stack.getSubCompound("ITEM", true));
        //noinspection ConstantConditions
        if(internalBlock != null) return internalBlock;
        ItemStack is = playerIn.getHeldItem(EnumHand.values()[(hand.ordinal() + 1) % 2]);
        if (is != null && isValidCamo(is, playerIn)) {
            return is;
        }else if (EnumHand.MAIN_HAND == hand) {
            int nextSlot = (playerIn.inventory.currentItem + 1) % 9;
            is = playerIn.inventory.getStackInSlot(nextSlot);
            if (is != null && isValidCamo(is,playerIn)) {
                return is;
            }
        }
        return null;
    }

    private boolean isValidCamo(ItemStack is, EntityPlayer playerIn) {
        if (is != null && is.getItem() instanceof ItemBlock) {
            Block b = ((ItemBlock) is.getItem()).getBlock();
            AxisAlignedBB c = b.getDefaultState().getCollisionBoundingBox(playerIn.getEntityWorld(), new BlockPos(0, 0, 0));
            if (!(b instanceof BlockCamoSlope) && c != null &&
                    c.maxX - c.minX == 1 &&
                    c.maxY - c.minY == 1 &&
                    c.maxZ - c.minZ == 1
                    ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    @Nonnull
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.getBlockState(pos).getBlock() instanceof BlockCamoSlope) {
            TileEntityCamoBase tile = ((TileEntityCamoBase) worldIn.getTileEntity(pos));
            if (tile != null) {
                if (playerIn.isSneaking()) {
                    tile.ClearItemFromFace(facing);
                    return EnumActionResult.SUCCESS;
                } else {
                    //check internal inventory
                    ItemStack internalBlock = ItemStack.loadItemStackFromNBT(stack.getSubCompound("ITEM", true));
                    //noinspection ConstantConditions bad inspection
                    if (internalBlock == null || internalBlock.stackSize < 1) {
                        ItemStack is = playerIn.getHeldItem(EnumHand.values()[(hand.ordinal() + 1) % 2]);
                        if (is != null && isValidCamo(is, playerIn)) {
                            internalBlock = is.copy();
                            internalBlock.stackSize= this.getMaxDamage();
                            if (!playerIn.isCreative()) {
                                if (hand == EnumHand.MAIN_HAND) {
                                    //decrement the last inventory slot
                                    playerIn.inventory.decrStackSize(playerIn.inventory.getSizeInventory() - 1, 1);
                                } else {
                                    //decrement the current held main item AKA currentItem
                                    playerIn.inventory.decrStackSize(playerIn.inventory.currentItem, 1);
                                }
                            }
                        } else if (EnumHand.MAIN_HAND == hand) {
                            int nextSlot = (playerIn.inventory.currentItem + 1) % 9;
                            is = playerIn.inventory.getStackInSlot(nextSlot);
                            if (is != null && isValidCamo(is, playerIn)) {
                                internalBlock = is.copy();
                                internalBlock.stackSize= this.getMaxDamage();
                                if (!playerIn.isCreative()) playerIn.inventory.decrStackSize(nextSlot, 1);
                            }
                        }
                    }
                    //noinspection ConstantConditions
                    if (internalBlock != null) {
                        tile.setFaceItemWithHeldItem(facing, internalBlock);
                        internalBlock.stackSize--;
                        stack.setTagInfo("ITEM",internalBlock.serializeNBT());
                        stack.setItemDamage(this.getMaxDamage()-internalBlock.stackSize);
                        if(internalBlock.stackSize<1){
                            //clear the item
                            stack.setTagInfo("ITEM",new NBTTagCompound());
                        }
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if(playerIn.isSneaking()){
            itemStackIn.setTagInfo("ITEM",new NBTTagCompound());
            itemStackIn.setItemDamage(0);
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public int getDamage(ItemStack stack)
    {
        ItemStack internalBlock = ItemStack.loadItemStackFromNBT(stack.getSubCompound("ITEM", true));
        //noinspection ConstantConditions
        return internalBlock==null?0:this.getMaxDamage()-internalBlock.stackSize;
    }
}
