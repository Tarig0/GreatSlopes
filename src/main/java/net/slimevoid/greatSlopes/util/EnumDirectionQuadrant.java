package net.slimevoid.greatSlopes.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Created by alcoo on 12/26/2015.
 *
 */
public enum EnumDirectionQuadrant implements IStringSerializable {
    DOWNSOUTH(EnumFacing.DOWN,EnumFacing.SOUTH),
    DOWNWEST(EnumFacing.DOWN,EnumFacing.WEST),
    DOWNNORTH(EnumFacing.DOWN,EnumFacing.NORTH),
    DOWNEAST(EnumFacing.DOWN,EnumFacing.EAST),
    UPSOUTH(EnumFacing.UP,EnumFacing.SOUTH),
    UPWEST(EnumFacing.UP,EnumFacing.WEST),
    UPNORTH(EnumFacing.UP,EnumFacing.NORTH),
    UPEAST(EnumFacing.UP,EnumFacing.EAST),
    NORTHDOWN(EnumFacing.NORTH,EnumFacing.DOWN),
    NORTHWEST(EnumFacing.NORTH,EnumFacing.WEST),
    NORTHUP(EnumFacing.NORTH,EnumFacing.UP),
    NORTHEAST(EnumFacing.NORTH,EnumFacing.EAST),
    SOUTHDOWN(EnumFacing.SOUTH,EnumFacing.DOWN),
    SOUTHWEST(EnumFacing.SOUTH,EnumFacing.WEST),
    SOUTHUP(EnumFacing.SOUTH,EnumFacing.UP),
    SOUTHEAST(EnumFacing.SOUTH,EnumFacing.EAST),
    WESTDOWN(EnumFacing.WEST,EnumFacing.DOWN),
    WESTSOUTH(EnumFacing.WEST,EnumFacing.SOUTH),
    WESTUP(EnumFacing.WEST,EnumFacing.UP),
    WESTNORTH(EnumFacing.WEST,EnumFacing.NORTH),
    EASTDOWN(EnumFacing.EAST,EnumFacing.DOWN),
    EASTSOUTH(EnumFacing.EAST,EnumFacing.SOUTH),
    EASTUP(EnumFacing.EAST,EnumFacing.UP),
    EASTNORTH(EnumFacing.EAST,EnumFacing.NORTH);

    private final String name;
    private final EnumFacing anchor;
    private final EnumFacing facing;

    EnumDirectionQuadrant(EnumFacing anchor, EnumFacing facing)
    {
        this.anchor = anchor;
        this.facing = facing;
        this.name = (anchor.getName() + facing.getName()).toLowerCase(Locale.ENGLISH);
    }

    public String toString()
    {
        return this.name;
    }
    @Nonnull
    public String getName()
    {
        return this.name;
    }

    public static Comparable get(EnumFacing anchor, int quadidx) {
        return values()[((anchor.ordinal()) * 4) + (quadidx % 4)];
    }

    public EnumFacing getAnchor(){
        return this.anchor;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    public int getQuadOridinal() {
        return this.ordinal()%4;
    }
}