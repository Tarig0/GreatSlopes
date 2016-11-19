package net.slimevoid.greatSlopes.client.model;

import com.google.common.base.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.*;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.core.helpers.Strings;

import javax.vecmath.Vector4f;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by alcoo on 11/15/2016.
 *
 */
public class SidedOBJModel extends OBJModel {

    private final ResourceLocation modelLocation;
    private final CustomData customData;

    public SidedOBJModel(MaterialLibrary matLib, ResourceLocation modelLocation)
    {
        super(matLib, modelLocation);
        this.modelLocation = modelLocation;
        this.customData = new CustomData();
    }

    public SidedOBJModel(MaterialLibrary matLib, ResourceLocation modelLocation, CustomData customData) {
        super(matLib,modelLocation);
        this.modelLocation = modelLocation;
        this.customData = new CustomData();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
        builder.put(ModelLoader.White.LOCATION.toString(), ModelLoader.White.INSTANCE);
        TextureAtlasSprite missing = bakedTextureGetter.apply(new ResourceLocation("missingno"));
        for (String e : this.getMatLib().getMaterialNames())
        {
            Material value = this.getMatLib().getMaterial(e);
            if (value.getTexture().getTextureLocation().getResourcePath().startsWith("#"))
            {
                FMLLog.severe("OBJLoader: Unresolved texture '%s' for obj model ?", value.getTexture().getTextureLocation().getResourcePath());
                builder.put(e, missing);
            }
            else
            {
                builder.put(e, bakedTextureGetter.apply(value.getTexture().getTextureLocation()));
            }
        }
        builder.put("missingno", missing);
        return new OBJBakedModel(this, state, format, builder.build());
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData)
    {
        OBJModel ret = new SidedOBJModel(this.getMatLib(), this.modelLocation, new CustomData(this.customData, customData));
        return ret;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures)
    {
        OBJModel ret = new SidedOBJModel(this.getMatLib().makeLibWithReplacements(textures), this.modelLocation, this.customData);
        return ret;
    }

    static class CustomData
    {
        public boolean ambientOcclusion = true;
        public boolean gui3d = true;
        public boolean flipV = false;

        public CustomData(SidedOBJModel.CustomData parent, ImmutableMap<String, String> customData)
        {
            this.ambientOcclusion = parent.ambientOcclusion;
            this.gui3d = parent.gui3d;
            this.flipV = parent.flipV;
            this.process(customData);
        }

        public CustomData() {}

        public void process(ImmutableMap<String, String> customData)
        {
            for (Map.Entry<String, String> e : customData.entrySet())
            {
                if (e.getKey().equals("ambient"))
                    this.ambientOcclusion = Boolean.valueOf(e.getValue());
                else if (e.getKey().equals("gui3d"))
                    this.gui3d = Boolean.valueOf(e.getValue());
                else if (e.getKey().equals("flip-v"))
                    this.flipV = Boolean.valueOf(e.getValue());
            }
        }


    }

    public static class Parser
    {
        private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
        private static Set<String> unknownObjectCommands = new HashSet<String>();
        public MaterialLibrary materialLibrary = new MaterialLibrary();
        private final IResourceManager manager;
        private BufferedReader objReader;
        private ResourceLocation objFrom;

        private List<String> groupList = Lists.newArrayList();
        private List<Vertex> vertices = Lists.newArrayList();
        private List<Normal> normals = Lists.newArrayList();
        private List<TextureCoordinate> texCoords = Lists.newArrayList();
        public Parser(IResource from, IResourceManager manager) throws IOException {
            this.manager = manager;
            this.objFrom = from.getResourceLocation();
            objReader = new BufferedReader(new InputStreamReader(from.getInputStream(), Charsets.UTF_8));
        }
        private float[] parseFloats(String[] data) // Helper converting strings to floats
        {
            float[] ret = new float[data.length];
            for (int i = 0; i < data.length; i++)
                ret[i] = Float.parseFloat(data[i]);
            return ret;
        }
        //Partial reading of the OBJ format. Documentation taken from http://paulbourke.net/dataformats/obj/
        public SidedOBJModel parse() throws IOException
        {
            String currentLine = "";
            Material material = new Material();
            material.setName(Material.DEFAULT_NAME);
            int usemtlCounter = 0;
            int lineNum = 0;
            int currentFaceIndex = -1;
            for (;;)
            {
                lineNum++;
                currentLine = objReader.readLine();
                if (currentLine == null) break;
                currentLine.trim();
                if (currentLine.isEmpty() || currentLine.startsWith("#")) continue;

                try
                {
                    String[] fields = WHITE_SPACE.split(currentLine, 2);
                    String key = fields[0];
                    String data = fields[1];
                    String[] splitData = WHITE_SPACE.split(data);

                    if (key.equalsIgnoreCase("mtllib"))
                    {
                        this.materialLibrary.parseMaterials(manager, data, objFrom);
                    }
                    else if (key.equalsIgnoreCase("usemtl"))
                    {
                        material = this.materialLibrary.getMaterial(data);
                        usemtlCounter++;
                    }
                    else if (key.equalsIgnoreCase("v")) // Vertices: x y z [w] - w Defaults to 1.0
                    {
                        float[] coords = parseFloats(splitData);
                        Vector4f pos = new Vector4f(coords[0], coords[1], coords[2], coords.length == 4 ? coords[3] : 1.0F);
                        this.vertices.add(new Vertex(pos, material));
                    }
                    else if (key.equalsIgnoreCase("vn")) // Vertex normals: x y z
                    {
                        this.normals.add(new Normal(parseFloats(splitData)));
                    }
                    else if (key.equalsIgnoreCase("vt")) // Vertex Textures: u [v] [w] - v/w Defaults to 0
                    {
                        float[] coords = parseFloats(splitData);
                        TextureCoordinate texCoord = new TextureCoordinate(coords[0],
                                coords.length >= 2 ? coords[1] : 0.0F,
                                coords.length >= 3 ? coords[2] : 0.0F);
                        if (texCoord.u < 0.0f || texCoord.u > 1.0f || texCoord.v < 0.0f || texCoord.v > 1.0f)
                            throw new UVsOutOfBoundsException(this.objFrom);
                        this.texCoords.add(texCoord);
                    }
                    else if(key.equalsIgnoreCase("s")){
                        currentFaceIndex = Integer.parseInt(data);
                    }
                    else if (key.equalsIgnoreCase("f")) // Face Elements: f v1[/vt1][/vn1] ...
                    {
                        if (splitData.length > 4)
                            FMLLog.warning("OBJModel.Parser: found a face ('f') with more than 4 vertices, only the first 4 of these vertices will be rendered!");

                        List<Vertex> v = Lists.newArrayListWithCapacity(splitData.length);

                        for (int i = 0; i < splitData.length; i++)
                        {
                            String[] pts = splitData[i].split("/");

                            int vert = Integer.parseInt(pts[0]);
                            Integer texture = pts.length < 2 || Strings.isEmpty(pts[1]) ? null : Integer.parseInt(pts[1]);
                            Integer normal = pts.length < 3 || Strings.isEmpty(pts[2]) ? null : Integer.parseInt(pts[2]);

                            vert = vert < 0 ? this.vertices.size() - 1 : vert - 1;
                            Vertex newV = new Vertex(new Vector4f(this.vertices.get(vert).getPos()), this.vertices.get(vert).getMaterial());

                            if (texture != null)
                                newV.setTextureCoordinate(this.texCoords.get(texture < 0 ? this.texCoords.size() - 1 : texture - 1));
                            if (normal != null)
                                newV.setNormal(this.normals.get(normal < 0 ? this.normals.size() - 1 : normal - 1));

                            v.add(newV);
                        }

                        Vertex[] va = v.toArray(new Vertex[v.size()]);
                        Face face;
                        if (currentFaceIndex > -1){
                            face = new Face(va, material.getName(),currentFaceIndex);
                        }else{
                            face = new Face(va, material.getName());
                        }
                        if (usemtlCounter < this.vertices.size())
                        {
                            for (Vertex ver : face.getVertices())
                            {
                                ver.setMaterial(material);
                            }
                        }

                        if (groupList.isEmpty())
                        {
                            if (this.materialLibrary.getGroups().containsKey(Group.DEFAULT_NAME))
                            {
                                this.materialLibrary.getGroups().get(Group.DEFAULT_NAME).addFace(face);
                            }
                            else
                            {
                                Group def = new Group(Group.DEFAULT_NAME, null);
                                def.addFace(face);
                                this.materialLibrary.getGroups().put(Group.DEFAULT_NAME, def);
                            }
                        }
                        else
                        {
                            for (String s : groupList)
                            {
                                if (this.materialLibrary.getGroups().containsKey(s))
                                {
                                    this.materialLibrary.getGroups().get(s).addFace(face);
                                }
                                else
                                {
                                    Group e = new Group(s, null);
                                    e.addFace(face);
                                    this.materialLibrary.getGroups().put(s, e);
                                }
                            }
                        }
                    }
                    else if (key.equalsIgnoreCase("g") || key.equalsIgnoreCase("o"))
                    {
                        groupList.clear();
                        if (key.equalsIgnoreCase("g"))
                        {
                            String[] splitSpace = data.split(" ");
                            for (String s : splitSpace)
                                groupList.add(s);
                        }
                        else
                        {
                            groupList.add(data);
                        }
                    }
                    else
                    {
                        if (!unknownObjectCommands.contains(key))
                        {
                            unknownObjectCommands.add(key);
                            FMLLog.info("OBJLoader.Parser: command '%s' (model: '%s') is not currently supported, skipping. Line: %d '%s'", key, objFrom, lineNum, currentLine);
                        }
                    }
                }
                catch (RuntimeException e)
                {
                    throw new RuntimeException(String.format("OBJLoader.Parser: Exception parsing line #%d: `%s`", lineNum, currentLine), e);
                }
            }

            return new SidedOBJModel(this.materialLibrary, this.objFrom);
        }
    }

    public static class Face extends OBJModel.Face {

        private EnumFacing side;

        public Face(Vertex[] verts, String materialName) {
            super(verts, materialName);
        }

        public Face(Vertex[] va, String name, int sideIndex) {
            this(va,name);
            side = EnumFacing.values()[sideIndex];
        }
        public Face(Vertex[] va, String name, EnumFacing side) {
            this(va,name);
            this.side = side;
        }

        public OBJModel.Face bake(TRSRTransformation transform)
        {
            EnumFacing oldFacing = getFacing();
            OBJModel.Face temp = super.bake(transform);
            EnumFacing newFacing;
            if(net.minecraftforge.common.model.TRSRTransformation.isInteger(transform.getMatrix())) {
                newFacing = transform.rotate(oldFacing);
            }else{
                newFacing = EnumFacing.getFacingFromVector(getNormal().x, getNormal().y, getNormal().z);
            }
            return new Face(temp.getVertices(),getMaterialName(),newFacing);
        }


        public EnumFacing getFacing() {
            return side!=null?side: EnumFacing.getFacingFromVector(getNormal().x, getNormal().y, getNormal().z);
        }
    }

    public class OBJBakedModel extends OBJModel.OBJBakedModel implements IPerspectiveAwareModel
    {
        private final SidedOBJModel model;
        private IModelState state;
        private final VertexFormat format;
        private ImmutableList<BakedQuad> quads;
        private ImmutableMap<String, TextureAtlasSprite> textures;
        private TextureAtlasSprite sprite = ModelLoader.White.INSTANCE;

        public OBJBakedModel(SidedOBJModel model, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textures)
        {
            super(model,state,format,textures);
            this.model = model;
            this.state = state;
            this.format = format;
            this.textures = textures;
        }

        public void scheduleRebake()
        {
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand)
        {
            if (side != null) return ImmutableList.of();
            if (quads == null)
            {
                quads = buildQuads(this.state);
            }
            if (blockState instanceof IExtendedBlockState)
            {
                IExtendedBlockState exState = (IExtendedBlockState) blockState;
                if (exState.getUnlistedNames().contains(net.minecraftforge.common.property.Properties.AnimationProperty))
                {

                    IModelState newState = exState.getValue(net.minecraftforge.common.property.Properties.AnimationProperty);
                    if (newState != null)
                    {
                        newState = new ModelStateComposition(this.state, newState);
                        return buildQuads(newState);
                    }
                }
            }
            return quads;
        }

        private ImmutableList<BakedQuad> buildQuads(IModelState modelState)
        {
            List<BakedQuad> quads = Lists.newArrayList();
            Collections.synchronizedSet(new LinkedHashSet<BakedQuad>());
            Set<OBJModel.Face> faces = Collections.synchronizedSet(new LinkedHashSet<OBJModel.Face>());
            com.google.common.base.Optional<TRSRTransformation> transform = com.google.common.base.Optional.absent();
            for (Group g : this.model.getMatLib().getGroups().values()) {
                if (modelState.apply(com.google.common.base.Optional.of(Models.getHiddenModelPart(ImmutableList.of(g.getName())))).isPresent()) {
                    continue;
                }
                transform = modelState.apply(com.google.common.base.Optional.<IModelPart>absent());
                faces.addAll(g.applyTransform(transform));
            }
            for (OBJModel.Face f : faces)
            {
                if (this.model.getMatLib().getMaterial(f.getMaterialName()).isWhite())
                {
                    for (Vertex v : f.getVertices())
                    {//update material in each vertex
                        if (!v.getMaterial().equals(this.model.getMatLib().getMaterial(v.getMaterial().getName())))
                        {
                            v.setMaterial(this.model.getMatLib().getMaterial(v.getMaterial().getName()));
                        }
                    }
                    sprite = ModelLoader.White.INSTANCE;
                }
                else sprite = this.textures.get(f.getMaterialName());
                UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
                builder.setContractUVs(true);
                if(f instanceof SidedOBJModel.Face){

                    builder.setQuadOrientation(((SidedOBJModel.Face)f).getFacing());
                }else {
                    builder.setQuadOrientation(EnumFacing.getFacingFromVector(f.getNormal().x, f.getNormal().y, f.getNormal().z));
                }
                builder.setTexture(sprite);
                Normal faceNormal = f.getNormal();
                putVertexData(builder, f.getVertices()[0], faceNormal, TextureCoordinate.getDefaultUVs()[0], sprite);
                putVertexData(builder, f.getVertices()[1], faceNormal, TextureCoordinate.getDefaultUVs()[1], sprite);
                putVertexData(builder, f.getVertices()[2], faceNormal, TextureCoordinate.getDefaultUVs()[2], sprite);
                putVertexData(builder, f.getVertices()[3], faceNormal, TextureCoordinate.getDefaultUVs()[3], sprite);
                quads.add(builder.build());
            }
            return ImmutableList.copyOf(quads);
        }

        private final void putVertexData(UnpackedBakedQuad.Builder builder, Vertex v, Normal faceNormal, TextureCoordinate defUV, TextureAtlasSprite sprite)
        {
            for (int e = 0; e < format.getElementCount(); e++)
            {
                switch (format.getElement(e).getUsage())
                {
                    case POSITION:
                        builder.put(e, v.getPos().x, v.getPos().y, v.getPos().z, v.getPos().w);
                        break;
                    case COLOR:
                        if (v.getMaterial() != null)
                            builder.put(e,
                                    v.getMaterial().getColor().x,
                                    v.getMaterial().getColor().y,
                                    v.getMaterial().getColor().z,
                                    v.getMaterial().getColor().w);
                        else
                            builder.put(e, 1, 1, 1, 1);
                        break;
                    case UV:
                        if (!v.hasTextureCoordinate())
                            builder.put(e,
                                    sprite.getInterpolatedU(defUV.u * 16),
                                    sprite.getInterpolatedV((model.customData.flipV ? 1 - defUV.v: defUV.v) * 16),
                                    0, 1);
                        else
                            builder.put(e,
                                    sprite.getInterpolatedU(v.getTextureCoordinate().u * 16),
                                    sprite.getInterpolatedV((model.customData.flipV ? 1 - v.getTextureCoordinate().v : v.getTextureCoordinate().v) * 16),
                                    0, 1);
                        break;
                    case NORMAL:
                        if (!v.hasNormal())
                            builder.put(e, faceNormal.x, faceNormal.y, faceNormal.z, 0);
                        else
                            builder.put(e, v.getNormal().x, v.getNormal().y, v.getNormal().z, 0);
                        break;
                    default:
                        builder.put(e);
                }
            }
        }
    }

}