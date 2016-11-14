package net.slimevoid.greatSlopes.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.slimevoid.greatSlopes.tileentity.TileEntityCamoBase;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

/**
 * Created by alcoo on 12/6/2015.
 *
 */
public class SlopeItem extends ItemBlock {

    private final List<String> ShapeCat;

    public SlopeItem(Block b, List<String> shapeCat) {
        super(b);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.ShapeCat = shapeCat;
    }
    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName().split(":")[0] + ":camo_slope_grade_"  + this.ShapeCat.get(stack.getItemDamage()%16);
    }
    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {

        if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
            if (world.getBlockState(pos).getBlock() == this.block) {
                TileEntityCamoBase tile = ((TileEntityCamoBase) world.getTileEntity(pos));
                if (tile != null) {
                    tile.setAnchor(side.getOpposite());
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
                        tile.setQuad(quadIdx);
                    } else {
                        tile.setQuadtoHface();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
