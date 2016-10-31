package net.slimevoid.greatSlopes.client.event;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.greatSlopes.client.renderer.block.model.ModelBlockCamo;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;



/**
 * Created by Allen on 3/24/2015.
 *
 */
@SideOnly(Side.CLIENT)
public class ModelBaker {
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        event.getModelRegistry().putObject(new ModelResourceLocation(ConfigLib.CamoModel, "normal"),new ModelBlockCamo());
    }
}


