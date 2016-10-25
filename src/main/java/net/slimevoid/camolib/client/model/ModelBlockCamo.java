package net.slimevoid.camolib.client.model;

import com.sun.istack.internal.NotNull;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.slimevoid.camolib.block.BlockCamoSlope;
import net.slimevoid.camolib.core.lib.ConfigLib;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Allen on 3/24/2015.
 *
 */
public class ModelBlockCamo implements IBakedModel {

    public ModelBlockCamo(){}


    private ArrayList<BakedQuad> CamoSide(IExtendedBlockState origState,@NotNull IBlockState camo,@NotNull IVertexProducer origFace) {
        ArrayList<BakedQuad> ret = new ArrayList<BakedQuad>();
        //Set Orientation and facings
        for (Object p : camo.getPropertyNames()) {
            if (p instanceof PropertyDirection) {
                if (((PropertyDirection) p).getAllowedValues().size() > 4) {
                    camo = camo.withProperty((IProperty) p, origState.getValue(BlockCamoSlope.FACING));
                } else {
                    camo = camo.withProperty((IProperty) p, origState.getValue(BlockCamoSlope.HFACING));
                }
            }
        }
        //only know how to handle stuff that extend bakedQuad
        if (BakedQuad.class.isAssignableFrom(origFace.getClass())) {
            //check to see if we can render the current camo block
            //might need to add a check per Quad if multipass blocks are a thing
            if (camo.getBlock().canRenderInLayer(camo,MinecraftForgeClient.getRenderLayer())) {
                IBakedModel camoModel = FMLClientHandler.instance().getClient().getBlockRendererDispatcher()
                        .getModelForState(camo);
                List listQuadsIn = camoModel.getQuads(camo, ((BakedQuad) origFace).getFace(), 0);
                //Loop through each quad
                for (Object quad : listQuadsIn) {
                    if (BakedQuad.class.isAssignableFrom(quad.getClass()))
                        ret.add(CamoQuad(origState,camo, (BakedQuad) origFace, (BakedQuad) quad));
                }
            }
        }
        return ret;
    }

    private BakedQuad CamoQuad(IExtendedBlockState origState,IBlockState camo,BakedQuad origFace,BakedQuad quad) {
        int[] Cs = new int[4];
        int[] UVs = new int[8];
        int[] targetInts = quad.getVertexData();
        //fill in Color, U, V info
        for (int i = 0; i < 4; ++i) {
            Cs[i] = targetInts[(i * 7) + 3];
            UVs[(i * 2) ] = targetInts[(i * 7) + 4];
            UVs[(i * 2) + 1] = targetInts[(i * 7) + 5];
        }
        CalcColor(origState, camo, quad.hasTintIndex(), Cs);

        //Finally replace color, U and V info for each face in the camo model
        int origInts[] = origFace.getVertexData();
        UVs = ReplaceTexture(origInts,origFace.getFace(), UVs);

        //this returns an array in the following format
        //X,Y,Z,Color,U,V,?Light? for each point
        int finalInts[] = {
                origInts[0], origInts[1], origInts[2], Cs[0], UVs[0], UVs[1],origInts[6],
                origInts[7], origInts[8], origInts[9], Cs[1], UVs[2], UVs[3],origInts[13],
                origInts[14], origInts[15], origInts[16], Cs[2], UVs[4], UVs[5],origInts[20],
                origInts[21], origInts[22], origInts[23], Cs[3], UVs[6], UVs[7],origInts[27]};
        return new BakedQuad(finalInts, origFace.getTintIndex(), origFace.getFace(),quad.getSprite(),true, DefaultVertexFormats.ITEM);
    }

    private int[] CalcColor(IExtendedBlockState origState, IBlockState camo, boolean hasTint, int[] Cs) {
        int renderLayer = MinecraftForgeClient.getRenderLayer().ordinal();
        BlockPos pos = origState.getValue(BlockCamoSlope.POS);
        BlockColors colorEngine = FMLClientHandler.instance().getClient().getBlockColors();
        WorldClient worldClient = FMLClientHandler.instance().getWorldClient();
        //Get the base color multiplier from the camo block can be set through state later
        int i1 = colorEngine.colorMultiplier(origState,worldClient,pos, renderLayer);

        //If Side had a tint such as grass then multiply that with what we got so far
        if (hasTint) {
            i1 = GetColorMulti(colorEngine.colorMultiplier(camo,worldClient,pos,renderLayer), i1);
        }

        //and also multiply the color found in the vertex data
        Cs[0] = GetColorMulti(Cs[0], i1);
        Cs[1] = GetColorMulti(Cs[1], i1);
        Cs[2] = GetColorMulti(Cs[2], i1);
        Cs[3] = GetColorMulti(Cs[3], i1);

        return Cs;
    }

    //This does the actual camouflage
    private int[] ReplaceTexture(int[] origInts,EnumFacing face , int[] UVs) {

        switch(face){
            case DOWN:
            case UP:
                UVs = CalcUVs(
                        Arrays.asList(
                                Float.intBitsToFloat(origInts[0]),
                                Float.intBitsToFloat(origInts[7]),
                                Float.intBitsToFloat(origInts[14]),
                                Float.intBitsToFloat(origInts[21])),
                        Arrays.asList(
                                Float.intBitsToFloat(origInts[2]),
                                Float.intBitsToFloat(origInts[9]),
                                Float.intBitsToFloat(origInts[16]),
                                Float.intBitsToFloat(origInts[23])),
                        UVs,
                        face == EnumFacing.UP,
                        true);
                break;
            case NORTH:
            case SOUTH:
                UVs = CalcUVs(
                        Arrays.asList(
                                Float.intBitsToFloat(origInts[0]),
                                Float.intBitsToFloat(origInts[7]),
                                Float.intBitsToFloat(origInts[14]),
                                Float.intBitsToFloat(origInts[21])),
                        Arrays.asList(
                                Float.intBitsToFloat(origInts[1]),
                                Float.intBitsToFloat(origInts[8]),
                                Float.intBitsToFloat(origInts[15]),
                                Float.intBitsToFloat(origInts[22])),
                        UVs,
                        false,
                        face == EnumFacing.SOUTH);
                break;
            case WEST:
            case EAST:
                UVs = CalcUVs(
                        Arrays.asList(
                                Float.intBitsToFloat(origInts[2]),
                                Float.intBitsToFloat(origInts[9]),
                                Float.intBitsToFloat(origInts[16]),
                                Float.intBitsToFloat(origInts[23])),
                        Arrays.asList(
                                Float.intBitsToFloat(origInts[1]),
                                Float.intBitsToFloat(origInts[8]),
                                Float.intBitsToFloat(origInts[15]),
                                Float.intBitsToFloat(origInts[22])),
                        UVs,
                        false,
                        face == EnumFacing.WEST);
                break;
        }
        return UVs;

    }

    //modifies UVs to match
    //origInts are used to figure out the cut off for the texture
    private int[] CalcUVs(List<Float> mUs, List<Float> mVs, int[] UVs, boolean flipV, boolean flipU) {
        List<Integer> Us = Arrays.asList(UVs[0], UVs[2], UVs[4], UVs[6]);
        List<Integer> Vs = Arrays.asList(UVs[1], UVs[3], UVs[5], UVs[7]);
        int maxU = Collections.max(Us);
        int minU = Collections.min(Us);
        int deltaU = maxU - minU;
        int maxV = Collections.max(Vs);
        int minV = Collections.min(Vs);
        int deltaV = maxV - minV;
        //all the coords are float from 0 -1 stored as ints
        //float shiftV = 1 -Collections.max(mVs); // Used to shift the textures down so that overlays like grass are in view
        //Use intBitsToFloat to get the correct decimal
        for (int i = 0; i < 4; i++) {
            if (flipU) {
                UVs[(i * 2)] = (int) (minU + (mUs.get(i) * deltaU));
            } else {
                UVs[(i * 2)] = (int) (maxU - (mUs.get(i) * deltaU));
            }
            if (flipV) {
                UVs[(i * 2) + 1] = (int) (minV + (mVs.get(i) * deltaV));
            } else {
                //UVs[(i * 2) + 1] = (int) (maxV - ((mVs.get(i) + shiftV) * deltaV)); //example usage of shiftV
                UVs[(i * 2) + 1] = (int) (maxV - (mVs.get(i)  * deltaV));
            }
        }
        return UVs;
    }

    private int GetColorMulti(int raw,int raw2) {

        int l = (int)((float)(raw & 255) * ((float) (raw2 >> 16 & 255) / 255.0F));
        int i1  = (int)((float)(raw >> 8 & 255) * ((float) (raw2 >> 8 & 255) / 255.0F));
        int j1 = (int)((float)(raw >> 16 & 255) * ((float) (raw2 & 255) / 255.0F));

            raw &= -16777216;
            raw |= j1 << 16 | i1 << 8 | l;


        return raw;
    }

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing facing, long rand) {
        //get model of block if we didn't camo this will allow multiple models to be handled
        List<BakedQuad> origFaces = GetNonCamoModel((IExtendedBlockState) state).getQuads(state, null, rand);

        if (facing != null) {
            List<BakedQuad> origSidedFaces = GetNonCamoModel((IExtendedBlockState) state).getQuads(state, facing, rand); //vanilla models actually do sided
            IBlockState camoState = ((IExtendedBlockState) state).getValue(BlockCamoSlope.BLOCKSTATES[facing.ordinal()]);
            if (camoState != null) {
                List<BakedQuad> ret = new ArrayList<BakedQuad>();
                for (BakedQuad face : origFaces) {
                    if (face.getFace() == facing)
                        ret.addAll(CamoSide((IExtendedBlockState) state, camoState, face));
                }
                for (BakedQuad face : origSidedFaces) {
                        ret.addAll(CamoSide((IExtendedBlockState) state, camoState, face));
                }
                return ret;
            } else if (state.getBlock().canRenderInLayer(state, MinecraftForgeClient.getRenderLayer())) {
                List<BakedQuad> ret = new ArrayList<BakedQuad>();
                for (BakedQuad face : origFaces) {
                    if (face.getFace() == facing)
                        ret.add(face);
                }
                for (BakedQuad face : origSidedFaces) {
                        ret.add(face);
                }
                return ret;
            }
        }
        return Collections.emptyList();
    }


    private IBakedModel GetNonCamoModel(IExtendedBlockState origState) {
        IBlockState tempState = origState.getBlock().getDefaultState();
        for (IProperty prop : origState.getPropertyNames()) {
            if (!prop.getName().equals("camo")) {
                tempState = tempState.withProperty(prop, origState.getValue(prop));
            }
        }

        return  Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
                .getModelForState(tempState);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }


    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleTexture() {
        return FMLClientHandler.instance().getClient().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(ConfigLib.CamoBlocks[0].getDefaultState()).getParticleTexture();
    }

    @Override
    @Nonnull
    public net.minecraft.client.renderer.block.model.ItemCameraTransforms getItemCameraTransforms() {
        return net.minecraft.client.renderer.block.model.ItemCameraTransforms.DEFAULT;
    }
    @Override
    @Nonnull
    public ItemOverrideList getOverrides(){
        return ItemOverrideList.NONE;
    }
}
