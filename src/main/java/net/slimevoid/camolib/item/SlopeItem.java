package net.slimevoid.camolib.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.slimevoid.camolib.tileentity.TileEntityCamoBase;
import net.slimevoid.camolib.util.EnumSlope;

import java.awt.*;

/**
 * Created by alcoo on 12/6/2015.
 */
public class SlopeItem extends ItemBlock {
    private final int shapeCat;

    public SlopeItem(Block b, Integer shapeCat) {
        super(b);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.shapeCat = shapeCat;
    }
    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName().split(":")[0] + ":CamoSlopeGrade"  + EnumSlope.values()[(this.shapeCat * 16)+(stack.getItemDamage()%16)];
    }
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {

        if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
            if (world.getBlockState(pos).getBlock() == this.block) {

                ((TileEntityCamoBase) world.getTileEntity(pos)).setAnchor(side.getOpposite());
                if (side != EnumFacing.UP && side != EnumFacing.DOWN) {
                    double tX = -0, tY = -0;
                    switch (side.getOpposite()) {
                        case NORTH:
                            tX = hitX * 2;
                            tY = hitY * 2;
                            break;
                        case SOUTH:
                            tX = (1 - hitX) * 2;
                            tY = hitY * 2;
                            break;
                        case WEST:
                            tX = (1 - hitZ) * 2;
                            tY = hitY * 2;
                            break;
                        case EAST:
                            tX = hitZ * 2;
                            tY = hitY * 2;
                            break;
                    }
                    int quadIdx;
                    Polygon tester = new Polygon(new int[]{0, 1, 2}, new int[]{2, 1, 2}, 3);
                    if (tester.contains(tX, tY)) {
                        quadIdx = 2;
                    } else {
                        tester = new Polygon(new int[]{2, 1, 2}, new int[]{0, 1, 2}, 3);
                        if (tester.contains(tX, tY)) {
                            quadIdx = 1;
                        } else {
                            tester = new Polygon(new int[]{2, 1, 0}, new int[]{0, 1, 0}, 3);
                            if (tester.contains(tX, tY)) {
                                quadIdx = 0;
                            } else {
                                quadIdx = 3;
                            }
                        }
                    }
                    ((TileEntityCamoBase) world.getTileEntity(pos)).setQuad(quadIdx);
                }else{
                    ((TileEntityCamoBase) world.getTileEntity(pos)).setQuadtoHface();
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
