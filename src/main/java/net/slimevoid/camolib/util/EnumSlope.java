package net.slimevoid.camolib.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * This enum holds all the shapes that can be represented by none overlapping bounding boxes
 * Created by alcoo on 12/26/2015.
 */
public enum EnumSlope implements IStringSerializable {
    SLOPE100(8),
    SLOPE100SHAVE2(SLOPE100,0,7),
    SLOPE100SHAVE2BASE2(SLOPE100SHAVE2,1),
    SLOPE100SHAVE4(SLOPE100,0,6),
    SLOPE100SHAVE4BASE2(SLOPE100SHAVE4,1),
    SLOPE100SHAVE4BASE4(SLOPE100SHAVE4,2),
    SLOPE100SHAVE6(SLOPE100,0,5),
    SLOPE100SHAVE6BASE2(SLOPE100SHAVE6,1),
    SLOPE100SHAVE6BASE4(SLOPE100SHAVE6,2),
    SLOPE100SHAVE6BASE6(SLOPE100SHAVE6,3),
    SLOPE100SHAVE8(SLOPE100,0,4),
    SLOPE100SHAVE8BASE2(SLOPE100SHAVE8,1),
    SLOPE100SHAVE8BASE4(SLOPE100SHAVE8,2),
    SLOPE100SHAVE8BASE6(SLOPE100SHAVE8,3),
    SLOPE100SHAVE8BASE8(SLOPE100SHAVE8,4),
    SLOPE100SHAVE10(SLOPE100,0,3),
    SLOPE100SHAVE10BASE2(SLOPE100SHAVE10,1),
    SLOPE100SHAVE10BASE4(SLOPE100SHAVE10,2),
    SLOPE100SHAVE10BASE6(SLOPE100SHAVE10,3),
    SLOPE100SHAVE10BASE8(SLOPE100SHAVE10,4),
    SLOPE100SHAVE10BASE10(SLOPE100SHAVE10,5),
    SLOPE100SHAVE12(SLOPE100,0,2),
    SLOPE100SHAVE12BASE2(SLOPE100SHAVE12,1),
    SLOPE100SHAVE12BASE4(SLOPE100SHAVE12,2),
    SLOPE100SHAVE12BASE6(SLOPE100SHAVE12,3),
    SLOPE100SHAVE12BASE8(SLOPE100SHAVE12,4),
    SLOPE100SHAVE12BASE10(SLOPE100SHAVE12,5),
    SLOPE100SHAVE12BASE12(SLOPE100SHAVE12,6),
    SLOPE100SHAVE14(SLOPE100,0,1),
    SLOPE100SHAVE14BASE2(SLOPE100SHAVE14,1),
    SLOPE100SHAVE14BASE4(SLOPE100SHAVE14,2),
    SLOPE100SHAVE14BASE6(SLOPE100SHAVE14,3),
    SLOPE100SHAVE14BASE8(SLOPE100SHAVE14,4),
    SLOPE100SHAVE14BASE10(SLOPE100SHAVE14,5),
    SLOPE100SHAVE14BASE12(SLOPE100SHAVE14,6),
    SLOPE100SHAVE14BASE14(SLOPE100SHAVE14,7),
    SLOPE100SHAVING2(SLOPE100,7,1),
    SLOPE100SHAVING4(SLOPE100,6,2),
    SLOPE100SHAVING6(SLOPE100,5,3),
    SLOPE100SHAVING8(SLOPE100,4,4),
    SLOPE100SHAVING10(SLOPE100,3,5),
    SLOPE100SHAVING12(SLOPE100,2,6),
    SLOPE100SHAVING14(SLOPE100,1,7),
    //87.5
    SLOPE87_5(7),
    SLOPE87_5BASE2(SLOPE87_5,1),
    SLOPE87_5SHAVE2(SLOPE87_5,0,6),
    SLOPE87_5SHAVE2BASE2(SLOPE87_5SHAVE2,1),
    SLOPE87_5SHAVE2BASE4(SLOPE87_5SHAVE2,2),
    SLOPE87_5SHAVE4(SLOPE87_5,0,5),
    SLOPE87_5SHAVE4BASE2(SLOPE87_5SHAVE4,1),
    SLOPE87_5SHAVE4BASE4(SLOPE87_5SHAVE4,2),
    SLOPE87_5SHAVE4BASE6(SLOPE87_5SHAVE4,3),
    SLOPE87_5SHAVE6(SLOPE87_5,0,4),
    SLOPE87_5SHAVE6BASE2(SLOPE87_5SHAVE6,1),
    SLOPE87_5SHAVE6BASE4(SLOPE87_5SHAVE6,2),
    SLOPE87_5SHAVE6BASE6(SLOPE87_5SHAVE6,3),
    SLOPE87_5SHAVE6BASE8(SLOPE87_5SHAVE6,4),
    SLOPE87_5SHAVE8(SLOPE87_5,0,3),
    SLOPE87_5SHAVE8BASE2(SLOPE87_5SHAVE8,1),
    SLOPE87_5SHAVE8BASE4(SLOPE87_5SHAVE8,2),
    SLOPE87_5SHAVE8BASE6(SLOPE87_5SHAVE8,3),
    SLOPE87_5SHAVE8BASE8(SLOPE87_5SHAVE8,4),
    SLOPE87_5SHAVE8BASE10(SLOPE87_5SHAVE8,5),
    SLOPE87_5SHAVE10(SLOPE87_5,0,2),
    SLOPE87_5SHAVE10BASE2(SLOPE87_5SHAVE10,1),
    SLOPE87_5SHAVE10BASE4(SLOPE87_5SHAVE10,2),
    SLOPE87_5SHAVE10BASE6(SLOPE87_5SHAVE10,3),
    SLOPE87_5SHAVE10BASE8(SLOPE87_5SHAVE10,4),
    SLOPE87_5SHAVE10BASE10(SLOPE87_5SHAVE10,5),
    SLOPE87_5SHAVE10BASE12(SLOPE87_5SHAVE10,6),
    SLOPE87_5SHAVE12(SLOPE87_5,0,1),
    SLOPE87_5SHAVE12BASE2(SLOPE87_5SHAVE12,1),
    SLOPE87_5SHAVE12BASE4(SLOPE87_5SHAVE12,2),
    SLOPE87_5SHAVE12BASE6(SLOPE87_5SHAVE12,3),
    SLOPE87_5SHAVE12BASE8(SLOPE87_5SHAVE12,4),
    SLOPE87_5SHAVE12BASE10(SLOPE87_5SHAVE12,5),
    SLOPE87_5SHAVE12BASE12(SLOPE87_5SHAVE12,6),
    SLOPE87_5SHAVE12BASE14(SLOPE87_5SHAVE12,7),
    SLOPE87_5SHAVING2(SLOPE87_5,6,1),
    SLOPE87_5SHAVING4(SLOPE87_5,5,2),
    SLOPE87_5SHAVING6(SLOPE87_5,4,3),
    SLOPE87_5SHAVING8(SLOPE87_5,3,4),
    SLOPE87_5SHAVING10(SLOPE87_5,2,5),
    SLOPE87_5SHAVING12(SLOPE87_5,1,6),
    //75 Grade
    SLOPE75(6),
    SLOPE75BASE2(SLOPE75,1),
    SLOPE75BASE4(SLOPE75,2),
    SLOPE75SHAVE2(SLOPE75,0,5),
    SLOPE75SHAVE2BASE2(SLOPE75SHAVE2,1),
    SLOPE75SHAVE2BASE4(SLOPE75SHAVE2,2),
    SLOPE75SHAVE2BASE6(SLOPE75SHAVE2,3),
    SLOPE75SHAVE4(SLOPE75,0,4),
    SLOPE75SHAVE4BASE2(SLOPE75SHAVE4,1),
    SLOPE75SHAVE4BASE4(SLOPE75SHAVE4,2),
    SLOPE75SHAVE4BASE6(SLOPE75SHAVE4,3),
    SLOPE75SHAVE4BASE8(SLOPE75SHAVE4,4),
    SLOPE75SHAVE6(SLOPE75,0,3),
    SLOPE75SHAVE6BASE2(SLOPE75SHAVE6,1),
    SLOPE75SHAVE6BASE4(SLOPE75SHAVE6,2),
    SLOPE75SHAVE6BASE6(SLOPE75SHAVE6,3),
    SLOPE75SHAVE6BASE8(SLOPE75SHAVE6,4),
    SLOPE75SHAVE6BASE10(SLOPE75SHAVE6,5),
    SLOPE75SHAVE8(SLOPE75,0,2),
    SLOPE75SHAVE8BASE2(SLOPE75SHAVE8,1),
    SLOPE75SHAVE8BASE4(SLOPE75SHAVE8,2),
    SLOPE75SHAVE8BASE6(SLOPE75SHAVE8,3),
    SLOPE75SHAVE8BASE8(SLOPE75SHAVE8,4),
    SLOPE75SHAVE8BASE10(SLOPE75SHAVE8,5),
    SLOPE75SHAVE8BASE12(SLOPE75SHAVE8,6),
    SLOPE75SHAVE10(SLOPE75,0,1),
    SLOPE75SHAVE10BASE2(SLOPE75SHAVE10,1),
    SLOPE75SHAVE10BASE4(SLOPE75SHAVE10,2),
    SLOPE75SHAVE10BASE6(SLOPE75SHAVE10,3),
    SLOPE75SHAVE10BASE8(SLOPE75SHAVE10,4),
    SLOPE75SHAVE10BASE10(SLOPE75SHAVE10,5),
    SLOPE75SHAVE10BASE12(SLOPE75SHAVE10,6),
    SLOPE75SHAVE10BASE14(SLOPE75SHAVE10,7),
    SLOPE75SHAVING2(SLOPE75,5,1),
    SLOPE75SHAVING4(SLOPE75,4,2),
    SLOPE75SHAVING6(SLOPE75,3,3),
    SLOPE75SHAVING8(SLOPE75,2,4),
    SLOPE75SHAVING10(SLOPE75,1,5),
    //62.5 Grade
    SLOPE62_5(5),
    SLOPE62_5BASE2(SLOPE62_5,1),
    SLOPE62_5BASE4(SLOPE62_5,2),
    SLOPE62_5BASE6(SLOPE62_5,3),
    SLOPE62_5SHAVE2(SLOPE62_5,0,4),
    SLOPE62_5SHAVE2BASE2(SLOPE62_5SHAVE2,1),
    SLOPE62_5SHAVE2BASE4(SLOPE62_5SHAVE2,2),
    SLOPE62_5SHAVE2BASE6(SLOPE62_5SHAVE2,3),
    SLOPE62_5SHAVE2BASE8(SLOPE62_5SHAVE2,4),
    SLOPE62_5SHAVE4(SLOPE62_5,0,3),
    SLOPE62_5SHAVE4BASE2(SLOPE62_5SHAVE4,1),
    SLOPE62_5SHAVE4BASE4(SLOPE62_5SHAVE4,2),
    SLOPE62_5SHAVE4BASE6(SLOPE62_5SHAVE4,3),
    SLOPE62_5SHAVE4BASE8(SLOPE62_5SHAVE4,4),
    SLOPE62_5SHAVE4BASE10(SLOPE62_5SHAVE4,5),
    SLOPE62_5SHAVE6(SLOPE62_5,0,2),
    SLOPE62_5SHAVE6BASE2(SLOPE62_5SHAVE6,1),
    SLOPE62_5SHAVE6BASE4(SLOPE62_5SHAVE6,2),
    SLOPE62_5SHAVE6BASE6(SLOPE62_5SHAVE6,3),
    SLOPE62_5SHAVE6BASE8(SLOPE62_5SHAVE6,4),
    SLOPE62_5SHAVE6BASE10(SLOPE62_5SHAVE6,5),
    SLOPE62_5SHAVE6BASE12(SLOPE62_5SHAVE6,6),
    SLOPE62_5SHAVE8(SLOPE62_5,0,1),
    SLOPE62_5SHAVE8BASE2(SLOPE62_5SHAVE8,1),
    SLOPE62_5SHAVE8BASE4(SLOPE62_5SHAVE8,2),
    SLOPE62_5SHAVE8BASE6(SLOPE62_5SHAVE8,3),
    SLOPE62_5SHAVE8BASE8(SLOPE62_5SHAVE8,4),
    SLOPE62_5SHAVE8BASE10(SLOPE62_5SHAVE8,5),
    SLOPE62_5SHAVE8BASE12(SLOPE62_5SHAVE8,6),
    SLOPE62_5SHAVE8BASE14(SLOPE62_5SHAVE8,7),
    SLOPE62_5SHAVING2(SLOPE62_5,4,1),
    SLOPE62_5SHAVING4(SLOPE62_5,3,2),
    SLOPE62_5SHAVING6(SLOPE62_5,2,3),
    SLOPE62_5SHAVING8(SLOPE62_5,1,4),
    //50 Grade
    SLOPE50(4),
    SLOPE50BASE2(SLOPE50,1),
    SLOPE50BASE4(SLOPE50,2),
    SLOPE50BASE6(SLOPE50,3),
    SLOPE50BASE8(SLOPE50,4),
    SLOPE50SHAVE2(SLOPE50,0,3),
    SLOPE50SHAVE2BASE2(SLOPE50SHAVE2,1),
    SLOPE50SHAVE2BASE4(SLOPE50SHAVE2,2),
    SLOPE50SHAVE2BASE6(SLOPE50SHAVE2,3),
    SLOPE50SHAVE2BASE8(SLOPE50SHAVE2,4),
    SLOPE50SHAVE2BASE10(SLOPE50SHAVE2,5),
    SLOPE50SHAVE4(SLOPE50,0,2),
    SLOPE50SHAVE4BASE2(SLOPE50SHAVE4,1),
    SLOPE50SHAVE4BASE4(SLOPE50SHAVE4,2),
    SLOPE50SHAVE4BASE6(SLOPE50SHAVE4,3),
    SLOPE50SHAVE4BASE8(SLOPE50SHAVE4,4),
    SLOPE50SHAVE4BASE10(SLOPE50SHAVE4,5),
    SLOPE50SHAVE4BASE12(SLOPE50SHAVE4,6),
    SLOPE50SHAVE6(SLOPE50,0,1),
    SLOPE50SHAVE6BASE2(SLOPE50SHAVE6,1),
    SLOPE50SHAVE6BASE4(SLOPE50SHAVE6,2),
    SLOPE50SHAVE6BASE6(SLOPE50SHAVE6,3),
    SLOPE50SHAVE6BASE8(SLOPE50SHAVE6,4),
    SLOPE50SHAVE6BASE10(SLOPE50SHAVE6,5),
    SLOPE50SHAVE6BASE12(SLOPE50SHAVE6,6),
    SLOPE50SHAVE6BASE14(SLOPE50SHAVE6,7),
    SLOPE50SHAVING2(SLOPE50,3,1),
    SLOPE50SHAVING4(SLOPE50,2,2),
    SLOPE50SHAVING6(SLOPE50,1,3),
    //37.5
    SLOPE37_5(3),
    SLOPE37_5BASE2(SLOPE37_5,1),
    SLOPE37_5BASE4(SLOPE37_5,2),
    SLOPE37_5BASE6(SLOPE37_5,3),
    SLOPE37_5BASE8(SLOPE37_5,4),
    SLOPE37_5BASE10(SLOPE37_5,5),
    SLOPE37_5SHAVE2(SLOPE37_5,0,2),
    SLOPE37_5SHAVE2BASE2(SLOPE37_5SHAVE2,1),
    SLOPE37_5SHAVE2BASE4(SLOPE37_5SHAVE2,2),
    SLOPE37_5SHAVE2BASE6(SLOPE37_5SHAVE2,3),
    SLOPE37_5SHAVE2BASE8(SLOPE37_5SHAVE2,4),
    SLOPE37_5SHAVE2BASE10(SLOPE37_5SHAVE2,5),
    SLOPE37_5SHAVE2BASE12(SLOPE37_5SHAVE2,6),
    SLOPE37_5SHAVE4(SLOPE37_5,0,1),
    SLOPE37_5SHAVE4BASE2(SLOPE37_5SHAVE4,1),
    SLOPE37_5SHAVE4BASE4(SLOPE37_5SHAVE4,2),
    SLOPE37_5SHAVE4BASE6(SLOPE37_5SHAVE4,3),
    SLOPE37_5SHAVE4BASE8(SLOPE37_5SHAVE4,4),
    SLOPE37_5SHAVE4BASE10(SLOPE37_5SHAVE4,5),
    SLOPE37_5SHAVE4BASE12(SLOPE37_5SHAVE4,6),
    SLOPE37_5SHAVE4BASE14(SLOPE37_5SHAVE4,7),
    SLOPE37_5SHAVING2(SLOPE37_5,2,1),
    SLOPE37_5SHAVING4(SLOPE37_5,1,2),
    //25 grade
    SLOPE25(2),
    SLOPE25BASE2(SLOPE25,1),
    SLOPE25BASE4(SLOPE25,2),
    SLOPE25BASE6(SLOPE25,3),
    SLOPE25BASE8(SLOPE25,4),
    SLOPE25BASE10(SLOPE25,5),
    SLOPE25BASE12(SLOPE25,6),
    SLOPE25SHAVE2(SLOPE25,0,1),
    SLOPE25SHAVE2BASE2(SLOPE25SHAVE2,1),
    SLOPE25SHAVE2BASE4(SLOPE25SHAVE2,2),
    SLOPE25SHAVE2BASE6(SLOPE25SHAVE2,3),
    SLOPE25SHAVE2BASE8(SLOPE25SHAVE2,4),
    SLOPE25SHAVE2BASE10(SLOPE25SHAVE2,5),
    SLOPE25SHAVE2BASE12(SLOPE25SHAVE2,6),
    SLOPE25SHAVE2BASE14(SLOPE25SHAVE2,7),
    SLOPE25SHAVING2(SLOPE25,1,1),
    //12.5 grade
    SLOPE12_5(1),
    SLOPE12_5BASE2(SLOPE12_5,1),
    SLOPE12_5BASE4(SLOPE12_5,2),
    SLOPE12_5BASE6(SLOPE12_5,3),
    SLOPE12_5BASE8(SLOPE12_5,4),
    SLOPE12_5BASE10(SLOPE12_5,5),
    SLOPE12_5BASE12(SLOPE12_5,6),
    SLOPE12_5BASE14(SLOPE12_5,7),
    //0 grade AKA slab
    SLOPE0BASE2(1,0,0,0),
    SLOPE0BASE4(SLOPE0BASE2,2),
    SLOPE0BASE6(SLOPE0BASE2,3),
    SLOPE0BASE8(SLOPE0BASE2,4),
    SLOPE0BASE10(SLOPE0BASE2,5),
    SLOPE0BASE12(SLOPE0BASE2,6),
    SLOPE0BASE14(SLOPE0BASE2,7),
    SLOPE0BASE16(SLOPE0BASE2,8); //full block

    private final String name;
    private final int slopeBase;
    private final int deltaBound;
    private final int slopeBoundCount;
    private final int iOffset;

    EnumSlope(int deltaBound) {
        this(0,deltaBound,0,-1);
    }
    EnumSlope(EnumSlope parentSlope, int slopeBase) {
        this(slopeBase,parentSlope.deltaBound,parentSlope.iOffset,Math.min(parentSlope.deltaBound - Math.max(-8+parentSlope.deltaBound+slopeBase,0),parentSlope.slopeBoundCount));
    }
    EnumSlope(EnumSlope parentSlope, int iOffset, int slopeBoundCount) {
        this(parentSlope.slopeBase,parentSlope.deltaBound,iOffset,slopeBoundCount);
    }
    EnumSlope(int slopeBase, int deltaBound, int iOffset,int slopeBoundCount) {
        String tempName = String.valueOf((deltaBound/8D * 100)).replace(".0","").replace(".","_");

        if(iOffset != 0){
            tempName += "shaving" + String.valueOf(slopeBoundCount * 2);
        }else if(slopeBoundCount > 0 && (deltaBound-slopeBoundCount) > 0){
            tempName += "shave" + String.valueOf((deltaBound-slopeBoundCount) * 2);
        }
        if (slopeBase > 0){
            tempName += "base" + String.valueOf(slopeBase * 2);
        }
        if(slopeBoundCount == -1){
            slopeBoundCount = deltaBound - Math.max(-8+deltaBound+slopeBase,0);
        }
        this.name = tempName;

        this.slopeBase = slopeBase;
        this.deltaBound = deltaBound;
        this.iOffset=iOffset;
        this.slopeBoundCount = slopeBoundCount;
    }

    public static ArrayList<EnumSlope> getSection(int i) {
        int start = i*16;
        int end = Math.min(start + 16,EnumSlope.values().length);
        ArrayList<EnumSlope> ret = new ArrayList<EnumSlope>();
        for(int x = start;x < end; x++){
            ret.add(EnumSlope.values()[x]);
        }
        return ret;
    }
    public static String[] getSectionNames(){
        String[] ret = new String[(int)Math.ceil(EnumSlope.values().length/16d)];
        for(int i=0;i<ret.length;i++){
            ret[i] = getSectionName(i);
        }
        return ret;
    }
    public static String getSectionName(int i){
        String ret = "CamoSlopeGrade";
        int start = i*16;
        char suffix= 'a';
        String currentName = EnumSlope.values()[start].getName();
        if(!NumberUtils.isNumber(currentName.replace('_','.'))){
            suffix++;
            int firstNonNum = currentName.indexOf('s');
            int secondary = currentName.indexOf('b');
            if (firstNonNum == -1)firstNonNum = secondary;
                    else if (secondary > -1 && firstNonNum > -1) firstNonNum = Math.min(firstNonNum, secondary);
            currentName = currentName.substring(0,firstNonNum);
            if (start >= 32){
                int prefixSearch = start - 32;
                while(prefixSearch >= 0 && EnumSlope.values()[prefixSearch].getName().startsWith(currentName)){
                    suffix++;
                    prefixSearch -= 16;
                }
                if(prefixSearch >=0 && EnumSlope.values()[prefixSearch + 15].getName().startsWith(currentName)) suffix++;
            }
        }
        ret += currentName + suffix;
        int end = Math.min(start + 15,EnumSlope.values().length-1);
        if (!EnumSlope.values()[end].getName().startsWith(currentName)) {
            for (int x = start + 1; x <= end; x++) {
                if (NumberUtils.isNumber(EnumSlope.values()[x].getName().replace('_', '.'))) {
                    ret += EnumSlope.values()[x].getName() + 'a';
                }
            }
        }
        return ret;
    }

    public String toString() {
        return this.name;
    }
    @Nonnull
    public String getName() {
        return this.name;
    }

    public Integer getBoundingCount() {
        return this.slopeBoundCount;
    }

    public AxisAlignedBB getSlopedBounding(Integer i, BlockPos pos, EnumDirectionQuatrent direction) {

        double x1 = pos.getX();
        double x2 = pos.getX() + 1;
        double y1 = pos.getY();
        double y2 = pos.getY() + 1;
        double z1 = pos.getZ();
        double z2 = pos.getZ() + 1;

        double minS =(i + this.slopeBase) / 8D;
        double maxS = minS + .125;
        switch(direction.getAnchor()){
            case WEST:
                x1 = pos.getX() + minS;
                x2 = pos.getX() + maxS;
                break;
            case EAST:
                x1 = pos.getX() + (1 - maxS);
                x2 = pos.getX() + (1 - minS);
                break;
            case DOWN:
                y1 = pos.getY() + minS;
                y2 = pos.getY() + maxS;
                break;
            case UP:
                y1 = pos.getY() + (1 - maxS);
                y2 = pos.getY() + (1 - minS);
                break;
            case NORTH:
                z1 = pos.getZ() + minS;
                z2 = pos.getZ() + maxS;
                break;
            case SOUTH:
                z1 = pos.getZ() + (1 - maxS);
                z2 = pos.getZ() + (1 - minS);
                break;
        }
        i+=this.iOffset;
        double threshold = (double)i / this.deltaBound;
        switch (direction.getFacing()){
            case WEST:
                x1 = pos.getX() + threshold;
                break;
            case EAST:
                x2= pos.getX() + (1 - threshold);
                break;
            case DOWN:
                y1 = pos.getY() + threshold;
                break;
            case UP:
                y2= pos.getY() + (1 - threshold);
                break;
            case NORTH:
                z1 = pos.getZ() + threshold;
                break;
            case SOUTH:
                z2 = pos.getZ() + (1 - threshold);
                break;
        }
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    //Only used for any slopes that sit on top of a box or in other words a slopeBase > 0
    public AxisAlignedBB getBaseBounding(BlockPos pos, EnumFacing anchor) {
        if (slopeBase > 0) {
            double sLowBound = slopeBase/8d;
            double x1 = pos.getX();
            double x2 = pos.getX() + 1;
            double y1 = pos.getY();
            double y2 = pos.getY() + 1;
            double z1 = pos.getZ();
            double z2 = pos.getZ() + 1;
            switch (anchor) {
                case WEST:
                    x2 = pos.getX() + sLowBound;
                    break;
                case EAST:
                    x1 = pos.getX() + (1-sLowBound);
                    break;
                case DOWN:
                    y2 = pos.getY() + sLowBound;
                    break;
                case UP:
                    y1 = pos.getY() + (1 - sLowBound);
                    break;
                case NORTH:
                    z2 = pos.getZ() + sLowBound;
                    break;
                case SOUTH:
                    z1 = pos.getZ() + (1 - sLowBound);
                    break;
            }
            return new AxisAlignedBB(x1,y1,z1,x2,y2,z2);
        }
        return null;
    }

    public boolean isSideSolid(EnumFacing placeSide,EnumDirectionQuatrent facing){
        if(facing.getAnchor() == placeSide){
            return true;
        }else if(placeSide == EnumFacing.UP && deltaBound ==0&& slopeBase==16){
            return true;
        }else{
            if(iOffset == 0 && deltaBound + slopeBase >= 8){
                return placeSide == facing.getFacing().getOpposite();
            }
            return false;
        }
    }
}
