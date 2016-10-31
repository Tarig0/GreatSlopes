package net.slimevoid.greatSlopes.common.property;

import com.google.common.base.Optional;
import net.minecraft.block.properties.PropertyHelper;
import net.slimevoid.greatSlopes.util.SlopeFactory;
import net.slimevoid.greatSlopes.util.SlopeShape;

import java.util.Collection;
import java.util.List;

/**
 * Created by alcoo on 10/30/2016.
 */
public class PropertyLookup extends PropertyHelper<String>{

    private List<String> allowedValues;

    public PropertyLookup(String name,  List<String> allowedValues) {
        super(name, String.class);
        this.allowedValues=allowedValues;
    }


    @Override
    public Collection<String> getAllowedValues() {
        return allowedValues;
    }

    @Override
    public Optional<String> parseValue(String value) {
        return getAllowedValues().contains(value)?Optional.of(value):Optional.<String>absent();
    }

    @Override
    public String getName(String value) {
        return value;
    }

    public SlopeShape getSlopeShape(String value){
        return SlopeFactory.Shapes.get(value);
    }

}
