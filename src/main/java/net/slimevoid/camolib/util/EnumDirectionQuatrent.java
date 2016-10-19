package net.slimevoid.camolib.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * Created by alcoo on 12/26/2015.
 */
public enum EnumDirectionQuatrent   implements IStringSerializable {
    DOWNNORTH("downnorth"),
    DOWNSOUTH("downsouth"),
    DOWNWEST("downwest"),
    DOWNEAST("downeast"),
    UPNORTH("upnorth"),
    UPSOUTH("upsouth"),
    UPWEST("upwest"),
    UPEAST("upeast"),
    NORTHDOWN("northdown"),
    NORTHEAST("northeast"),
    NORTHUP("northup"),
    NORTHWEST("northwest"),
    SOUTHDOWN("southdown"),
    SOUTHWEST("southwest"),
    SOUTHUP("southup"),
    SOUTHEAST("southeast"),
    WESTDOWN("westdown"),
    WESTNORTH("westnorth"),
    WESTUP("westup"),
    WESTSOUTH("westsouth"),
    EASTDOWN("eastdown"),
    EASTSOUTH("eastsouth"),
    EASTUP("eastup"),
    EASTNORTH("eastnorth");

    private final String name;

    EnumDirectionQuatrent(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return this.name;
    }

    public String getName()
    {
        return this.name;
    }

    public static Comparable get(EnumFacing anchor, int quadidx) {
        return values()[((anchor.ordinal()) * 4) + (quadidx % 4)];
    }

}