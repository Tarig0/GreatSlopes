package net.slimevoid.camolib.core;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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

/**
 * Created by Allen on 3/24/2015.
 *
 */
@SideOnly(Side.CLIENT)
public class ModelBaker {
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        for (BlockCamoSlope b : ConfigLib.CamoBlocks) {
            OverrideCamoModel(event, b);
        }
    }

    private void OverrideCamoModel(ModelBakeEvent event, BlockCamoSlope b) {
        @SuppressWarnings("unchecked") UnmodifiableIterator<IBlockState> iterator = b.getBlockState().getValidStates().iterator();
        while (iterator.hasNext()){
            IBlockState state = iterator.next();
            if ((Boolean) state.getValue(BlockCamoSlope.CAMO)){
                event.getModelRegistry().putObject(getModelResourceLocation(state), new ModelBlockCamo());
            }
        }
    }

    public static void setItemsforCamo(){
        for (BlockCamoSlope b : ConfigLib.CamoBlocks) {
            IBlockState dstate = b.getDefaultState();
            int length = b.TYPE[b.shapeCat].getAllowedValues().size();
            Item i = Item.getItemFromBlock(b);
            for (int dmg = 0; dmg < length; dmg++) {
                IBlockState state = dstate.withProperty(b.TYPE[b.shapeCat], EnumSlope.values()[(b.shapeCat*16) + dmg]).withProperty(BlockCamoSlope.DIRECTIONQUAD, EnumDirectionQuatrent.DOWNWEST).withProperty(BlockCamoSlope.CAMO, false);
                ModelLoader.setCustomModelResourceLocation(i, dmg, getModelResourceLocation(state));
            }
        }

    }


    protected static ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        return new ModelResourceLocation(net.minecraftforge.fml.common.registry.GameData.getBlockRegistry().getNameForObject(state.getBlock()), new DefaultStateMapper().getPropertyString(state.getProperties()));
    }
}
