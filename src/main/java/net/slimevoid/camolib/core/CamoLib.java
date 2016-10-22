package net.slimevoid.camolib.core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.camolib.block.BlockCamoSlope;
import net.slimevoid.camolib.core.lib.ConfigLib;
import net.slimevoid.camolib.tileentity.TileEntityCamoBase;
import net.slimevoid.camolib.util.EnumSlope;

import javax.annotation.Nonnull;


/**
 * Created by Allen on 3/21/2015.
 *
 */
@Mod( modid = CamoLib.MODID, name = "CamoLib", version="0.0.1")
public class CamoLib {
    public static final String MODID = "camolib";
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigLib.CamoBlocks = new BlockCamoSlope[(int)Math.ceil(EnumSlope.values().length/16d)];
        for(int i=0;i<ConfigLib.CamoBlocks.length;i++){
            ConfigLib.CamoBlocks[i]= new BlockCamoSlope(EnumSlope.getSectionName(i), Material.CLOTH, i );
        }

        if (event.getSide() == Side.CLIENT) clientPreInit();
    }

    @SideOnly(Side.CLIENT)
    private void clientPreInit(){
        //register the modelBaker to insert the smart renderer
        MinecraftForge.EVENT_BUS.register(new ModelBaker());
        OBJLoader.INSTANCE.addDomain("camolib");
        for(Block b : ConfigLib.CamoBlocks){
            ModelLoader.setCustomStateMapper(b, new StateMapperBase()
            {
                @Nonnull
                protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state)
                {
                    if (state.getValue(BlockCamoSlope.CAMO)) {
                        return new ModelResourceLocation(ConfigLib.CamoModel, "normal");
                    }else {
                        return new ModelResourceLocation(ConfigLib.SlopeModel, new DefaultStateMapper().getPropertyString(state.getProperties()));
                    }
                }
            });
        }
        ModelBaker.setItemsforCamo();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerTileEntity(TileEntityCamoBase.class, "slimevoidcamo");
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent event){

    }
}
