package net.slimevoid.greatSlopes.common.property;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by alcoo on 12/12/2015.
 *
 */
public class UnlistedPropertyIBlockState implements IUnlistedProperty<IBlockState> {
    private final String name;
    private UnlistedPropertyIBlockState(String name){
        this.name = name;
    }

    public static UnlistedPropertyIBlockState create(String name){
        return new UnlistedPropertyIBlockState(name);
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isValid(IBlockState value)
    {
        return true;
    }

    public Class<IBlockState> getType()
    {
        return IBlockState.class;
    }

    public String valueToString(IBlockState value)
    {
        return value.toString();
    }
}
