package utilities;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

/**
 *
 * @author thomasw
 */
public class World {

    /**
     * Adds an ExamineCameraAppState to the SimpleApplication. Disables the standard
     * FlyByCamera in SimpleApplication.
     * @param application The SimpleApplication.
     * @param location The start location for the ExamineCamera.
     * @param sensitivity The sensitivity for the ExamineCamera.
     */
    public static void AddExamineCameraAppState(SimpleApplication application,
            Vector3f location, float sensitivity) {
        // Disable the first person camera that is used in SimpleApplication
        // by default.
        application.getFlyByCamera().setEnabled(false);

        // Create ExamineCamera, second parameter gives camera start location.
        // Default lookat point for the camera is (0, 0, 0).
        ExamineCamera examineCam = new ExamineCamera(application.getCamera(), 
                location);
        // Set sensitivity for the camera.
        examineCam.setSensitivity(sensitivity);

        // Create app state, and set the ExamineCamera.
        ExamineCameraAppState camAppState = new ExamineCameraAppState();
        camAppState.setCamera(examineCam);

        // Add the app state to the application.
        application.getStateManager().attach(camAppState);
    }
    
    /**
     * Adds a simple debug world to the scene. Containing a ground plane, and
     * a skybox.
     * @param app The Application, necessary for loading assets.
     * @return Node containing the world.
     */
    public static Node CreateSimpleWorld(Application app) {
        AssetManager assetManager = app.getAssetManager();

        Node world = new Node("SimpleWorld");

        Spatial sky = SkyFactory.createSky(assetManager,
                "Textures/Sky/Bright/BrightSky.dds", false);
        world.attachChild(sky);

        Texture groundTex = assetManager.loadTexture("Textures/GravelCobble.jpg");
        groundTex.setWrap(Texture.WrapMode.Repeat);

        Material groundMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        groundMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        groundMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
        groundMat.setColor("Specular", ColorRGBA.White.mult(0.65f));
        groundMat.setBoolean("UseMaterialColors", true);
        groundMat.setTexture("DiffuseMap", groundTex);

        Mesh groundMesh = new Box(1, 1, 1);
        groundMesh.scaleTextureCoordinates(new Vector2f(100, 100));

        Geometry ground = new Geometry("ground", groundMesh);
        ground.setMaterial(groundMat);
        ground.setLocalTranslation(0, -4.2f, 0);
        ground.setLocalScale(1000, 0.2f, 1000);
        world.attachChild(ground);

        return world;
    }
}
