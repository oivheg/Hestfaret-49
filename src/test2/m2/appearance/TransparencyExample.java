package m2.appearance;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.*;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import utilities.World;

/**
 * Example that demonstrates the different types of transparency that is
 * possible in jME.
 * 
 * Textures by Yorik, obtained here: http://blender-archi.tuxfamily.org/Greenhouse
 *
 * @author thomasw
 */
public class TransparencyExample extends SimpleApplication {

    Texture palmTexture;
    Texture plantTexture;
    Vector3f currLoc = new Vector3f(0, 0, 0);
    float spacing = 17f;

    @Override
    public void simpleInitApp() {
        World.AddExamineCameraAppState(this, new Vector3f(0, 0, 20), 3);

        // attach the color cube to the scene graph.
        Node node = new Node();
        rootNode.attachChild(node);

        AmbientLight aLight = new AmbientLight();
        aLight.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(aLight);

        // Directional light that shines towards negative z-axis.
        DirectionalLight dLight = new DirectionalLight();
        dLight.setDirection(Vector3f.UNIT_Z.mult(-1));
        rootNode.addLight(dLight);

        // Add a world.
        Node world = World.CreateSimpleWorld(this);
        world.setLocalTranslation(0, -4, 0);
        rootNode.attachChild(world);

        // Load palm texture with alpha channel.
        palmTexture = assetManager.loadTexture("Textures/palm.png");
        plantTexture = assetManager.loadTexture("Textures/Ginger.png");

        // Create a quad mesh.
        Mesh quadMesh = createQuadMesh(15f, 15f);

        // Opaque, BlendMode.Off
        Geometry opaque0 = createGeomWMaterial(quadMesh);
        opaque0.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.Off);
        node.attachChild(opaque0);

        // Transparent, BlendMode.Alpha
        Geometry alphaBlended0 = createGeomWMaterial(quadMesh);
        alphaBlended0.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.Alpha);
        node.attachChild(alphaBlended0);
        Geometry alphaBlended1 = createGeomWMaterial(quadMesh);
        alphaBlended1.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.Alpha);
        alphaBlended1.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.attachChild(alphaBlended1);

        // Transparent, BlendMode.Additive - disregards ordering
        Geometry addativeBlended0 = createGeomWMaterial(quadMesh);
        addativeBlended0.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.Additive);
        addativeBlended0.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.attachChild(addativeBlended0);

        // AlphaAdditive
        Geometry alphaAddativeBlended0 = createGeomWMaterial(quadMesh);
        alphaAddativeBlended0.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.AlphaAdditive);
        alphaAddativeBlended0.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.attachChild(alphaAddativeBlended0);
        
        // Modulate
        Geometry modulateBlended0 = createGeomWMaterial(quadMesh);
        modulateBlended0.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.Modulate);
        modulateBlended0.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.attachChild(modulateBlended0);

        // ModulateX2
        Geometry modulateX2Blended0 = createGeomWMaterial(quadMesh);
        modulateX2Blended0.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.ModulateX2);
        modulateX2Blended0.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.attachChild(modulateX2Blended0);

        // PremultAlpha
        Geometry premultAlphaBlended0 = createGeomWMaterial(quadMesh);
        premultAlphaBlended0.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.PremultAlpha);
        premultAlphaBlended0.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.attachChild(premultAlphaBlended0);

        // Color
        Geometry colorBlended0 = createGeomWMaterial(quadMesh);
        colorBlended0.getMaterial().getAdditionalRenderState().setBlendMode(
                RenderState.BlendMode.Color);
        colorBlended0.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.attachChild(colorBlended0);

        // traverse scene to disable face culling for the geometry.
        node.depthFirstTraversal(new SceneGraphVisitor() {

            @Override
            public void visit(Spatial spatial) {
                System.out.println("spatial.name = " + spatial);
                if (spatial instanceof Geometry) {
                    // turn off face culling, so that we can see both sides.
                    ((Geometry) spatial).getMaterial().getAdditionalRenderState().
                            setFaceCullMode(RenderState.FaceCullMode.Off);
                }
            }
        });
        
        Node nodeCloned = node.clone(true);
        // traverse scene to disable face culling for the geometry.
        nodeCloned.depthFirstTraversal(new SceneGraphVisitor() {

            @Override
            public void visit(Spatial spatial) {
                System.out.println("spatial.name = " + spatial);
                if (spatial instanceof Geometry) {
                    // turn off face culling, so that we can see both sides.
                    ((Geometry) spatial).getMaterial().setTexture("DiffuseMap", plantTexture);
                }
            }
        });
        
        nodeCloned.setLocalTranslation(new Vector3f(0, 0, -20));
        rootNode.attachChild(nodeCloned);
        
        // Add a series of debug spheres behind the quads.
        Node debugSpheres = createDebugSpheres();
        node.attachChild(debugSpheres);
    }
    
    /**
     * Creates a quad mesh with center in the middle of the quad.
     * @param x Total widht of the quad.
     * @param y Total height of the quad.
     * @return Mesh for the quad.
     */
    private Mesh createQuadMesh(float x, float y) {
        Mesh mesh = new Mesh();
        float halfX = x / 2f;
        float halfY = y / 2f;
        float[] coords = new float[]{
            -halfX, halfY, 0.0f,
            -halfX, -halfY, 0.0f,
            halfX, -halfY, 0.0f,
            halfX, halfY, 0.0f,};

        float[] texcoords = new float[]{
            0, 1,
            0, 0,
            1, 0,
            1, 1,};

        float[] normals = new float[]{
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,};

        int[] indexes = new int[]{
            1, 2, 0,
            2, 3, 0,};

        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(coords));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texcoords));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));
        mesh.updateBound();

        return mesh;
    }

    /**
     * Creates Geometry based on the mesh, preloaded with the alpha texture and
     * material set to use alpha channel. The geometry is shifted along the
     * x-axis according to the location of the previous quad.
     * @param mesh The mesh to construct the Geometry from
     * @return The Geometry.
     */
    private Geometry createGeomWMaterial(Mesh mesh) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", palmTexture);
        mat.setBoolean("UseAlpha", true);

        Geometry geom = new Geometry("geom", mesh);
        geom.setMaterial(mat);
        geom.setLocalTranslation(currLoc);
        currLoc.x += spacing;

        return geom;
    }

    /**
     * Creates a series of debug spheres along one axis.
     * @return The debug spheres contained in a Node.
     */
    private Node createDebugSpheres() {
        Node node = new Node("debugSpheres");

        Mesh mesh = new Sphere(50, 50, 1);
        Material opaqueMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        opaqueMat.setColor("Ambient", new ColorRGBA(0.0f, 0.0f, 0.3f, 1.0f));
        opaqueMat.setColor("Diffuse", new ColorRGBA(0.0f, 0.0f, 0.6f, 1.0f));
        opaqueMat.setColor("Specular", new ColorRGBA(0.0f, 0.0f, 1.0f, 1.0f));
        opaqueMat.setFloat("Shininess", 16f);
        opaqueMat.setBoolean("UseMaterialColors", true);
        opaqueMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Off);

        Geometry opaqueSphere = new Geometry("opaqueSphere", mesh);
        opaqueSphere.setMaterial(opaqueMat);

        Vector3f loc = new Vector3f(0, 0, -34);
        for (int i = 0; i < 100; i++) {
            Spatial clone = opaqueSphere.clone();
            clone.setLocalTranslation(loc);
            node.attachChild(clone);
            loc.x += 2;
        }

        return node;
    }

    public static void main(String[] args) {
        TransparencyExample app = new TransparencyExample();
        app.start();
    }
}
