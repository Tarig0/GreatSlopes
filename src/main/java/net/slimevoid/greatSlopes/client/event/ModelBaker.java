package net.slimevoid.greatSlopes.client.event;

import com.google.common.base.Throwables;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.client.ShaderHelper;
import net.slimevoid.greatSlopes.client.renderer.block.model.ModelBlockCamo;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;
import net.slimevoid.greatSlopes.item.ItemCamoTool;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import static net.slimevoid.greatSlopes.block.BlockCamoSlope.*;


/**
 * Created by Allen on 3/24/2015.
 *
 */
@SideOnly(Side.CLIENT)
public class ModelBaker {
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        event.getModelRegistry().putObject(new ModelResourceLocation(ConfigLib.SlopeModel, "normal"), new ModelBlockCamo());
    }

    private static final MethodHandle renderPosX_getter, renderPosY_getter, renderPosZ_getter;

    static {

        try {
            Field f = ReflectionHelper.findField(RenderManager.class, "renderPosX");
            f.setAccessible(true);
            renderPosX_getter = MethodHandles.publicLookup().unreflectGetter(f);
            f = ReflectionHelper.findField(RenderManager.class, "renderPosY");
            f.setAccessible(true);
            renderPosY_getter = MethodHandles.publicLookup().unreflectGetter(f);
            f = ReflectionHelper.findField(RenderManager.class, "renderPosZ");
            f.setAccessible(true);
            renderPosZ_getter = MethodHandles.publicLookup().unreflectGetter(f);
        } catch (IllegalAccessException e) {
            //Botania.LOGGER.fatal("Couldn't initialize client methodhandles! Things will be broken!");
            e.printStackTrace();
            throw Throwables.propagate(e);
        }

    }
    @SubscribeEvent
    public void onWorldRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (mc.thePlayer != null && mc.objectMouseOver != null) {
            EnumHand hand = EnumHand.MAIN_HAND;
            ItemStack i = mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND);
            if (i == null || !(i.getItem() instanceof ItemCamoTool)) {
                hand = EnumHand.OFF_HAND;
                i = mc.thePlayer.getHeldItem(EnumHand.OFF_HAND);
            }
            if (i != null && i.getItem() instanceof ItemCamoTool) {
                renderPlayerLook(mc.thePlayer,hand, mc.objectMouseOver, i);
            }
        }
    }

    private static void renderPlayerLook(EntityPlayer playerIn, EnumHand hand, RayTraceResult src, ItemStack heldCamoTool) {
        BlockPos anchorPos = src.getBlockPos();
        IBlockState targetState = playerIn.getEntityWorld().getBlockState(anchorPos);
        if (playerIn.getEntityWorld().getBlockState(anchorPos).getBlock() instanceof BlockCamoSlope) {
            IBlockState state = targetState.getActualState(playerIn.getEntityWorld(),anchorPos).withProperty(CAMO, true);
            GlStateManager.pushMatrix();
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();
            ShaderHelper.useShader(ShaderHelper.alpha, shader -> {
                int alpha = ARBShaderObjects.glGetUniformLocationARB(shader, "alpha");
                ARBShaderObjects.glUniform1fARB(alpha, 0.4F);
            });

            renderComponentInWorld(playerIn,hand,anchorPos, state,heldCamoTool,src.sideHit);

            ShaderHelper.releaseShader();
            GL11.glPopAttrib();
            GlStateManager.popMatrix();

        }
    }

    private static boolean renderComponentInWorld(EntityPlayer playerIn, EnumHand hand,BlockPos anchorPos, IBlockState state, ItemStack heldCamoTool, EnumFacing sideHit) {
        double renderPosX, renderPosY, renderPosZ;
        try {
            renderPosX = (double) renderPosX_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
            renderPosY = (double) renderPosY_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
            renderPosZ = (double) renderPosZ_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());

        } catch (Throwable t) {
            return true;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(-renderPosX, -renderPosY, -renderPosZ);
        GlStateManager.disableDepth();
        doRenderComponent(playerIn,hand,state, anchorPos,heldCamoTool,sideHit);
        GlStateManager.popMatrix();
        return true;

    }

    private static void doRenderComponent(EntityPlayer playerIn,EnumHand hand,IBlockState state, BlockPos pos, ItemStack heldCamoTool, EnumFacing sideHit) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.rotate(90.0F, 0.0F, -1.0F, 0.0F);
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        state = ((ItemCamoTool) heldCamoTool.getItem()).getPaintedState(heldCamoTool,playerIn,hand ,(IExtendedBlockState) state.getBlock().getExtendedState(state,playerIn.getEntityWorld(),pos), sideHit);
        if (state != null) {
            Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0F, true);
        }
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}


