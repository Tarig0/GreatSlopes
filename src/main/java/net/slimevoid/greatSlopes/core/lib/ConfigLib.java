package net.slimevoid.greatSlopes.core.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.block.BlockBandSaw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Allen on 3/22/2015.
 *
 */
public class ConfigLib {
    public static List<BlockCamoSlope> CamoBlocks = new ArrayList<>();
    public static BlockBandSaw VertBandSaw;
    public static BlockBandSaw HorzBandSaw;
    @SideOnly(Side.CLIENT)
    public static ResourceLocation SlopeModel;
    private static Configuration config;

    public static CreativeTabs tabSlopes = new CreativeTabs("SuperSlopes") {
        @SuppressWarnings("NullableProblems")
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ConfigLib.CamoBlocks.get(0));
        }
    };
    public static Item Glue;
    public static Item BasicCamoTool;
    public static int CamoMultiplier = 6;

    public static void InitConfig(File suggestedConfigurationFile) {
        config = new Configuration(suggestedConfigurationFile);
        config.setCategoryComment("Recipes","Recipes for the saws as json {input:{output1:#,output2:#}}");

    }
    public static void saveConfig() {
        config.save();
    }
    public static String GetVertGrade100Recipes() {
        return config.get("Recipes","VerticalSawGrade100","{" +
                "CamoSlopeGrade0base16:{100:2}," +
                "CamoSlopeGrade0base14:{100shave2:1,100shaving14:1}," +
                "CamoSlopeGrade0base12:{100shave4:1,100shaving12:1}," +
                "CamoSlopeGrade0base10:{100shave6:1,100shaving10:1}," +
                "CamoSlopeGrade0base8:{100shave8:1,100shaving8:1}," +
                "CamoSlopeGrade0base6:{100shave10:1,100shaving6:1}," +
                "CamoSlopeGrade0base4:{100shave12:1,100shaving4:1}," +
                "CamoSlopeGrade0base2:{100shave14:1,100shaving2:1}" +
                "}").getString();
    }

    public static String GetVertGrade87_5Recipes() {
        return config.get("Recipes","VerticalSawGrade87_5","{" +
                "CamoSlopeGrade0base16:{87_5:1,87_5base2:1}," +
                "CamoSlopeGrade0base14:{87_5:2}," +
                "CamoSlopeGrade0base12:{100shave2:1,100shaving12:1}," +
                "CamoSlopeGrade0base10:{100shave4:1,100shaving10:1}," +
                "CamoSlopeGrade0base8:{100shave6:1,100shaving8:1}," +
                "CamoSlopeGrade0base6:{100shave8:1,100shaving6:1}," +
                "CamoSlopeGrade0base4:{100shave10:1,100shaving4:1}," +
                "CamoSlopeGrade0base2:{100shave12:1,100shaving2:1}" +
                "}").getString();
    }

    public static String GetVertGrade75Recipes() {
        return config.get("Recipes","VerticalSawGrade75","{" +
                "CamoSlopeGrade0base16:{75:1,75base4:1}," +
                "CamoSlopeGrade0base14:{75:1,75base2:1}," +
                "CamoSlopeGrade0base12:{75:2}," +
                "CamoSlopeGrade0base10:{75shave2:1,75shaving10:1}," +
                "CamoSlopeGrade0base8:{75shave4:1,75shaving8:1}," +
                "CamoSlopeGrade0base6:{75shave6:1,75shaving6:1}," +
                "CamoSlopeGrade0base4:{75shave8:1,75shaving4:1}," +
                "CamoSlopeGrade0base2:{75shave10:1,75shaving2:1}" +
                "}").getString();
    }

    public static String GetVertGrade62_5Recipes() {
        return config.get("Recipes","VerticalSawGrade62_5","{" +
                "CamoSlopeGrade0base16:{62_5:1,62_5base6:1}," +
                "CamoSlopeGrade0base14:{62_5:1,62_5base4:1}," +
                "CamoSlopeGrade0base12:{62_5:1,62_5base2:1}," +
                "CamoSlopeGrade0base10:{62_5:2}," +
                "CamoSlopeGrade0base8:{62_5shave2:1,62_5shaving8:1}," +
                "CamoSlopeGrade0base6:{62_5shave4:1,62_5shaving6:1}," +
                "CamoSlopeGrade0base4:{62_5shave6:1,62_5shaving4:1}," +
                "CamoSlopeGrade0base2:{62_5shave8:1,62_5shaving2:1}" +
                "}").getString();
    }

    public static String GetVertGrade50Recipes() {
        return config.get("Recipes","VerticalSawGrade50","{" +
                "CamoSlopeGrade0base16:{50:1,50base8:1}," +
                "CamoSlopeGrade0base14:{50:1,50base6:1}," +
                "CamoSlopeGrade0base12:{50:1,50base4:1}," +
                "CamoSlopeGrade0base10:{50:1,50base2:1}," +
                "CamoSlopeGrade0base8:{50:2}," +
                "CamoSlopeGrade0base6:{50shave2:1,50shaving6:1}," +
                "CamoSlopeGrade0base4:{50shave4:1,50shaving4:1}," +
                "CamoSlopeGrade0base2:{50shave6:1,50shaving2:1}" +
                "}").getString();
    }

    public static String GetVertGrade37_5Recipes() {
        return config.get("Recipes","VerticalSawGrade37_5","{" +
                "CamoSlopeGrade0base16:{37_5:1,37_5base10:1}," +
                "CamoSlopeGrade0base14:{37_5:1,37_5base8:1}," +
                "CamoSlopeGrade0base12:{37_5:1,37_5base6:1}," +
                "CamoSlopeGrade0base10:{37_5:1,37_5base4:1}," +
                "CamoSlopeGrade0base8:{37_5:1,37_5base2:1}," +
                "CamoSlopeGrade0base6:{37_5:2}," +
                "CamoSlopeGrade0base4:{37_5shave2:1,37_5shaving4:1}," +
                "CamoSlopeGrade0base2:{37_5shave4:1,37_5shaving2:1}" +
                "}").getString();
    }

    public static String GetVertGrade25Recipes() {
        return config.get("Recipes","VerticalSawGrade25","{" +
                "CamoSlopeGrade0base16:{25:1,25base12:1}," +
                "CamoSlopeGrade0base14:{25:1,25base10:1}," +
                "CamoSlopeGrade0base12:{25:1,25base8:1}," +
                "CamoSlopeGrade0base10:{25:1,25base6:1}," +
                "CamoSlopeGrade0base8:{25:1,25base4:1}," +
                "CamoSlopeGrade0base6:{25:1,25base2:1}," +
                "CamoSlopeGrade0base4:{25:2}," +
                "CamoSlopeGrade0base2:{25shave2:1,25shaving2:1}" +
                "}").getString();
    }

    public static String GetVertGrade12_5Recipes() {
        return config.get("Recipes","VerticalSawGrade12_5","{" +
                "CamoSlopeGrade0base16:{12_5:1,12_5base14:1}," +
                "CamoSlopeGrade0base14:{12_5:1,12_5base12:1}," +
                "CamoSlopeGrade0base12:{12_5:1,12_5base10:1}," +
                "CamoSlopeGrade0base10:{12_5:1,12_5base8:1}," +
                "CamoSlopeGrade0base8:{12_5:1,12_5base6:1}," +
                "CamoSlopeGrade0base6:{12_5:1,12_5base4:1}," +
                "CamoSlopeGrade0base4:{12_5:1,12_5base2:1}," +
                "CamoSlopeGrade0base2:{12_5:2}" +
                "}").getString();
    }

    public static String GetHorzPosition14Recipes() {
        return config.get("Recipes","HorizontalSawPosition14","{" +
                "CamoSlopeGrade0base16:{0base14:1,0base2:1}" +
                ",CamoSlopeGrade100:{100shave2:1,100shaving2:1}" +
                ",CamoSlopeGrade87_5base2:{87_5shave2base2:1,87_5shaving2:1}" +
                ",CamoSlopeGrade75base4:{75shave2base4:1,75shaving2:1}" +
                ",CamoSlopeGrade62_5base6:{62_5shave2base6:1,62_5shaving2:1}" +
                ",CamoSlopeGrade50base8:{50shave2base8:1,50shaving2:1}" +
                ",CamoSlopeGrade37_5base10:{37_5shave2base10:1,37_5shaving2:1}" +
                ",CamoSlopeGrade25base12:{25shave2base12:1,25shaving2:1}" +
                ",CamoSlopeGrade12_5base14:{12_5shave2base14:1,12_5shaving2:1}" +
                "}").getString();
    }

    public static String GetHorzPosition12Recipes() {
        return config.get("Recipes","HorizontalSawPosition12","{" +
                "CamoSlopeGrade0base16:{0base12:1,0base4:1}," +
                "CamoSlopeGrade0base14:{0base12:1,0base2:1}" +
                ",CamoSlopeGrade100:{100shave4:1,100shaving4:1}" +
                ",CamoSlopeGrade87_5:{87_5shave2:1,87_5shaving2:1}" +
                ",CamoSlopeGrade87_5base2:{87_5shave4base2:1,87_5shaving4:1}" +
                ",CamoSlopeGrade75base2:{75shave2base2:1,75shaving2:1}" +
                ",CamoSlopeGrade75base4:{75shave4base4:1,75shaving4:1}" +
                ",CamoSlopeGrade62_5base4:{62_5shave2base4:1,62_5shaving2:1}" +
                ",CamoSlopeGrade62_5base6:{62_5shave4base6:1,62_5shaving4:1}" +
                ",CamoSlopeGrade50base6:{50shave2base6:1,50shaving2:1}" +
                ",CamoSlopeGrade50base8:{50shave4base8:1,50shaving4:1}" +
                ",CamoSlopeGrade37_5base8:{37_5shave2base8:1,37_5shaving2:1}" +
                ",CamoSlopeGrade37_5base10:{37_5shave4base10:1,37_5shaving4:1}" +
                ",CamoSlopeGrade25base10:{25shave2base10:1,25shaving2:1}" +
                ",CamoSlopeGrade25base12:{25shave4base12:1,25shaving4:1}" +
                ",CamoSlopeGrade12_5base12:{12_5shave2base12:1,12_5shaving2:1}" +
                ",CamoSlopeGrade12_5base14:{12_5shave4base14:1,12_5shaving4:1}" +
                "}").getString();
    }

    public static String GetHorzPosition10Recipes() {
        return config.get("Recipes","HorizontalSawPosition10","{" +
                "CamoSlopeGrade0base16:{0base10:1,0base6:1}," +
                "CamoSlopeGrade0base14:{0base10:1,0base4:1}," +
                "CamoSlopeGrade0base12:{0base10:1,0base2:1}" +
                ",CamoSlopeGrade100:{100shave6:1,100shaving6:1}" +
                ",CamoSlopeGrade87_5:{87_5shave4:1,87_5shaving4:1}" +
                ",CamoSlopeGrade87_5base2:{87_5shave6base2:1,87_5shaving6:1}" +
                //TODO::complete recipes


                "}").getString();
    }

    public static String GetHorzPosition8Recipes() {
        return config.get("Recipes","HorizontalSawPosition8","{" +
                "CamoSlopeGrade0base16:{0base8:1,0base8:1}," +
                "CamoSlopeGrade0base14:{0base8:1,0base6:1}," +
                "CamoSlopeGrade0base12:{0base8:1,0base4:1}," +
                "CamoSlopeGrade0base10:{0base8:1,0base2:1}" +
                "}").getString();
    }

    public static String GetHorzPosition6Recipes() {
        return config.get("Recipes","HorizontalSawPosition6","{" +
                "CamoSlopeGrade0base16:{0base6:1,0base10:1}," +
                "CamoSlopeGrade0base14:{0base6:1,0base8:1}," +
                "CamoSlopeGrade0base12:{0base6:1,0base6:1}," +
                "CamoSlopeGrade0base10:{0base6:1,0base4:1}," +
                "CamoSlopeGrade0base8:{0base6:1,0base2:1}" +
                "}").getString();
    }

    public static String GetHorzPosition4Recipes() {
        return config.get("Recipes","HorizontalSawPosition4","{" +
                "CamoSlopeGrade0base16:{0base4:1,0base12:1}," +
                "CamoSlopeGrade0base14:{0base4:1,0base10:1}," +
                "CamoSlopeGrade0base12:{0base4:1,0base8:1}," +
                "CamoSlopeGrade0base10:{0base4:1,0base6:1}," +
                "CamoSlopeGrade0base8:{0base4:1,0base4:1}," +
                "CamoSlopeGrade0base6:{0base4:1,0base2:1}" +
                "}").getString();
    }

    public static String GetHorzPosition2Recipes() {
        return config.get("Recipes","HorizontalSawPosition2","{" +
                "CamoSlopeGrade0base16:{0base2:1,0base14:1}," +
                "CamoSlopeGrade0base14:{0base2:1,0base12:1}," +
                "CamoSlopeGrade0base12:{0base2:1,0base10:1}," +
                "CamoSlopeGrade0base10:{0base2:1,0base8:1}," +
                "CamoSlopeGrade0base8:{0base2:1,0base6:1}," +
                "CamoSlopeGrade0base6:{0base2:1,0base4:1}," +
                "CamoSlopeGrade0base4:{0base2:1,0base2:1}" +
                "}").getString();
    }
}
