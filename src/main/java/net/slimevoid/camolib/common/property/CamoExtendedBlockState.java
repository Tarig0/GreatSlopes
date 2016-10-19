package net.slimevoid.camolib.common.property;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.slimevoid.camolib.block.BlockCamoSlope;
import net.slimevoid.camolib.tileentity.TileEntityCamoBase;
import net.slimevoid.camolib.util.EnumDirectionQuatrent;
import net.slimevoid.camolib.util.EnumSlope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

import static net.slimevoid.camolib.block.BlockCamoSlope.CAMO;
import static net.slimevoid.camolib.block.BlockCamoSlope.DIRECTIONQUAD;
import static net.slimevoid.camolib.block.BlockCamoSlope.TYPE;

public class CamoExtendedBlockState extends ExtendedBlockState {
    public CamoExtendedBlockState(Block blockIn, IProperty<?>[] properties, IUnlistedProperty<?>[] unlistedProperties) {
        super(blockIn, properties, unlistedProperties);
    }

    @Override
    protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
    {
        if (unlistedProperties == null || unlistedProperties.isEmpty()) return super.createState(block, properties, unlistedProperties);
        return new ExtendedStateImplementation(block, properties, unlistedProperties, null);
    }

    private static class ExtendedStateImplementation extends ExtendedBlockState.ExtendedStateImplementation {

        private ExtendedStateImplementation(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> table) {
            super(block, properties, unlistedProperties, table);
        }

        @Override
        @Nonnull
        public IBlockState getActualState(@Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
            TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
            if (tile != null) {
                return this.withProperty(CAMO, tile.hasCamoData()).withProperty(DIRECTIONQUAD, EnumDirectionQuatrent.get(tile.getAnchor(), tile.getQuad()));
            } else {
                return this.withProperty(CAMO, false);
            }
        }
        @Override
        public boolean isFullBlock()
        {
            return false;
        }
        @Override
        public boolean isOpaqueCube(){
            return false;
        }
        @Override
        public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity p_185908_5_) {
            TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
            if (tile != null) {
                EnumSlope slopeType = ((EnumSlope) this.getValue(TYPE[((BlockCamoSlope)this.getBlock()).shapeCat]));
                EnumFacing anchor = tile.getAnchor();
                EnumDirectionQuatrent facing = (EnumDirectionQuatrent) EnumDirectionQuatrent.get(anchor, tile.getQuad());
                AxisAlignedBB axisalignedbb1 = slopeType.getBaseBounding(pos, anchor);
                if (axisalignedbb1 != null && mask.intersectsWith(axisalignedbb1)) {
                    list.add(axisalignedbb1);
                }

                for (int i = 0; i < slopeType.getBoundingCount(); i++) {
                    axisalignedbb1 = slopeType.getSlopedBounding(i, pos, facing);

                    if (axisalignedbb1 != null && mask.intersectsWith(axisalignedbb1)) {
                        list.add(axisalignedbb1);
                    }
                }
            }
        }

        @Override
        public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
        {
            TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
            IBlockState state = worldIn.getBlockState(pos);
            if (tile != null) {
                EnumSlope slopeType = ((EnumSlope) state.getValue(TYPE[((BlockCamoSlope)this.getBlock()).shapeCat]));
                EnumFacing anchor = tile.getAnchor();
                EnumDirectionQuatrent facing = (EnumDirectionQuatrent) EnumDirectionQuatrent.get(anchor, tile.getQuad());
                AxisAlignedBB axisalignedbb1;
                for (int i = slopeType.getBoundingCount() - 1; i > -1; i--) {
                    axisalignedbb1 = slopeType.getSlopedBounding(i, pos, facing);

                    if (axisalignedbb1 != null) {
                        axisalignedbb1 = axisalignedbb1.offset(-pos.getX(),-pos.getY(),-pos.getZ());
                        RayTraceResult ret = ((BlockCamoSlope)this.getBlock()).doRayTrace(pos, start, end,axisalignedbb1);
                        if (ret !=null){
                            return ret;
                        }
                    }
                }
                axisalignedbb1 = slopeType.getBaseBounding(pos, anchor);
                if (axisalignedbb1 != null) {
                    axisalignedbb1 = axisalignedbb1.offset(-pos.getX(),-pos.getY(),-pos.getZ());
                    RayTraceResult ret = ((BlockCamoSlope)this.getBlock()).doRayTrace( pos, start, end,axisalignedbb1);
                    if (ret !=null){
                        return ret;
                    }
                }


            }
            return null;
        }
    }
}
