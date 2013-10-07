package m2.loaders;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import utilities.World;

/**
 * Example that illustrates loading of Ogre DotScene files.
 *
 * @author thomasw
 */
public class LoaderExample extends SimpleApplication {

    private final static String FilePath = "Scenes/rooms/DemoRoom/Ogre3D/DemoRoom.scene";

    @Override
    public void simpleInitApp() {
        World.AddExamineCameraAppState(this, new Vector3f(0, 0, 20), 1);

        addLights();

/*        Node world = World.CreateSimpleWorld(this);
        world.setLocalTranslation(0, -4, 0);
        rootNode.attachChild(world);
*/
        // Load the model into a spatial.
        String path = FilePath;
        Spatial model = loadModel(path);

        // Traverse scene to disable face culling for the geometry.
        model.depthFirstTraversal(new SceneGraphVisitor() {

            @Override
            public void visit(Spatial spatial) {
                if (spatial instanceof Geometry) {
                    // Turn off face culling, so that we can see both sides.
                    ((Geometry) spatial).getMaterial().getAdditionalRenderState().
                            setFaceCullMode(RenderState.FaceCullMode.Off);
                }
            }
        });

        model.setLocalScale(1.0f);
        rootNode.attachChild(model);
    }

    private void addLights() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        rootNode.addLight(ambient);

        DirectionalLight diffuse = new DirectionalLight();
        diffuse.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f));
        diffuse.setDirection(new Vector3f(-1.0f, -1.0f, -1.0f));
        rootNode.addLight(diffuse);
    }

    private Spatial loadModel(String path) {
        // Load the model using the AssetManager.
        Spatial model = assetManager.loadModel(path);
        return model;
    }

    public static void main(String[] args) {
        LoaderExample app = new LoaderExample();
        app.start();
    }
}
