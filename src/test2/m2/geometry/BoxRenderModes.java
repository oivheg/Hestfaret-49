package m2.geometry;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import utilities.IndexedTriangleBox;
import utilities.OutlineBox;

/**
 * Example that illustrates four different rendering modes for rendering
 * boxes. The modes are normal, wireframe, indexed triangles, and a outline
 * method using lines.
 * 
 * Cycle between the render modes by pressing left ctrl.
 * 
 * @author thomasw
 */
public class BoxRenderModes extends SimpleApplication implements ActionListener {

    final static String ToggleRenderMode = "ToggleRenderMode";
    final static int NORMAL = 1;
    final static int WIREFRAME = 2;
    final static int INDEXED_TRIANGLE = 3;
    final static int OUTLINED = 4;
    Node[] nodes;
    int numRenderModes = 4;
    int idx = 0;

    @Override
    public void simpleInitApp() {
        // Register button for switching render modes. Left Control button.
        inputManager.addMapping(ToggleRenderMode, new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addListener(this, ToggleRenderMode);

        // A graph for each render mode.
        nodes = new Node[numRenderModes];
        nodes[0] = new Node("normal");
        nodes[1] = new Node("wireframe");
        nodes[2] = new Node("indexed_triangle");
        nodes[3] = new Node("outlined");
        
        addLights();
        createBoxes();

        rootNode.attachChild(nodes[idx]);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        // Cycle between render modes when pressing left ctrl key.
        if (name.equals(ToggleRenderMode)) {
            if (isPressed) {
                // Remove current box from its parent (rootNode).
                nodes[idx].removeFromParent();

                idx++;
                if (idx >= nodes.length) {
                    idx = 0;
                }

                System.out.println("Showing: " + nodes[idx].getName());
                rootNode.attachChild(nodes[idx]);
            }
        }
    }

    private void addLights() {
        // Set up ambient light.
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.Red.mult(0.2f));
        rootNode.addLight(ambient);

        // Set up directional light.
        DirectionalLight diffuse = new DirectionalLight();
        diffuse.setColor(ColorRGBA.Red.mult(0.8f));
        diffuse.setDirection(new Vector3f(-0.75f, -0.8f, -0.8f));
        rootNode.addLight(diffuse);

        // Set up directional light.
        DirectionalLight diffuse2 = new DirectionalLight();
        diffuse2.setColor(ColorRGBA.Red.mult(0.8f));
        diffuse2.setDirection(new Vector3f(0.8f, -0.8f, 0.8f));
        rootNode.addLight(diffuse2);
    }

    private void createBoxes() {
        // Create geometry to use for each render mode.
        Geometry normalBox = createBox(NORMAL);
        Geometry wireframeBox = createBox(WIREFRAME);
        Geometry indexedTriangleBox = createBox(INDEXED_TRIANGLE);
        Geometry outlinedBox = createBox(OUTLINED);

        // Attach each box to a node.
        nodes[0].attachChild(normalBox);
        nodes[1].attachChild(wireframeBox);
        nodes[2].attachChild(indexedTriangleBox);
        nodes[3].attachChild(outlinedBox);
    }

    private Geometry createBox(int renderMode) {
        Mesh mesh = null;
        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Ambient", ColorRGBA.Red);
        mat.setColor("Diffuse", ColorRGBA.Red);
        mat.setBoolean("UseMaterialColors", true);

        switch (renderMode) {
            case NORMAL:
                // Mesh using the built in box method in jME3.
                mesh = createNormalBox();
                break;

            case WIREFRAME:
                // Mesh using the built in box method in jME3, with render mode
                // set to wireframe.
                mesh = createNormalBox();
                mat = new Material(assetManager,
                        "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setColor("Color", ColorRGBA.Red);
                mat.getAdditionalRenderState().setWireframe(true);
                break;

            case INDEXED_TRIANGLE:
                // Mesh using indexed triangles.
                mesh = createIndexedTriangleBox();
                break;

            case OUTLINED:
                // Mesh using lines forming quads, creating a "outlined holo" effect.
                mesh = createOutlinedBox();
                mat = new Material(assetManager,
                        "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setColor("Color", ColorRGBA.Red);
                break;
        }

        // Set mesh to geometry, add material and scale.
        Geometry geom = new Geometry("Box", mesh);
        geom.setMaterial(mat);
        geom.setLocalScale(2.5f);

        return geom;
    }

    public Mesh createNormalBox() {
        Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);
        return mesh;
    }

    public Mesh createIndexedTriangleBox() {
        IndexedTriangleBox mesh = new IndexedTriangleBox(2, true, false);
        return mesh;
    }

    public Mesh createOutlinedBox() {
        OutlineBox mesh = new OutlineBox(2);
        return mesh;
    }

    public static void main(String[] args) {
        BoxRenderModes app = new BoxRenderModes();
        app.start();
    }
}
