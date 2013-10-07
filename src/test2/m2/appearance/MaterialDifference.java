package m2.appearance;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import utilities.World;

/**
 * Example that demonstrates loading material definitions, parameters that
 * can modify the material, light (ambient and spot), and the difference
 * between an unshaded and a shaded (influenced by light) sphere.
 *
 * @author thomasw
 */
public class MaterialDifference extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        World.AddExamineCameraAppState(this, new Vector3f(0, 0, 20), 3);
        
        // Add a node below the root, with a scale of 4.
        Node node = new Node();
        node.setLocalScale(4);
        rootNode.attachChild(node);

        // Ambient light, with a dark white color.
        AmbientLight aLight = new AmbientLight();
        aLight.setColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 0.2f));
        rootNode.addLight(aLight);

        // Spot light, with a white color.
        SpotLight sLight = new SpotLight();
        sLight.setColor(new ColorRGBA(1, 1, 1, 1));
        sLight.setDirection(new Vector3f(1, 0, -1));
        sLight.setPosition(new Vector3f(-10, 5, 10));
        sLight.setSpotInnerAngle(75 * FastMath.DEG_TO_RAD);
        sLight.setSpotOuterAngle(90 * FastMath.DEG_TO_RAD);
        rootNode.addLight(sLight);

        // Create a mesh for the spheres.
        Mesh sphereMesh = new Sphere(50, 50, 1);

        // Blue unshaded sphere, located to the left.
        Geometry unshadedSphere = new Geometry("unshadedSphere", sphereMesh);
        Material unshadedMat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        unshadedMat.setColor("Color", ColorRGBA.Blue);
        unshadedSphere.setMaterial(unshadedMat);
        unshadedSphere.setLocalTranslation(-1, 0, -2);

        // Blue shaded sphere, located to the right.
        Geometry shadedSphere = new Geometry("shadedSphere", sphereMesh);
        Material lightingMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        lightingMat.setColor("Ambient", ColorRGBA.Blue.mult(0.2f));
        lightingMat.setColor("Diffuse", ColorRGBA.Blue.mult(0.6f));
        lightingMat.setColor("Specular", ColorRGBA.Blue);
        lightingMat.setFloat("Shininess", 16f);
        lightingMat.setBoolean("UseMaterialColors", true);
        shadedSphere.setMaterial(lightingMat);
        shadedSphere.setLocalTranslation(1, 0, -2);

        node.attachChild(unshadedSphere);
        node.attachChild(shadedSphere);

        // Add a world around, to make everything nicer.
        Node world = World.CreateSimpleWorld(this);
        rootNode.attachChild(world);
    }

    public static void main(String[] args) {
        MaterialDifference app = new MaterialDifference();
        app.start();
    }
}
