package net.slimevoid.greatSlopes.common.property;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by alcoo on 12/12/2015.
 *
 */
public class UnlistedPropertyBlockPos implements IUnlistedProperty<BlockPos> {
    private final String name;
    private UnlistedPropertyBlockPos(String name){
        this.name = name;
    }

    public static UnlistedPropertyBlockPos create(String name){
        return new UnlistedPropertyBlockPos(name);
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isValid(BlockPos value){return value != null;}

    public Class<BlockPos> getType()
    {
        return BlockPos.class;
    }

    public String valueToString(BlockPos value)
    {
        return value.toString();
    }
}
