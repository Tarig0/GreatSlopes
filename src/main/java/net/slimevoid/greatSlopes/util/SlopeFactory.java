package net.slimevoid.greatSlopes.util;

import net.minecraft.block.material.Material;
import net.minecraft.world.gen.structure.template.Template;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;

import java.util.*;

/**
 * Created by alcoo on 10/30/2016.
 */
public class SlopeFactory {
    static final int maxRise = 8; //100
    static final int minRise = 0; //12_5
    static final int maxHeight = 8;
    static final int groupSize = 16;
    static final int minShaving = 1;
    static final Double run = 8.0;
    public static final HashMap<String, SlopeShape> Shapes = new LinkedHashMap<String, SlopeShape>();

    public static void generate() {
        BlockInfo bInf = new BlockInfo();
        for (int rise = maxRise; rise >= minRise; rise--) {
            int shave = 0;
            int minBase = rise>0?0:1;
            do  {
                int base = minBase;
                do {
                    bInf.constructShape(rise, shave, base);
                    base++;
                }while(rise - shave + base <= maxHeight);
                shave++;
            }while (rise - shave > 0);
            int shaving = minShaving;
            while (shaving < rise){
                bInf.constructShape(rise, shaving);
                shaving++;
            }
        }
    }



    private static class BlockInfo {
        private static String baseName = "CamoSlopeGrade";
        private String blockName = baseName; //init blockName with first known block name
        private List<String> validValues = new ArrayList<String>();

        public void constructShape(int rise, int shave, int base) {
            String currentName = getBaseName(rise);
            if(shave > 0){
                currentName += "shave" + (shave * 2);
            }
            if(base>0){
                currentName += "base" + (base * 2);
            }
            AddToLists(currentName,  new SlopeShape(rise,shave,base));
        }
        public void constructShape(int rise, int shaving) {
            String currentName = getBaseName(rise) + "shaving" + (shaving * 2);
            AddToLists(currentName,  new SlopeShape(rise,shaving));
        }
        private String getBaseName(int rise) {
            String currentName = ConvertRiseToGrade(rise);
            if (!blockName.substring(0, blockName.length() - 1).endsWith(currentName)) {
                if (validValues.size() == 0){
                    blockName = baseName + currentName + 'A';
                }else{
                    blockName += currentName + 'A';
                }
            }
            return currentName;
        }

        private static String ConvertRiseToGrade(int rise) {
            return String.valueOf((rise / run) * 100).replace(".0","").replace(".", "_");
        }
        private void AddToLists(String currentName, SlopeShape shape) {
            Shapes.put(currentName,shape);
            validValues.add(currentName);
            if (validValues.size() == groupSize) {
                ConfigLib.CamoBlocks.add(new BlockCamoSlope(blockName, Material.CLOTH,validValues));
                validValues = new ArrayList<String>();//don't empty create new
                blockName = baseName + ConvertRiseToGrade(shape.getRise())+ (char)(blockName.charAt(blockName.length() - 1) + 1);
            }
        }
    }
}
