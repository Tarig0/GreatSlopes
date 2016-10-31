package net.slimevoid.greatSlopes.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.slimevoid.greatSlopes.common.property.CamoExtendedBlockState;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;

/**
 * Created by alcoo on 10/30/2016.
 */
public class BlockVertBandSaw extends Block {
    public static final IUnlistedProperty<Integer> SAWPOSITION = Properties.toUnlisted(PropertyInteger.create("sawposition",1,8));
    public BlockVertBandSaw() {
        super(Material.CLAY);
        this.setDefaultState(((IExtendedBlockState)this.getDefaultState()).withProperty(SAWPOSITION,1));
        setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":VertBandSaw");
        setRegistryName("VertBandSaw");
        GameRegistry.register(this);
        Item i = new ItemBlock(this);
        i.setRegistryName("VertBandSaw");
        i.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":VertBandSaw");
        GameRegistry.register(i);
        this.setCreativeTab(ConfigLib.tabSlopes);
    }
    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[]{SAWPOSITION});
    }
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return ((IExtendedBlockState) state).withProperty(SAWPOSITION,1);
    }


}
