package net.slimevoid.greatSlopes.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.greatSlopes.common.property.PropertyLookup;
import net.slimevoid.greatSlopes.common.property.UnlistedPropertyBlockPos;
import net.slimevoid.greatSlopes.common.property.UnlistedPropertyIBlockState;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;
import net.slimevoid.greatSlopes.item.SlopeItem;
import net.slimevoid.greatSlopes.tileentity.TileEntityCamoBase;
import net.slimevoid.greatSlopes.util.EnumDirectionQuadrant;
import net.slimevoid.greatSlopes.util.ICamoBlock;
import net.slimevoid.greatSlopes.util.SlopeFactory;
import net.slimevoid.greatSlopes.util.SlopeShape;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Allen on 3/22/2015.
 *
 */
public class BlockCamoSlope extends Block implements ITileEntityProvider, ICamoBlock {
    public PropertyLookup<SlopeShape> TYPE;
    public static final PropertyBool CAMO =  PropertyBool.create ("camo");
    public static final PropertyEnum DIRECTIONQUAD = PropertyEnum.create("dquad", EnumDirectionQuadrant.class);
    public static final IUnlistedProperty<EnumFacing> FACING = Properties.toUnlisted(PropertyDirection.create("facing"));
    public static final IUnlistedProperty<EnumFacing> HFACING = Properties.toUnlisted(PropertyDirection.create("hfacing"));
    public static final UnlistedPropertyIBlockState[] BLOCKSTATES = new UnlistedPropertyIBlockState[6];
    public static final UnlistedPropertyBlockPos POS = UnlistedPropertyBlockPos.create("pos");
    static{
        for(EnumFacing f : EnumFacing.values()){
            BLOCKSTATES[f.ordinal()] = UnlistedPropertyIBlockState.create(f.getName());
        }
    }
    public BlockCamoSlope(String name,Material materialIn,List<String> shapeCat) {
        super(materialIn);
        setRegistryName(name);
        setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":" + name);
        this.fullBlock = false;
        this.isBlockContainer = true;
        this.createType(shapeCat);
        Field blockState = ReflectionHelper.findField(Block.class,"field_176227_L","blockState");
        blockState.setAccessible(true);
        try {
            blockState.set(this, this.createBlockState());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.setDefaultState(this.blockState.getBaseState());
        this.setDefaultState(this.getDefaultState().withProperty(CAMO,false));
        GameRegistry.register(this);
        Item i = new SlopeItem(this,shapeCat);
        i.setRegistryName(name);
        i.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":" + name);
        GameRegistry.register(i);
    }


    protected void createType(List<String> shapeCat) {
        this.TYPE = new PropertyLookup<SlopeShape>("slope",shapeCat){
            @Override
            public SlopeShape getLookup(String value) {
                return SlopeFactory.Shapes.get(value);
            }
        };
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        if (TYPE == null) {
            return new ExtendedBlockState(this, new IProperty[]{CAMO, DIRECTIONQUAD}, ArrayUtils.addAll(new IUnlistedProperty[]{FACING, HFACING, POS}, BLOCKSTATES));
        } else {
            return new ExtendedBlockState(this, new IProperty[]{CAMO, TYPE, DIRECTIONQUAD}, ArrayUtils.addAll(new IUnlistedProperty[]{FACING, HFACING, POS}, BLOCKSTATES));
        }
    }
    @Override
    @Nonnull
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
            if (tile != null) {
                for (EnumFacing f : EnumFacing.VALUES) {
                    state = ((IExtendedBlockState) state).withProperty(BLOCKSTATES[f.ordinal()], tile.getStateForSide(f));
                }
                state = ((IExtendedBlockState)state).withProperty(FACING,tile.getFacing())
                        .withProperty(HFACING,tile.getHorzFacing())
                        .withProperty(POS,tile.getPos())
                        .withProperty(DIRECTIONQUAD, EnumDirectionQuadrant.get(tile.getAnchor(), tile.getQuad()));
            }
        }
        return state;
    }
    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state,@Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
        if (tile != null) {
            return state.withProperty(CAMO, tile.hasCamoData()).withProperty(DIRECTIONQUAD, EnumDirectionQuadrant.get(tile.getAnchor(), tile.getQuad()));
        } else {
            return state.withProperty(CAMO, false);
        }
    }
    @Override
    public boolean canRenderInLayer(IBlockState state,@Nullable BlockRenderLayer layer)
    {
        return true;
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }
    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return new TileEntityCamoBase();
    }
    @Override
    @Nonnull
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getStateFromMeta(meta);
    }
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
        if (tile != null) {
            tile.setHorzFacing(placer.getHorizontalFacing().getOpposite());
            tile.setFacing(BlockPistonBase.getFacingFromEntity(pos, placer));
        }
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return Math.max(0,this.TYPE.getAllowedValuesList().indexOf( state.getValue(this.TYPE)));
    }
    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
            return this.getDefaultState().withProperty(this.TYPE,this.TYPE.getAllowedValuesList().get(meta));
    }
    @Override
    public int getLightValue(@Nonnull IBlockState state,IBlockAccess world,@Nonnull BlockPos pos) {
        TileEntityCamoBase tile = (TileEntityCamoBase) world.getTileEntity(pos);
        if (tile != null) {
            return tile.getLightValue();
        } else return super.getLightValue(state,world, pos);
    }
    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(@Nonnull Item itemIn, CreativeTabs tab, List list) {
        for (int x = 0; x< TYPE.getAllowedValues().size();x++) {
            //noinspection unchecked
            list.add(new ItemStack(itemIn, 1,x));
        }
    }

    @Override
    public boolean isSideSolid(IBlockState state,@Nonnull IBlockAccess worldIn,@Nonnull BlockPos pos, EnumFacing side)
    {
        TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
        if (tile != null) {
            SlopeShape slopeType = TYPE.getLookup(state.getValue(TYPE));
            return slopeType.isSideSolid(side,(EnumDirectionQuadrant) EnumDirectionQuadrant.get(tile.getAnchor(), tile.getQuad()));
        }
        return false;
    }
    @Override
    @SuppressWarnings("deprecation")
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        TileEntityCamoBase tile = (TileEntityCamoBase) source.getTileEntity(pos);
        if (tile != null) {
            SlopeShape slopeType = TYPE.getLookup(state.getValue(TYPE));
            EnumDirectionQuadrant facing = (EnumDirectionQuadrant) EnumDirectionQuadrant.get(tile.getAnchor(), tile.getQuad());
            return slopeType.getBoundingBox(facing);
        }
        return FULL_BLOCK_AABB;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state,@Nonnull World worldIn,@Nonnull  BlockPos pos,@Nonnull  AxisAlignedBB mask,@Nonnull  List<AxisAlignedBB> list, @Nullable Entity p_185908_5_) {
        TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
            if (tile != null) {
                SlopeShape slopeType = TYPE.getLookup(state.getValue(TYPE));
                EnumFacing anchor = tile.getAnchor();
                AxisAlignedBB axisalignedbb1 = slopeType.getBaseBounding(pos, anchor);
            if (axisalignedbb1 != null && mask.intersectsWith(axisalignedbb1)) {
                list.add(axisalignedbb1);
            }
            EnumDirectionQuadrant facing = (EnumDirectionQuadrant) EnumDirectionQuadrant.get(anchor, tile.getQuad());

            for (int i = 0; i < slopeType.getBoundingCount(); i++) {
                for (AxisAlignedBB box:slopeType.getSlopedBounding(i, pos, facing)) {
                    if (mask.intersectsWith(box)) {
                        list.add(box);
                    }
                }
            }
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    public RayTraceResult collisionRayTrace(IBlockState state,@Nonnull World worldIn,@Nonnull  BlockPos pos,@Nonnull  Vec3d start,@Nonnull  Vec3d end)
    {
        TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);

        if (tile != null) {
            SlopeShape slopeType = TYPE.getLookup(state.getValue(TYPE));
            EnumFacing anchor = tile.getAnchor();
            EnumDirectionQuadrant facing = (EnumDirectionQuadrant) EnumDirectionQuadrant.get(anchor, tile.getQuad());
            AxisAlignedBB axisalignedbb1;
            RayTraceResult ret = null;
            for (int i = slopeType.getBoundingCount() - 1; i > -1; i--) {
                RayTraceResult sliceCanadite = null;
                for (AxisAlignedBB box :slopeType.getSlopedBounding(i, BlockPos.ORIGIN, facing)){
                    RayTraceResult canadite = this.rayTrace(pos, start, end,box);
                    if(canadite!=null) {
                        if (sliceCanadite == null || start.distanceTo(sliceCanadite.hitVec) > start.distanceTo(canadite.hitVec)) {
                            sliceCanadite = canadite;
                        }
                    }
                }
                if(sliceCanadite!=null) {
                    if (ret == null || start.distanceTo(ret.hitVec) > start.distanceTo(sliceCanadite.hitVec)) {
                        ret = sliceCanadite;
                    } else {
                        return ret;
                    }
                }
            }
            axisalignedbb1 = slopeType.getBaseBounding(pos, anchor);
            if (axisalignedbb1 != null) {
                axisalignedbb1 = axisalignedbb1.offset(-pos.getX(),-pos.getY(),-pos.getZ());
                RayTraceResult canadite = this.rayTrace( pos, start, end,axisalignedbb1);
                if(canadite!=null) {
                    if (ret == null || start.distanceTo(ret.hitVec) > start.distanceTo(canadite.hitVec)) {
                        ret = canadite;
                    } else {
                        return ret;
                    }
                }
            }
            return ret;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState,@Nonnull IBlockAccess blockAccess,@Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

}