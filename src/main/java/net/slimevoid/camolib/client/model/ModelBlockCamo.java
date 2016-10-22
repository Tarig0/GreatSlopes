package net.slimevoid.camolib.client.model;

import com.sun.istack.internal.NotNull;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.slimevoid.camolib.block.BlockCamoSlope;
import net.slimevoid.camolib.core.lib.ConfigLib;

import javax.annotation.Nonnull;
import java.util.ArrayList;
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
        int[] UVs = new int[12];

        //fill in Color, U, V info
        for (int i = 0; i < 4; ++i) {
            UVs[(i * 3)] = quad.getVertexData()[(i * 7) + 3];
            UVs[(i * 3) + 1] = quad.getVertexData()[(i * 7) + 4];
            UVs[(i * 3) + 2] = quad.getVertexData()[(i * 7) + 5];
        }
        //Get the base color multiplier from the camo block can be set through state later
        int i1 = FMLClientHandler.instance().getClient().getBlockColors().colorMultiplier(ConfigLib.CamoBlocks[0].getDefaultState(),FMLClientHandler.instance().getWorldClient(),origState.getValue(BlockCamoSlope.POS),MinecraftForgeClient.getRenderLayer().ordinal());

        //If Side had a tint such as grass then multiply that with what we got so far
        if (quad.hasTintIndex()) {
            i1 = GetColorMulti(FMLClientHandler.instance().getClient().getBlockColors().colorMultiplier(camo,FMLClientHandler.instance().getWorldClient(),origState.getValue(BlockCamoSlope.POS),MinecraftForgeClient.getRenderLayer().ordinal())
                    , i1
            );
        }

        //and also multiply the color found in the vertex data
        UVs[0] = GetColorMulti(UVs[0], i1);
        UVs[3] = GetColorMulti(UVs[3], i1);
        UVs[6] = GetColorMulti(UVs[6], i1);
        UVs[9] = GetColorMulti(UVs[9], i1);
        //Finally replace color, U and V info for each face in the camo model
        return ReplaceTexture(origFace, UVs, quad.getSprite());
    }

    //This does the actual camouflage
    private BakedQuad ReplaceTexture(BakedQuad origFace, int[] CUVs,TextureAtlasSprite sprite) {
        int origInts[] = origFace.getVertexData();
        //this returns an array in the following format

        switch(origFace.getFace()){
            case DOWN: //possibly need to handle flipped texture
            case UP:
                CUVs = CalcUVs(new int[]{0,2},origInts,CUVs);
                break;
            case NORTH:
            case SOUTH:
                CUVs = CalcUVs(new int[]{0,1},origInts,CUVs);
                break;
            case WEST:
            case EAST:
                CUVs = CalcUVs(new int[]{2,1},origInts,CUVs);
                break;
        }

        //X,Y,Z,Color,U,V,?Light? for each point
        int finalInts[] = {
                origInts[0], origInts[1], origInts[2], CUVs[0], CUVs[1], CUVs[2],origInts[6],
                origInts[7], origInts[8], origInts[9], CUVs[3], CUVs[4], CUVs[5],origInts[13],
                origInts[14], origInts[15], origInts[16], CUVs[6], CUVs[7], CUVs[8],origInts[20],
                origInts[21], origInts[22], origInts[23], CUVs[9], CUVs[10], CUVs[11],origInts[27]};
        return new BakedQuad(finalInts, origFace.getTintIndex(), origFace.getFace(),sprite,true, DefaultVertexFormats.ITEM);
    }

    //modifies UVs to match
    private int[] CalcUVs(int[] ints, int[] origInts, int[] CUVs) {
        int maxU = Math.max(CUVs[1],Math.max(CUVs[4],Math.max(CUVs[7],CUVs[10])));
        int deltaU = maxU - Math.min(CUVs[1],Math.min(CUVs[4],Math.min(CUVs[7],CUVs[10])));
        int maxV = Math.max(CUVs[2],Math.max(CUVs[5],Math.max(CUVs[8],CUVs[11])));
        int deltaV = maxV - Math.min(CUVs[2],Math.min(CUVs[5],Math.min(CUVs[8],CUVs[11])));
        //all the coords are float from 0 -1 stored as ints
        //Use intBitsToFloat to get the correct decimal
        for (int i=0;i<4;i++) {
            CUVs[(i * 3) + 1] = (int)(maxU - (Float.intBitsToFloat(origInts[(7 * i) + ints[0]]) * deltaU));
            CUVs[(i * 3) + 2] = (int)(maxV - (Float.intBitsToFloat(origInts[(7 * i) + ints[1]]) * deltaV));
        }

        return CUVs;
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
            IBlockState camoState = ((IExtendedBlockState) state).getValue(BlockCamoSlope.BLOCKSTATES[facing.ordinal()]);
            if (camoState != null) {
                List<BakedQuad> ret = new ArrayList<BakedQuad>();
                for (BakedQuad face : origFaces) {
                    if (face.getFace() == facing)
                        ret.addAll(CamoSide((IExtendedBlockState) state, camoState, face));
                }
                return ret;
            } else if (state.getBlock().canRenderInLayer(state, MinecraftForgeClient.getRenderLayer())) {
                List<BakedQuad> ret = new ArrayList<BakedQuad>();
                for (BakedQuad face : origFaces) {
                    if (face.getFace() == facing)
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
