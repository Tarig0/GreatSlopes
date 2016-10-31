package net.slimevoid.greatSlopes.core.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.block.BlockVertBandSaw;
import net.slimevoid.greatSlopes.core.GreatSlopes;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Allen on 3/22/2015.
 *
 */
public class ConfigLib {
    public static List<BlockCamoSlope> CamoBlocks = new ArrayList<BlockCamoSlope>();
    public static BlockVertBandSaw VertBandSaw;


    public static CreativeTabs tabSlopes = new CreativeTabs("SuperSlopes") {
        @SuppressWarnings("NullableProblems")
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ConfigLib.CamoBlocks.get(0));
        }
    };


    public static ResourceLocation CamoModel = new ResourceLocation(GreatSlopes.MODID, "CamoSlope");
    public static ResourceLocation SlopeModel = new ResourceLocation(GreatSlopes.MODID, "Slope");

}
