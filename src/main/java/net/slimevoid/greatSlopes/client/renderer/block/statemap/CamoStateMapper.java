package net.slimevoid.greatSlopes.client.renderer.block.statemap;

/*
 * Created by alcoo on 10/30/2016.
 *
 */

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;

import javax.annotation.Nonnull;

public class CamoStateMapper extends StateMapperBase
{
    private ResourceLocation baseModel;
    public CamoStateMapper(ResourceLocation baseModel){
        this.baseModel = baseModel;
    }
    @Nonnull
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state)
    {
        if (state.getValue(BlockCamoSlope.CAMO)) {
            return new ModelResourceLocation(baseModel, "normal");
        }else {
            return new ModelResourceLocation(baseModel, getPropertyString(state.getProperties()));
        }
    }
}