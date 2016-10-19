package net.slimevoid.camolib.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;
import net.slimevoid.camolib.util.EnumSlope;

import java.util.Collection;

/**
 * Created by alcoo on 3/12/2016.
 *
 */
public class PropertySlopeShape extends PropertyEnum {

    protected PropertySlopeShape(String name, Collection values) {
        super(name, EnumSlope.class, values);
    }

    /**
     * Create a new PropertyDirection with the given name
     */
    public static PropertySlopeShape create(String name) {
        /**
         * Create a new PropertyDirection with all directions that match the given Predicate
         */
        return create(name, Predicates.alwaysTrue());
    }

    /**
     * Create a new PropertyDirection with all directions that match the given Predicate
     */
    public static PropertySlopeShape create(String name, Predicate filter) {
        /**
         * Create a new PropertyDirection for the given direction values
         */
        return create(name, Collections2.filter(Lists.newArrayList(EnumSlope.values()), filter));
    }

    /**
     * Create a new PropertyDirection for the given direction values
     */
    public static PropertySlopeShape create(String name, Collection values) {
        return new PropertySlopeShape(name, values);
    }
}

