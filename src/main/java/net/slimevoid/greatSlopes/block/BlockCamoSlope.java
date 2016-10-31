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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.greatSlopes.common.property.PropertyLookup;
import net.slimevoid.greatSlopes.common.property.UnlistedPropertyBlockPos;
import net.slimevoid.greatSlopes.common.property.UnlistedPropertyIBlockState;
import net.slimevoid.greatSlopes.common.property.CamoExtendedBlockState;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;
import net.slimevoid.greatSlopes.item.SlopeItem;
import net.slimevoid.greatSlopes.tileentity.TileEntityCamoBase;
import net.slimevoid.greatSlopes.util.EnumDirectionQuatrent;
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
public class BlockCamoSlope extends Block implements ITileEntityProvider {
    public final PropertyLookup TYPE;
    public static final PropertyBool CAMO =  PropertyBool.create ("camo");
    public static final PropertyEnum DIRECTIONQUAD = PropertyEnum.create("dquad", EnumDirectionQuatrent.class);
    public static final IUnlistedProperty<EnumFacing> FACING = Properties.toUnlisted(PropertyDirection.create("facing"));
    public static final IUnlistedProperty<EnumFacing> HFACING = Properties.toUnlisted(PropertyDirection.create("hfacing"));
    public static final UnlistedPropertyIBlockState[] BLOCKSTATES = new UnlistedPropertyIBlockState[6];
    public static final UnlistedPropertyBlockPos POS = UnlistedPropertyBlockPos.create("pos");
    static{
        for(EnumFacing f : EnumFacing.values()){
            BLOCKSTATES[f.ordinal()] = UnlistedPropertyIBlockState.create(f.getName());
        }
    }
    //public int shapeCat;
    public BlockCamoSlope(String name,Material materialIn,List<String> shapeCat) {
        super(materialIn);
        setRegistryName(name);
        setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":" + name);
        this.fullBlock = false;
        this.isBlockContainer = true;
        this.TYPE = new PropertyLookup("slope",shapeCat);
        try {
            Field blockState = Block.class.getDeclaredField("blockState");
            blockState.setAccessible(true);
            blockState.set(this,this.createBlockState());
            this.setDefaultState(this.blockState.getBaseState());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.setDefaultState(this.getDefaultState().withProperty(CAMO,false));
        GameRegistry.register(this);
        Item i = new SlopeItem(this,shapeCat);
        i.setRegistryName(name);
        i.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":" + name);
        GameRegistry.register(i);
        this.setCreativeTab(ConfigLib.tabSlopes);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        if (TYPE == null) {
            return new CamoExtendedBlockState(this, new IProperty[]{CAMO, DIRECTIONQUAD}, ArrayUtils.addAll(new IUnlistedProperty[]{FACING, HFACING, POS}, BLOCKSTATES));
        } else {
            return new CamoExtendedBlockState(this, new IProperty[]{CAMO, TYPE, DIRECTIONQUAD}, ArrayUtils.addAll(new IUnlistedProperty[]{FACING, HFACING, POS}, BLOCKSTATES));
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
                        .withProperty(DIRECTIONQUAD,EnumDirectionQuatrent.get(tile.getAnchor(), tile.getQuad()));
            }
        }
        return state;
    }
    @Override
    public boolean canRenderInLayer(IBlockState state,@Nullable BlockRenderLayer layer)
    {
        return true;
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,ItemStack heldItem,@Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityCamoBase tile = ((TileEntityCamoBase) worldIn.getTileEntity(pos));
            if(tile != null)
            if (playerIn.isSneaking())
               return tile.ClearItemFromFace(side);
            else
                return tile.setFaceItemWithHeldItem(side, heldItem);
        }
        return false;
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        String currentSlope = state.getValue(TYPE);
        int i = 0;
        for (String value : TYPE.getAllowedValues()) {
            if (value == currentSlope) {
                return i;
            }
            i++;
        }
        return 0;
    }
    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
            return this.getDefaultState().withProperty(TYPE,((List<String>)TYPE.getAllowedValues()).get(meta));
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
        int x = 0;
        for (String value : TYPE.getAllowedValues()) {
            //noinspection unchecked
            list.add(new ItemStack(itemIn, 1,x));
            x++;
        }
    }

    public RayTraceResult doRayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB axisalignedbb1) {
        return rayTrace(pos, start, end, axisalignedbb1);
    }

    @Override
    public void breakBlock(@Nonnull World world,@Nonnull BlockPos pos,@Nonnull IBlockState state)
    {
        TileEntityCamoBase tile = (TileEntityCamoBase) world.getTileEntity(pos);
        if (tile != null) {
            for(EnumFacing face:EnumFacing.values()) {
                tile.ClearItemFromFace(face);
            }
        }
        super.breakBlock(world, pos, state);
    }

    public boolean isSideSolid(IBlockState state,@Nonnull IBlockAccess worldIn,@Nonnull BlockPos pos, EnumFacing side)
    {
        TileEntityCamoBase tile = (TileEntityCamoBase) worldIn.getTileEntity(pos);
        if (tile != null) {
            SlopeShape slopeType = TYPE.getSlopeShape(state.getValue(TYPE));
            return slopeType.isSideSolid(side,(EnumDirectionQuatrent) EnumDirectionQuatrent.get(tile.getAnchor(), tile.getQuad()));
        }
        return false;
    }
}