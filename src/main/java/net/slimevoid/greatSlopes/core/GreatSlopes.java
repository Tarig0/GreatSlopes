package net.slimevoid.greatSlopes.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.slimevoid.greatSlopes.block.BlockCamoSlope;
import net.slimevoid.greatSlopes.block.BlockBandSaw;
import net.slimevoid.greatSlopes.client.ShaderHelper;
import net.slimevoid.greatSlopes.client.event.ModelBaker;
import net.slimevoid.greatSlopes.client.renderer.block.statemap.CamoStateMapper;
import net.slimevoid.greatSlopes.core.lib.ConfigLib;
import net.slimevoid.greatSlopes.item.ItemCamoTool;
import net.slimevoid.greatSlopes.tileentity.TileEntityCamoBase;
import net.slimevoid.greatSlopes.util.EnumDirectionQuatrent;
import net.slimevoid.greatSlopes.util.SlopeFactory;

import java.util.List;


/**
 * Created by Allen on 3/21/2015.
 *
 */
@Mod( modid = GreatSlopes.MODID, name = "GreatSlopes", version="0.0.1")
public class GreatSlopes {
    public static final String MODID = "great_slopes";
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigLib.InitConfig(event.getSuggestedConfigurationFile());
        ConfigLib.VertBandSaw = new BlockBandSaw(true);
        ConfigLib.HorzBandSaw = new BlockBandSaw(false);
        ConfigLib.Glue = new Item().setRegistryName("glue").setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":glue").setCreativeTab(CreativeTabs.MATERIALS);
        GameRegistry.register(ConfigLib.Glue);
        ConfigLib.BasicCamoTool = new ItemCamoTool(ConfigLib.CamoMultiplier).setRegistryName("basic_camo_tool").setUnlocalizedName(Loader.instance().activeModContainer().getModId() + ":basic_camo_tool").setCreativeTab(ConfigLib.tabSlopes);
        GameRegistry.register(ConfigLib.BasicCamoTool);
        SlopeFactory.generate();
        if (event.getSide() == Side.CLIENT) clientPreInit();
    }

    @SideOnly(Side.CLIENT)
    private void clientPreInit(){
        ConfigLib.SlopeModel = new ResourceLocation(GreatSlopes.MODID, "slope");
        //register the modelBaker to insert the smart renderer
        MinecraftForge.EVENT_BUS.register(new ModelBaker());
        OBJLoader.INSTANCE.addDomain(MODID);
        IBlockState dstate = ConfigLib.CamoBlocks.get(0).getDefaultState().withProperty(BlockCamoSlope.DIRECTIONQUAD, EnumDirectionQuatrent.DOWNWEST).withProperty(BlockCamoSlope.CAMO, false);
        String propString = new DefaultStateMapper().getPropertyString(dstate.getProperties()).replaceAll("(?<=(,|^))slope=((?!,).)*","slope=%s");
        for(BlockCamoSlope b : ConfigLib.CamoBlocks){
            ModelLoader.setCustomStateMapper(b, new CamoStateMapper());
            List<String> values = b.TYPE.getAllowedValuesList();
            Item i = Item.getItemFromBlock(b);
            if (i != null) {
                for (int dmg = 0; dmg < values.size(); dmg++) {
                    ModelLoader.setCustomModelResourceLocation(i, dmg, new ModelResourceLocation(ConfigLib.SlopeModel,String.format(propString,values.get(dmg)) ));

                }
            }
        }
        Item i = Item.getItemFromBlock(ConfigLib.VertBandSaw);
        if(i!=null) {
            ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.delegate.name(), "inventory"));
        }
        i = Item.getItemFromBlock(ConfigLib.HorzBandSaw);
        if(i!=null) {
            ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.delegate.name(), "inventory"));
        }
        i = ConfigLib.Glue;
        ModelLoader.setCustomModelResourceLocation(i,0,new ModelResourceLocation(i.delegate.name(),"inventory"));
        i = ConfigLib.BasicCamoTool;
        ModelLoader.setCustomModelResourceLocation(i,0,new ModelResourceLocation(i.delegate.name(),"inventory"));
        ShaderHelper.initShaders();

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerTileEntity(TileEntityCamoBase.class, "slimevoid_camo");
        ConfigLib.VertBandSaw.AddRecipes(7,ConfigLib.GetVertGrade100Recipes());
        ConfigLib.VertBandSaw.AddRecipes(6,ConfigLib.GetVertGrade87_5Recipes());
        ConfigLib.VertBandSaw.AddRecipes(5,ConfigLib.GetVertGrade75Recipes());
        ConfigLib.VertBandSaw.AddRecipes(4,ConfigLib.GetVertGrade62_5Recipes());
        ConfigLib.VertBandSaw.AddRecipes(3,ConfigLib.GetVertGrade50Recipes());
        ConfigLib.VertBandSaw.AddRecipes(2,ConfigLib.GetVertGrade37_5Recipes());
        ConfigLib.VertBandSaw.AddRecipes(1,ConfigLib.GetVertGrade25Recipes());
        ConfigLib.VertBandSaw.AddRecipes(0,ConfigLib.GetVertGrade12_5Recipes());
        ConfigLib.HorzBandSaw.AddRecipes(6,ConfigLib.GetHorzPosition14Recipes());
        ConfigLib.HorzBandSaw.AddRecipes(5,ConfigLib.GetHorzPosition12Recipes());
        ConfigLib.HorzBandSaw.AddRecipes(4,ConfigLib.GetHorzPosition10Recipes());
        ConfigLib.HorzBandSaw.AddRecipes(3,ConfigLib.GetHorzPosition8Recipes());
        ConfigLib.HorzBandSaw.AddRecipes(2,ConfigLib.GetHorzPosition6Recipes());
        ConfigLib.HorzBandSaw.AddRecipes(1,ConfigLib.GetHorzPosition4Recipes());
        ConfigLib.HorzBandSaw.AddRecipes(0,ConfigLib.GetHorzPosition2Recipes());
        PotionType water = PotionType.getPotionTypeForName("water");
        if(water != null) {
            GameRegistry.addShapelessRecipe(new ItemStack(ConfigLib.Glue, 1), Items.WHEAT, Items.SUGAR, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), water));
        }
        ItemStack entryItem = new ItemStack(ConfigLib.CamoBlocks.get(ConfigLib.CamoBlocks.size() - 1),8);
        entryItem.setItemDamage(ConfigLib.CamoBlocks.get(ConfigLib.CamoBlocks.size() - 1).TYPE.getAllowedValuesList().indexOf("0_base16"));
        GameRegistry.addRecipe(new ShapedOreRecipe(entryItem,new String[] {"ppp","pgp","ppp"},'p',"plankWood",'g',ConfigLib.Glue));
        GameRegistry.addRecipe(new ItemStack(ConfigLib.VertBandSaw),new String[] {"rir","gig","sis"},'r',Items.REDSTONE,'i',Items.IRON_INGOT,'g', Blocks.GLASS_PANE,'s',Blocks.STONE);
        GameRegistry.addRecipe(new ItemStack(ConfigLib.HorzBandSaw),new String[] {"rgr","iii","sgs"},'r',Items.REDSTONE,'i',Items.IRON_INGOT,'g', Blocks.GLASS_PANE,'s',Blocks.STONE);
        GameRegistry.addRecipe(new ItemStack(ConfigLib.BasicCamoTool, 1),new String[] {"GRG","RXR","RGR"},'G',Blocks.GLASS,'R',Items.REDSTONE,'X',Blocks.IRON_BLOCK );
        ConfigLib.saveConfig();
    }




}
