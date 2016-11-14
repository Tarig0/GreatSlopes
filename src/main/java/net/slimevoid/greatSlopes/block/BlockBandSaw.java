package net.slimevoid.greatSlopes.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.greatSlopes.core.GreatSlopes;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * Created by alcoo on 10/30/2016.
 */
public class BlockBandSaw extends Block {
    private final PropertyInteger SAWPOSITION;
    private static final PropertyBool NORTHSOUTH = PropertyBool.create("north_south");
    private ArrayList<HashMap<String, ItemStack[]>> Recipes = new ArrayList<>();
private final int maxPos;
    public BlockBandSaw(boolean isVert) {
        super(Material.CLAY);
        maxPos = isVert ? 7 : 6;
        SAWPOSITION = PropertyInteger.create("saw_position", 0, maxPos);
        Field blockState = ReflectionHelper.findField(Block.class,"field_176227_L","blockState");
        try {
            blockState.set(this, this.createBlockState());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.setDefaultState(this.blockState.getBaseState());
        this.setDefaultState(this.getDefaultState().withProperty(SAWPOSITION, maxPos));
        String name = (isVert ? "vert" : "horz") + "_band_saw";
        setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":" + name);
        setRegistryName(name);
        GameRegistry.register(this);
        Item i = new ItemBlock(this);
        i.setRegistryName(name);
        i.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":" + name);
        GameRegistry.register(i);
        this.setCreativeTab(ConfigLib.tabSlopes);


        for (int x = 0; x <= maxPos; x++) {
            Recipes.add(new HashMap<>());
        }
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        if (SAWPOSITION == null) {
            return new BlockStateContainer(this, NORTHSOUTH);
        } else {
            return new BlockStateContainer(this, NORTHSOUTH,SAWPOSITION);
        }
    }

    @Override
    @Nonnull
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(NORTHSOUTH, placer.getHorizontalFacing() ==EnumFacing.NORTH || placer.getHorizontalFacing() ==EnumFacing.SOUTH), 2);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | (state.getValue(NORTHSOUTH)?1:0);
        i = i | state.getValue(SAWPOSITION) << 1;
        return i;
    }
    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(NORTHSOUTH, (meta & 1) == 1).withProperty(SAWPOSITION, (meta & 15) >> 1);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
        {
            return true;
        }
        else if(side.getAxis() == (state.getValue(NORTHSOUTH)? EnumFacing.Axis.Z:EnumFacing.Axis.X)){
            if (heldItem != null) {
                ItemStack[] items = Recipes.get(state.getValue(SAWPOSITION)).get(heldItem.getUnlocalizedName());
                if (items != null) {
                    heldItem.stackSize -= 1;
                    for (ItemStack i : items) {
                        BehaviorDefaultDispenseItem.doDispense(worldIn, i.copy(), 8, side.getOpposite(), getDispensePosition(side.getOpposite(), pos));
                    }
                }
            }
            return true;
        }else if(side.getAxis() != EnumFacing.Axis.Y){
            int newPos;
            if(playerIn.isSneaking()){
                newPos = (state.getValue(SAWPOSITION) - 1);
                if (newPos == -1){
                    newPos = maxPos;
                }
            }else{
                newPos = (state.getValue(SAWPOSITION) + 1)%(maxPos+1);
            }

            worldIn.setBlockState(pos,state.withProperty(SAWPOSITION,newPos),3);
            return true;
        }
        return false;
    }

    private static IPosition getDispensePosition(EnumFacing enumfacing,BlockPos coords)
    {
        double d0 = coords.getX() + 0.7D * (double)enumfacing.getFrontOffsetX();
        double d1 = coords.getY() + 0.7D * (double)enumfacing.getFrontOffsetY();
        double d2 = coords.getZ() + 0.7D * (double)enumfacing.getFrontOffsetZ();
        return new PositionImpl(d0, d1, d2);
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public void AddRecipes(int position, String recipeJson) {
        JsonElement jelement = new JsonParser().parse(recipeJson);
        JsonObject jobj = jelement.getAsJsonObject();
        for(Map.Entry<String,JsonElement> r: jobj.entrySet()){
            String s = r.getKey();
            if(!s.contains(":")){
                s = GreatSlopes.MODID + ":" + s;
            }
            if(!s.startsWith("tile.")){
                s = "tile." + s;
            }
            Set<Map.Entry<String,JsonElement>> om = r.getValue().getAsJsonObject().entrySet();
            ItemStack[] results = new ItemStack[om.size()];
            int i = 0;
            for(Map.Entry<String,JsonElement> o: om){
                for(BlockCamoSlope b : ConfigLib.CamoBlocks){
                    List<String> values = b.TYPE.getAllowedValuesList();
                    int dmg = values.indexOf(o.getKey());
                    if(dmg>-1){
                        //noinspection ConstantConditions
                        results[i] = new ItemStack(Item.getItemFromBlock(b),o.getValue().getAsInt(),dmg) ;
                        i++;
                        break;
                    }
                }

            }
            this.Recipes.get(position).put(s,results);
        }
    }
}
