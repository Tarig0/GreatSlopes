package net.slimevoid.camolib.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.camolib.block.BlockCamoSlope;
import net.slimevoid.camolib.client.model.ModelBlockCamo;
import net.slimevoid.camolib.core.lib.ConfigLib;
import net.slimevoid.camolib.util.EnumDirectionQuatrent;
import net.slimevoid.camolib.util.EnumSlope;

import static net.slimevoid.camolib.block.BlockCamoSlope.TYPE;

/**
 * Created by Allen on 3/24/2015.
 *
 */
@SideOnly(Side.CLIENT)
class ModelBaker {
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        event.getModelRegistry().putObject(new ModelResourceLocation(ConfigLib.CamoModel, "normal"),new ModelBlockCamo());
    }


    static void setItemsforCamo(){
        for (BlockCamoSlope b : ConfigLib.CamoBlocks) {
            IBlockState dstate = b.getDefaultState();
            int length = TYPE[b.shapeCat].getAllowedValues().size();
            Item i = Item.getItemFromBlock(b);
            if (i != null) {
                for (int dmg = 0; dmg < length; dmg++) {
                    IBlockState state = dstate.withProperty(TYPE[b.shapeCat], EnumSlope.values()[(b.shapeCat * 16) + dmg]).withProperty(BlockCamoSlope.DIRECTIONQUAD, EnumDirectionQuatrent.DOWNWEST).withProperty(BlockCamoSlope.CAMO, false);
                    ModelLoader.setCustomModelResourceLocation(i, dmg, getModelResourceLocation(state));
                }
            }
        }

    }


    private static ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        return new ModelResourceLocation(ConfigLib.SlopeModel, new DefaultStateMapper().getPropertyString(state.getProperties()));
    }


}

