package net.slimevoid.camolib.util;

import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

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
    //75 Grade
    SLOPE75(6),
    SLOPE75BASE2(SLOPE75,2),
    SLOPE75BASE4(SLOPE75,4),
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
    SLOPE25SHAVING2(SLOPE25,3,1),
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
    SLOPE0BASE2("0base2",1,0,0,0),
    SLOPE0BASE4(SLOPE0BASE2,2),
    SLOPE0BASE6(SLOPE0BASE2,3),
    SLOPE0BASE8(SLOPE0BASE2,4),
    SLOPE0BASE10(SLOPE0BASE2,5),
    SLOPE0BASE12(SLOPE0BASE2,6),
    SLOPE0BASE14(SLOPE0BASE2,7);

    private final String name;
    private final int slopeBase;
    private final int deltaBound;
    private final int slopeBoundCount;
    private final int iOffset;

    EnumSlope(int deltaBound) {
        this(String.valueOf((deltaBound/8D * 100)).replace(".0","").replace(".","_"),  0,  deltaBound);
    }
    EnumSlope(EnumSlope parentSlope, int slopeBase) {
        this(parentSlope.getName()+"base"+String.valueOf(slopeBase * 2),slopeBase,parentSlope.deltaBound,parentSlope.iOffset,Math.min(parentSlope.deltaBound - Math.max(-8+parentSlope.deltaBound+slopeBase,0),parentSlope.slopeBoundCount));
    }
    EnumSlope(String name, int slopeBase, int deltaBound) {
        this(name,slopeBase,deltaBound,0,deltaBound - Math.max(-8+deltaBound+slopeBase,0));
    }

    EnumSlope(EnumSlope parentSlope, int iOffset, int slopeBoundCount) {
        this(parentSlope.getName()+(iOffset==0?"shave"+String.valueOf(((parentSlope.deltaBound)-slopeBoundCount)*2):"shaving"+String.valueOf(slopeBoundCount*2)),parentSlope.slopeBase,parentSlope.deltaBound,iOffset,slopeBoundCount);
    }
    EnumSlope(String name, int slopeBase, int deltaBound, int iOffset,int slopeBoundCount) {
        this.name = name;
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
        suffix = 'a';
        int end = Math.min(start + 16,EnumSlope.values().length-1);
        if (!EnumSlope.values()[end].getName().startsWith(currentName)) {
            for (int x = start + 1; x <= end; x++) {
                if (NumberUtils.isNumber(EnumSlope.values()[x].getName().replace('_', '.'))) {
                    ret += EnumSlope.values()[x].getName() + suffix;
                }
            }
        }
        return ret;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public Integer getBoundingCount() {
        return this.slopeBoundCount;
    }

    public AxisAlignedBB getSlopedBounding(Integer i, BlockPos pos, EnumDirectionQuatrent direction) {

        double minS =(i + this.slopeBase) / 8D;
        double maxS = minS + .125;
        i+=this.iOffset;
        double threshold = (double)i / this.deltaBound;
        //TODO use the following to make less lines of code
        double x1 = pos.getX();
        double x2 = pos.getX() + 1;
        double y1 = pos.getY();
        double y2 = pos.getY() + 1;
        double z1 = pos.getZ();
        double z2 = pos.getZ() + 1;
        switch (direction) {
            case DOWNNORTH:
                return new AxisAlignedBB(pos.getX(), pos.getY() + minS, pos.getZ() + threshold, pos.getX() + 1, pos.getY() + maxS, pos.getZ() + 1);
            case DOWNSOUTH:
                return new AxisAlignedBB(pos.getX(), pos.getY() + minS, pos.getZ(), pos.getX() + 1, pos.getY() + maxS, pos.getZ() + (1 - threshold));
            case DOWNEAST:
                return new AxisAlignedBB(pos.getX(), pos.getY() + minS, pos.getZ(), pos.getX() + (1 - threshold), pos.getY() + maxS, pos.getZ() + 1);
            case DOWNWEST:
                return new AxisAlignedBB(pos.getX() + threshold, pos.getY() + minS, pos.getZ(), pos.getX() + 1, pos.getY() + maxS, pos.getZ() + 1);
            case UPNORTH:
                return new AxisAlignedBB(pos.getX(), pos.getY() + (1 - maxS), pos.getZ() + threshold, pos.getX() + 1, pos.getY() + (1 - minS), pos.getZ() + 1);
            case UPSOUTH:
                return new AxisAlignedBB(pos.getX(), pos.getY() + (1 - maxS), pos.getZ(), pos.getX() + 1, pos.getY() + (1 - minS), pos.getZ() + (1 - threshold));
            case UPEAST:
                return new AxisAlignedBB(pos.getX(), pos.getY() + (1 - maxS), pos.getZ(), pos.getX() + (1 - threshold), pos.getY() + (1 - minS), pos.getZ() + 1);
            case UPWEST:
                return new AxisAlignedBB(pos.getX() + threshold, pos.getY() + (1 - maxS), pos.getZ(), pos.getX() + 1, pos.getY() + (1 - minS), pos.getZ() + 1);
            case NORTHDOWN:
                return new AxisAlignedBB(pos.getX(), pos.getY() + threshold, pos.getZ() + minS, pos.getX() + 1, pos.getY() + 1, pos.getZ() + maxS);
            case NORTHEAST:
                return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + minS, pos.getX() + (1 - threshold), pos.getY() + 1, pos.getZ() + maxS);
            case NORTHUP:
                return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + minS, pos.getX() + 1, pos.getY() + (1 - threshold), pos.getZ() + maxS);
            case NORTHWEST:
                return new AxisAlignedBB(pos.getX() + threshold, pos.getY(), pos.getZ() + minS, pos.getX() + 1, pos.getY() + 1, pos.getZ() + maxS);
            case SOUTHDOWN:
                return new AxisAlignedBB(pos.getX(), pos.getY() + threshold, pos.getZ() + (1 - maxS), pos.getX() + 1, pos.getY() + 1, pos.getZ() + (1 - minS));
            case SOUTHWEST:
                return new AxisAlignedBB(pos.getX() + threshold, pos.getY(), pos.getZ() + (1 - maxS), pos.getX() + 1, pos.getY() + 1, pos.getZ() + (1 - minS));
            case SOUTHUP:
                return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + (1 - maxS), pos.getX() + 1, pos.getY() + (1 - threshold), pos.getZ() + (1 - minS));
            case SOUTHEAST:
                return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + (1 - maxS), pos.getX() + (1 - threshold), pos.getY() + 1, pos.getZ() + (1 - minS));
            case WESTDOWN:
                return new AxisAlignedBB(pos.getX() + minS, pos.getY() + threshold, pos.getZ(), pos.getX() + maxS, pos.getY() + 1, pos.getZ() + 1);
            case WESTNORTH:
                return new AxisAlignedBB(pos.getX() + minS, pos.getY(), pos.getZ() + threshold, pos.getX() + maxS, pos.getY() + 1, pos.getZ() + 1);
            case WESTUP:
                return new AxisAlignedBB(pos.getX() + minS, pos.getY(), pos.getZ(), pos.getX() + maxS, pos.getY() + (1 - threshold), pos.getZ() + 1);
            case WESTSOUTH:
                return new AxisAlignedBB(pos.getX() + minS, pos.getY(), pos.getZ(), pos.getX() + maxS, pos.getY() + 1, pos.getZ() + (1 - threshold));
            case EASTDOWN:
                return new AxisAlignedBB(pos.getX() + (1 - maxS), pos.getY() + threshold, pos.getZ(), pos.getX() + (1 - minS), pos.getY() + 1, pos.getZ() + 1);
            case EASTSOUTH:
                return new AxisAlignedBB(pos.getX() + (1 - maxS), pos.getY(), pos.getZ(), pos.getX() + (1 - minS), pos.getY() + 1, pos.getZ() + (1 - threshold));
            case EASTUP:
                return new AxisAlignedBB(pos.getX() + (1 - maxS), pos.getY(), pos.getZ(), pos.getX() + (1 - minS), pos.getY() + (1 - threshold), pos.getZ() + 1);
            case EASTNORTH:
                return new AxisAlignedBB(pos.getX() + (1 - maxS), pos.getY(), pos.getZ() + threshold, pos.getX() + (1 - minS), pos.getY() + 1, pos.getZ() + 1);
        }
        return new AxisAlignedBB(threshold + pos.getX(), pos.getY() + minS, pos.getZ(), pos.getX() + 1, pos.getY() + maxS, pos.getZ() + 1);
    }

    //Only used for any slopes that sit on top of a box or in other words a slopeBase > 0
    public AxisAlignedBB getBaseBounding(BlockPos pos, EnumFacing anchor) {
        if (slopeBase > 0) {
            double sLowBound = slopeBase/8d;
            switch (anchor) {
                case DOWN:
                    return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + sLowBound, pos.getZ() + 1);
                case UP:
                    return new AxisAlignedBB(pos.getX(), pos.getY() + (1 - sLowBound), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                case NORTH:
                    return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + sLowBound);
                case SOUTH:
                    return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + (1 - sLowBound), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                case WEST:
                    return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + sLowBound, pos.getY() + 1, pos.getZ() + 1);
                case EAST:
                    return new AxisAlignedBB(pos.getX() + (1-sLowBound), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            }
        }
        return null;
    }
}
