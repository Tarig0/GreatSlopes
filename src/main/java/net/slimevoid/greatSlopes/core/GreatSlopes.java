package net.slimevoid.greatSlopes.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.block.BlockVertBandSaw;
import net.slimevoid.greatSlopes.client.event.ModelBaker;
import net.slimevoid.greatSlopes.client.renderer.block.statemap.CamoStateMapper;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;
import net.slimevoid.greatSlopes.tileentity.TileEntityCamoBase;
import net.slimevoid.greatSlopes.util.EnumDirectionQuatrent;
import net.slimevoid.greatSlopes.util.SlopeFactory;

import java.util.List;



/**
 * Created by Allen on 3/21/2015.
 *
 */
@Mod( modid = GreatSlopes.MODID, name = "GreatSlopes", version="0.0.1")
public class GreatSlopes {
    public static final String MODID = "greatslopes";
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigLib.VertBandSaw = new BlockVertBandSaw();
        SlopeFactory.generate();
        if (event.getSide() == Side.CLIENT) clientPreInit();
    }

    @SideOnly(Side.CLIENT)
    private void clientPreInit(){
        //register the modelBaker to insert the smart renderer
        MinecraftForge.EVENT_BUS.register(new ModelBaker());
        OBJLoader.INSTANCE.addDomain(MODID);
        for(BlockCamoSlope b : ConfigLib.CamoBlocks){
            ModelLoader.setCustomStateMapper(b, CamoStateMapper.INSTANCE);

            List<String> values = (List<String>)b.TYPE.getAllowedValues();
            Item i = Item.getItemFromBlock(b);
            IBlockState dstate = b.getDefaultState();
            if (i != null) {
                for (int dmg = 0; dmg < values.size(); dmg++) {
                    IBlockState state = dstate.withProperty(b.TYPE,values.get(dmg)).withProperty(BlockCamoSlope.DIRECTIONQUAD, EnumDirectionQuatrent.DOWNWEST).withProperty(BlockCamoSlope.CAMO, false);
                    ModelLoader.setCustomModelResourceLocation(i, dmg, new ModelResourceLocation(ConfigLib.SlopeModel, new DefaultStateMapper().getPropertyString(state.getProperties())));
                }
            }
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerTileEntity(TileEntityCamoBase.class, "slimevoidcamo");
    }

}
