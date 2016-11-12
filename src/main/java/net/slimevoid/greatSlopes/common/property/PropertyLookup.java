package net.slimevoid.greatSlopes.common.property;

import com.google.common.base.Optional;
import net.minecraft.block.properties.PropertyHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Created by alcoo on 10/30/2016.
 *
 */
public abstract class PropertyLookup<T> extends PropertyHelper<String>{
    private List<String> allowedValues;
    protected PropertyLookup(String name,  List<String> allowedValues) {
        super(name, String.class);
        this.allowedValues=allowedValues;
    }
    @Override
    @Nonnull
    public Collection<String> getAllowedValues() {
        return allowedValues;
    }
    @SuppressWarnings("Guava")
    @Override
    @Nonnull
    public Optional<String> parseValue(@Nonnull String value) {
        return getAllowedValues().contains(value)?Optional.of(value):Optional.absent();
    }
    @Override
    @Nonnull
    public String getName(@Nonnull String value) {
        return value;
    }

    public abstract T getLookup(String value);

    public List<String> getAllowedValuesList() {
        return allowedValues;
    }
}
