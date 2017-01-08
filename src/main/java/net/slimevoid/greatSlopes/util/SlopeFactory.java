package net.slimevoid.greatSlopes.util;

import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.SplashProgress;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;

import java.util.*;

/**
 * Created by alcoo on 10/30/2016.
 *
 */
public class SlopeFactory {
    private static final int maxRise = 8; //100
    private static final int minRise = 0; //0
    private static final int minCornerRise = 1; //12_5
    private static final int maxHeight = 8;
    private static final int groupSize = 16; //creates a block per 16
    private static final int minShaving = 1;
    private static final Double run = 8.0;
    public static final HashMap<String, SlopeShape> Shapes = new LinkedHashMap<>();
    public static net.minecraftforge.fml.common.ProgressManager.ProgressBar bar;
    public static void generate() {
        bar = net.minecraftforge.fml.common.ProgressManager.push("Generating Shapes", 92);
        BlockInfo bInf = new BlockInfo("camo_slope_grade");
        for (int rise = maxRise; rise >= minRise; rise--) {
            int shave = 0;
            int minBase = rise > 0 ? 0 : 1;
            do {
                int base = minBase;
                do {
                    bInf.constructSingleSlope(rise, shave, base);
                    base++;
                } while (rise - shave + base <= maxHeight);
                shave++;
            } while (rise - shave > 0);
            int shaving = minShaving;
            while (shaving < rise) {
                bInf.constructSingleShavingSlope(rise, shaving);
                shaving++;
            }
        }
        //ensure the single slopes are cleared and created
        bInf.flushValidValues(ConfigLib.tabSlopes);
        int tabCornerSlopesIndex = ConfigLib.CamoBlocks.size();
        ConfigLib.tabCornerSlopes = new CreativeTabs("great_corner_slopes") {
            @SuppressWarnings("NullableProblems")
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(ConfigLib.CamoBlocks.get(tabCornerSlopesIndex));
            }
        };
        bInf = new BlockInfo("camo_ext_corner_slope_grade");
        for (int rise = maxRise; rise >= minCornerRise; rise--) {
            for (int riseL = maxRise; riseL >= minCornerRise; riseL--) {
                bInf.ConstructShape(ConfigLib.tabCornerSlopes, false, 0, 0, rise, riseL);
            }
        }
        bInf.flushValidValues(ConfigLib.tabCornerSlopes);
        bInf = new BlockInfo("camo_wedge_slope_grade");
        for (int rise = maxRise; rise >= minCornerRise; rise--) {
            for (int riseB = maxRise; riseB >= minCornerRise; riseB--) {
                for (int riseL = maxRise; riseL >= minCornerRise; riseL--) {
                    bInf.ConstructShape(ConfigLib.tabCornerSlopes, false, 0, 0, rise, riseL, riseB);
                }
            }
        }

        bInf = new BlockInfo("camo_int_corner_slope_grade");
        for (int rise = maxRise; rise >= minCornerRise; rise--) {
            for (int riseL = maxRise; riseL >= minCornerRise; riseL--) {
                bInf.ConstructShape(ConfigLib.tabCornerSlopes, true, 0, 0, rise, riseL);
            }
        }
        bInf.flushValidValues(ConfigLib.tabCornerSlopes);
        bInf = new BlockInfo("camo_trench_slope_grade");
        for (int rise = maxRise; rise >= minCornerRise; rise--) {
            for (int riseB = maxRise; riseB >= minCornerRise; riseB--) {
                for (int riseL = maxRise; riseL >= minCornerRise; riseL--) {
                    bInf.ConstructShape(ConfigLib.tabCornerSlopes, true, 0, 0, rise, riseL, riseB);
                }
            }
        }
        bInf.flushValidValues(ConfigLib.tabCornerSlopes);
        net.minecraftforge.fml.common.ProgressManager.pop(bar);
    }

    private static class BlockInfo {
        private final String baseName;
        private String blockName; //init blockName with first known block name
        private List<String> validValues = new ArrayList<>();

        public BlockInfo(String baseName) {
            this.baseName = baseName;
            this.blockName =  this.baseName;
            bar.step(this.baseName);
        }

        //used to create single sloped blocks
        void constructSingleSlope(int rise, int shave, int base) {
            String currentName = getBaseName(false,Collections.singletonList(rise));
            if(shave > 0){
                currentName += "_shave" + (shave * 2);
            }
            if(base>0){
                currentName += "_base" + (base * 2);
            }
            AddSlopeToLists(currentName,  new SlopeShape(false,base,shave,rise),ConfigLib.tabSlopes);
        }
        void constructSingleShavingSlope(int rise, int shaving) {
            String currentName = getBaseName(false,Collections.singletonList(rise)) + "_shaving" + (shaving * 2);
            AddSlopeToLists(currentName,  new SlopeShape(false,-8+shaving,0,rise),ConfigLib.tabSlopes);
        }
        private String getBaseName(boolean isInternal, List<Integer> rises) {
            String currentName = (isInternal?"":"") + ConvertRisesToGrade(Lists.newArrayList(rises.get(0)));
            if (!blockName.substring(0, blockName.length() - 2).endsWith(currentName)) {
                if (validValues.size() == 0){
                    blockName = baseName + "|" + currentName + "_a";
                }else{
                    blockName += "|" + currentName + "_a";
                }
            }
            return (isInternal?"int":"") + ConvertRisesToGrade(Lists.newArrayList(rises));
        }

        private static String ConvertRisesToGrade(List<Integer> rises) {
            String ret = "";
            int lastRise;
            //if all 0 print the single 0
            for (lastRise = rises.size(); lastRise>1;lastRise--) {
                if(rises.get(lastRise-1) != 0){
                    break;
                }
            }
            for (int i = 0; i<lastRise;i++) {
                ret += "_" + String.valueOf((rises.get(i) / run) * 100).replace(".0","").replace(".", "_");
            }
            return ret.substring(1);
        }
        private void AddSlopeToLists(String currentName, SlopeShape shape, CreativeTabs tab) {
            Shapes.put(currentName,shape);
            validValues.add(currentName);
            if (validValues.size() == groupSize) {
                flushValidValues(tab);
                validValues = new ArrayList<>();//don't empty create new
                blockName = baseName + '|' + (shape.getIsInternal()?"int":"") + ConvertRisesToGrade(Lists.newArrayList(shape.getRise(0)))+'_' + (char)(blockName.charAt(blockName.length() - 1) + 1);
                bar.step(blockName);
            }
        }

        private void flushValidValues(CreativeTabs tab) {
            if (validValues.size()>0) {
                BlockCamoSlope block = new BlockCamoSlope(blockName, Material.CLOTH, validValues);
                block.setCreativeTab(tab);
                ConfigLib.CamoBlocks.add(block);

            }
        }

        public void ConstructShape(CreativeTabs tab,boolean isInternal, int base, int shave, Integer... rises) {
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < rises.length; i++) {
                l.add(rises[i]);
            }
            String currentName = getBaseName(isInternal,l);

            if (shave > 0) {
                currentName += "_shave" + (shave * 2);
            }
            if (base > 0) {
                currentName += "_base" + (base * 2);
            }
            AddSlopeToLists(currentName, new SlopeShape(isInternal, base, shave, rises), tab);
        }
    }
}
