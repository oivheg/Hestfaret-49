package m2.appearance;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture;
import utilities.Pyramid;
import utilities.RotationControl;
import utilities.World;

/**
 * Example that demonstrates various Material properties.
 *
 * @author thomasw
 */
public class MaterialExample extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        World.AddExamineCameraAppState(this, new Vector3f(0, 0, 10), 1);

        addLights();
        createBoxes();
    }

    private void addLights() {
        // set background color to light blue.
        viewPort.setBackgroundColor(new ColorRGBA(0.0f, 0.5f, 1.0f, 1.0f));

        // Set up ambient light.
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f));
        rootNode.addLight(ambient);

        // Set up directional light.
        DirectionalLight diffuse = new DirectionalLight();
        diffuse.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f));
        diffuse.setDirection(new Vector3f(-1.0f, -1.0f, -1.0f));
        rootNode.addLight(diffuse);
    }

    private void createBoxes() {
        Vector3f loc = new Vector3f();
        Mesh mesh = new Pyramid(1, 1);

        for (int i = 0, idx = 0; i < 3; i++) {
            float ypos = (i - 1) * 1.9f;
            for (int j = 0; j < 3; j++, idx++) {
                float xpos = (j - 1) * 2.1f;
                createBox(mesh.clone(), loc.set(xpos, ypos, 0), idx);
            }
        }
    }

    private void createBox(Mesh mesh, Vector3f loc, int idx) {
        Geometry geom = null;

        switch (idx) {
            case 0:
                geom = createUnshadedBox(mesh);
                break;
            case 1:
                geom = createUnshadedWireframeBox(mesh);
                break;
            case 2:
                geom = createUnshadedPointsBox(mesh);
                break;
            case 3:
                geom = createSolidLitBox(mesh);
                break;
            case 4:
                geom = createTexturedBox(mesh);
                break;
            case 5:
                geom = createTransparentBox(mesh);
                break;
            case 6:
                geom = createSolidLitNoSpecularBox(mesh);
                break;
            case 7:
                geom = createSolidLitOnlySpecularBox(mesh);
                break;
            case 8:
                geom = createAnotherSolidLitBox(mesh);
                break;
        }

        geom.setLocalTranslation(loc);
        Control control = new RotationControl();
        geom.addControl(control);
        rootNode.attachChild(geom);
    }

    private Geometry createUnshadedBox(Mesh mesh) {
        // Unshaded box with just a color.
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1.0f, 0.2f, 0.4f, 1.0f));
        Geometry geom = new Geometry("unshaded", mesh);
        geom.setMaterial(mat);

        return geom;
    }

    private Geometry createUnshadedWireframeBox(Mesh mesh) {
        // Box rendered as wireframe, shows lines along each triangle.

        // There are two ways to set wireframe:
        // 1): Set mesh to Mesh.Mode.Lines mode
        mesh.setMode(Mesh.Mode.Lines);

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1.0f, 0.2f, 0.4f, 1.0f));

        // 2): Set renderstate for the material to wireframe, and disable
        //     face culling.
//        mat.getAdditionalRenderState().setWireframe(true);
//        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);

        Geometry geom = new Geometry("unshaded_wireframe", mesh);
        geom.setMaterial(mat);

        return geom;
    }

    private Geometry createUnshadedPointsBox(Mesh mesh) {
        // Box rendered as points, with one point at each vertex.
        mesh.setMode(Mesh.Mode.Points);
        mesh.setPointSize(mesh.getPointSize() * 4);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1.0f, 0.2f, 0.4f, 1.0f));
        Geometry geom = new Geometry("unshaded_points", mesh);
        geom.setMaterial(mat);

        return geom;
    }

    private Geometry createSolidLitBox(Mesh mesh) {
        // Box rendered with lighting.
        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", new ColorRGBA(0.8f, 0.0f, 0.0f, 1.0f));
        mat.setColor("Diffuse", new ColorRGBA(0.8f, 0.0f, 0.0f, 1.0f));
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 80.0f);
        Geometry geom = new Geometry("solidlit", mesh);
        geom.setMaterial(mat);

        return geom;
    }

    private Geometry createTexturedBox(Mesh mesh) {
        // Box rendered with a texture.
        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Ambient", ColorRGBA.White);
        mat.setColor("Diffuse", ColorRGBA.Black);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 1.0f);
        // Load and set texture.
        Texture tex = assetManager.loadTexture("Textures/rocks.jpg");
        mat.setTexture("DiffuseMap", tex);
        Geometry geom = new Geometry("textured", mesh);
        geom.setMaterial(mat);

        return geom;
    }

    private Geometry createTransparentBox(Mesh mesh) {
        // Box rendered with transparency, using alpha blending.
        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", new ColorRGBA(0.7f, 0.8f, 1.0f, 0.6f));
        mat.setColor("Diffuse", new ColorRGBA(0.7f, 0.8f, 1.0f, 0.6f));
        mat.setColor("Specular", new ColorRGBA(0.0f, 0.0f, 0.0f, 0.6f));
        mat.setFloat("Shininess", 1.0f);
        // Set render state for material to use the alpha channel.
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        Geometry geom = new Geometry("transparent", mesh);
        geom.setMaterial(mat);
        // Add the geometry to the transparent render bucket, to allow correct
        // transparency sorting.
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        return geom;
    }

    private Geometry createSolidLitNoSpecularBox(Mesh mesh) {
        // Box rendered with lighting, but no specular.
        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", new ColorRGBA(0.0f, 0.0f, 0.8f, 1.0f));
        mat.setColor("Diffuse", new ColorRGBA(0.0f, 0.0f, 0.8f, 1.0f));
        mat.setColor("Specular", ColorRGBA.Black);
        mat.setFloat("Shininess", 80.0f);
        Geometry geom = new Geometry("solidlit", mesh);
        geom.setMaterial(mat);

        return geom;
    }

    private Geometry createSolidLitOnlySpecularBox(Mesh mesh) {
        // Box rendered with lighting, but only specular.
        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Black);
        mat.setColor("Diffuse", ColorRGBA.Black);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 80.0f);
        Geometry geom = new Geometry("solidlit", mesh);
        geom.setMaterial(mat);

        return geom;
    }

    private Geometry createAnotherSolidLitBox(Mesh mesh) {
        // Box renered with lighting.
        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", new ColorRGBA(0.8f, 0.8f, 0.0f, 1.0f));
        mat.setColor("Diffuse", new ColorRGBA(0.8f, 0.8f, 0.0f, 1.0f));
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 80.0f);
        Geometry geom = new Geometry("solidlit", mesh);
        geom.setMaterial(mat);

        return geom;
    }

    public static void main(String[] args) {
        MaterialExample app = new MaterialExample();
        app.start();
    }
}
