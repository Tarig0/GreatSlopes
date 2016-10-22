package net.slimevoid.camolib.core.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.slimevoid.camolib.block.BlockCamoSlope;
import net.slimevoid.camolib.core.CamoLib;


/**
 * Created by Allen on 3/22/2015.
 *
 */
public class ConfigLib {
    public static BlockCamoSlope[] CamoBlocks;
    public static CreativeTabs tabSlopes = new CreativeTabs("SuperSlopes") {
        @SuppressWarnings("NullableProblems")
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ConfigLib.CamoBlocks[0]);
        }
    };
    public static ResourceLocation CamoModel = new ResourceLocation(CamoLib.MODID, "CamoSlope");
    public static ResourceLocation SlopeModel = new ResourceLocation(CamoLib.MODID, "Slope");
}
