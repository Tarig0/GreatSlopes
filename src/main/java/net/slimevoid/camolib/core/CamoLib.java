package net.slimevoid.camolib.core;

import net.minecraft.block.material.Material;
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

/**
 * Created by Allen on 3/21/2015.
 *
 */
@Mod( modid = "camolib", name = "CamoLib", canBeDeactivated = false, version="0.0.1")
public class CamoLib {

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        ConfigLib.CamoBlocks = new BlockCamoSlope[(int)Math.ceil(EnumSlope.values().length/16d)];
        for(int i=0;i<ConfigLib.CamoBlocks.length;i++){
            ConfigLib.CamoBlocks[i]= new BlockCamoSlope(EnumSlope.getSectionName(i), Material.ANVIL, i );
        }

        if (event.getSide() == Side.CLIENT) clientPreInit(event);
    }

    @SideOnly(Side.CLIENT)
    public void clientPreInit(FMLPreInitializationEvent event){
        //register the modelBaker to insert the smart renderer
        MinecraftForge.EVENT_BUS.register(new ModelBaker());
        OBJLoader.INSTANCE.addDomain("camolib");
        //ModelBaker.setItemsforCamo();
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
