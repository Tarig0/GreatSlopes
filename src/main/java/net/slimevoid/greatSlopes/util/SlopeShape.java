package net.slimevoid.greatSlopes.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * Created by alcoo on 10/30/2016.
 *
 */
public class SlopeShape {
    private final int Rise;
    private final int Base;
    private final int Shave;

    SlopeShape(int rise, int shave, int base) {
        //need to finish constructor
        Rise = rise;
        Shave = shave;
        Base = base;
    }

    SlopeShape(int rise, int shaving) {
        this(rise,0,0-rise+shaving);
        //need to finish constructor
    }

    public AxisAlignedBB getSlopedBounding(Integer i, BlockPos pos, EnumDirectionQuatrent direction) {

        double x1 = pos.getX();
        double x2 = pos.getX() + 1;
        double y1 = pos.getY();
        double y2 = pos.getY() + 1;
        double z1 = pos.getZ();
        double z2 = pos.getZ() + 1;

        double minS =(i + Math.max(this.Base,0)) / 8D;
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
        if(Base < 0) i-=this.Base;
        double threshold = (double)i / this.Rise;
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
        if (Base > 0) {
            double sLowBound = Base/8d;
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

    public boolean isSideSolid(EnumFacing placeSide, EnumDirectionQuatrent facing){
        if(facing.getAnchor() == placeSide){
            return true;
        }else if(placeSide == EnumFacing.UP && Rise == 0 && Base==8){
            return true;
        }else{
            if(Rise + Base >= 8){
                return placeSide == facing.getFacing().getOpposite();
            }
            return false;
        }
    }

    public int getBoundingCount() {
       return Base<0?Rise+Base:(Rise - Shave);
    }

    int getRise() {
        return Rise;
    }

    public AxisAlignedBB getBoundingBox(EnumDirectionQuatrent direction) {
        double x1 = 0;
        double x2 = 1;
        double y1 = 0;
        double y2 = 1;
        double z1 = 0;
        double z2 = 1;
        int height = getBoundingCount();
        if(Base<0){
            double threshold = (double)(0-this.Base) / this.Rise;
            switch (direction.getFacing()){
                case WEST:
                    x1 = threshold;
                    break;
                case EAST:
                    x2= (1 - threshold);
                    break;
                case DOWN:
                    y1 =  threshold;
                    break;
                case UP:
                    y2= (1 - threshold);
                    break;
                case NORTH:
                    z1 =  threshold;
                    break;
                case SOUTH:
                    z2 =  (1 - threshold);
                    break;
            }
        }else{
            height+=Base;
        }
        switch (direction.getAnchor()) {
            case WEST:
                x2 = height/8f;
                break;
            case EAST:
                x1 =1-(height/8f);
            break;
            case DOWN:
                y2 = height/8f;
            break;
            case UP:
                y1 = 1-(height/8f);
            break;
            case NORTH:
                z2 = (height/8f);
            break;
            case SOUTH:
                z1 = 1-(height/8f);
            break;
        }

        return new AxisAlignedBB(x1,y1,z1,x2,y2,z2);
    }
}
