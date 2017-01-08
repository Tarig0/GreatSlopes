package net.slimevoid.greatSlopes.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by alcoo on 12/20/2016.
 *
 */
public class SlopeShape {
    private static final double GRADIENT_STEP = 0.125; //1/8
    //Holds the plans in an array based on the enumfacing.axis ordinal
    private static final EnumFacing[][] PLANES = {
            {EnumFacing.NORTH,EnumFacing.UP,EnumFacing.SOUTH,EnumFacing.DOWN},//x
            EnumFacing.HORIZONTALS,//y
            {EnumFacing.DOWN,EnumFacing.EAST,EnumFacing.UP,EnumFacing.WEST}//z
    };
    //holds the slope rises front, left, back, right
    //Rises should be between 0-8;
    private final ImmutableList<Integer> rises;
    private final int base;
    private final int shave;
    //Wether or not the slope, if has more than one slope
    //should create one hit box or multiple
    private final boolean isInternal;
    private final int boundingCount;


//TODO:: generate pyrimades {1,1,1,1}
    //This should cover all shapes with slices crossing an axis
    SlopeShape(Integer... rises){
        this(false,0,0,rises);
    }

    SlopeShape(boolean isInternal,int base,int shave, Integer... rises){
        this.isInternal = isInternal;
        this.base = base;
        this.shave = shave;
        int trimmedLength = rises.length;
        for (int i = rises.length-1;i >= 0 && rises[i]==0;i--){
            trimmedLength = i;
        }
        //only store the elements that we care about
        this.rises = ImmutableList.copyOf(Arrays.copyOf(rises,trimmedLength));
        //cache the max slope bounding count
        if(trimmedLength>0) {
            if(this.isInternal){
                this.boundingCount = Collections.max(this.rises);
            }else {
                int bc = 8;
                for (int r : this.rises) {
                    if (r > 0 && r < bc) {
                        bc = r;
                    }
                }
                this.boundingCount = bc;
            }
        }
        else
            this.boundingCount = 0;
    }

    public int getBoundingCount() {
        return this.boundingCount + (this.base<0?this.base:-1*this.shave);
    }

    private EnumFacing getSlopeFacing(EnumDirectionQuadrant direction, int offset) {
        if (offset == 0)
            return direction.getFacing(); //just return the facing
        else
            return PLANES[direction.getAnchor().getAxis().ordinal()][(direction.getQuadOridinal() + offset)%4];
    }
    public AxisAlignedBB getBaseBounding(BlockPos pos, EnumFacing anchor) {
        if (base > 0) {
            double coords[] = {0,1,0,1,0,1};
            ApplyThreshold(coords,1-(base*GRADIENT_STEP),anchor.getOpposite());
            return new AxisAlignedBB(coords[0],coords[2],coords[4],coords[1],coords[3],coords[5]).offset(pos);
        }
        return null;
    }
    //returns a list of slope hit boxes found at the current slice
    public List<AxisAlignedBB> getSlopedBounding(Integer i, BlockPos pos, EnumDirectionQuadrant direction) {
        double coords[] = {0,1,0,1,0,1};
        //Create the base to apex line
        double minS =i * GRADIENT_STEP;
        if(this.base>0){
            minS += this.base * GRADIENT_STEP;
        }
        ApplyThreshold(coords,minS + GRADIENT_STEP,direction.getAnchor());
        //apply the opposite threshold
        int baseOrdinal= direction.getAnchor().getAxis().ordinal()*2;
        switch(direction.getAnchor().getAxisDirection()){
            case POSITIVE:
                coords[baseOrdinal] = (1 - minS);
                break;
            case NEGATIVE:
                coords[baseOrdinal + 1] = minS;
                break;
        }
        List<AxisAlignedBB> ret = new ArrayList<>();
        if(base < 0) i-=this.base;
        for(int j=0;j<this.rises.size();j++){
            double threshold = i / (double)this.rises.get(j);
            if(this.isInternal){
                //create a box and add it
                double tempCoords[] = Arrays.copyOf(coords,6);
                ApplyThreshold(tempCoords,threshold, getSlopeFacing(direction,j));
                ret.add(new AxisAlignedBB(tempCoords[0],tempCoords[2],tempCoords[4],tempCoords[1],tempCoords[3],tempCoords[5]).offset(pos));
            }else{
                //constrain the one box
                ApplyThreshold(coords,threshold, getSlopeFacing(direction,j));
            }
        }
        if(!this.isInternal){
            ret.add(new AxisAlignedBB(coords[0],coords[2],coords[4],coords[1],coords[3],coords[5]).offset(pos));
        }
        return ret;
    }
    //used to draw a box around the block when highlighted
    public AxisAlignedBB getBoundingBox(EnumDirectionQuadrant direction) {
        double coords[] = {0,1,0,1,0,1};
        double height = getBoundingCount();
        if(this.base<0){
            ApplyThreshold(coords,this.base*-1*GRADIENT_STEP,direction.getFacing());
        }else{
            height += this.base;
        }
        ApplyThreshold(coords,1 -(height*GRADIENT_STEP),direction.getAnchor().getOpposite());

        return new AxisAlignedBB(coords[0],coords[2],coords[4],coords[1],coords[3],coords[5]);
    }

    public boolean isSideSolid(EnumFacing placeSide, EnumDirectionQuadrant facing) {
        if(facing.getAnchor() == placeSide){
            return true;
        }else if(!this.isInternal && facing.getFacing().getOpposite() == placeSide){
            return true;
        }else{
            //TODO::Add condition for the right hand slope
            return false;
        }
    }

    private void ApplyThreshold(double[] coords, double threshold, EnumFacing slopeFacing) {
        int index = slopeFacing.getAxis().ordinal()*2;
        switch (slopeFacing.getAxisDirection()){
            case NEGATIVE:
                coords[index] = threshold;
                break;
            case POSITIVE:
                coords[index + 1] = (1 - threshold);
                break;
        }
    }


    public Integer getRise(int i) {
        return this.rises.size() > i? this.rises.get(i):0;
    }

    public int getBase() {
        return base;
    }

    public List<EnumFacing> getSlopeFacings(EnumDirectionQuadrant dQuad) {
        List<EnumFacing> ret = new ArrayList<>();
        for(int i=0;i<this.rises.size();i++){
            if (this.getRise(i)>0){
                ret.add(this.getSlopeFacing(dQuad,i));
            }
        }
        return ret;
    }

    public EnumFacing getActualSide(EnumDirectionQuadrant dQuad, EnumFacing facing,double hitX,double hitY,double hitZ) {
        if (this.getSlopeFacings(dQuad).contains(facing)) {
            EnumFacing apex = dQuad.getAnchor().getOpposite();
            double d;
            if (apex.getAxis() == EnumFacing.Axis.Y) {
                d = hitY;
            } else if (apex.getAxis() == EnumFacing.Axis.X) {
                d = hitX;
            } else {
                d = hitZ;
            }
            float f1 = (float) d * 8f;
            if (apex.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) f1 = 8 - f1;
            if (f1 > this.getBase()) {
               return apex;
            }
        }
        return facing;
    }

    public ImmutableList<Integer> getRises() {
        if(rises.size()>0) {
            return rises;
        } else {
            return ImmutableList.copyOf(Collections.singletonList(0));
        }
    }

    public boolean getIsInternal() {
        return isInternal;
    }
}
