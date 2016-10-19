package net.slimevoid.camolib.core.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.slimevoid.camolib.block.BlockCamoSlope;

/**
 * Created by Allen on 3/22/2015.
 *
 */
public class ConfigLib {
    public static BlockCamoSlope[] CamoBlocks;
    public static CreativeTabs tabSlopes = new CreativeTabs("SuperSlopes") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ConfigLib.CamoBlocks[0]);
        }
    };
}
