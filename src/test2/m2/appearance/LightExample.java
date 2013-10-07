package m2.appearance;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
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
 * Example that demonstrates the different light types in jME. Also illustrates
 * how you can set which lights influence which geometry by adding them to
 * specific subgraphs instead of adding them directly to the root node. 
 *
 * @author thomasw
 */
public class LightExample extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        World.AddExamineCameraAppState(this, new Vector3f(0, 0, 10), 1);
        
        Node nodeWithLight = new Node();
        rootNode.attachChild(nodeWithLight);

        Node nodeWithoutLight = new Node();
        rootNode.attachChild(nodeWithoutLight);

        AmbientLight aLight = new AmbientLight();
        aLight.setColor(new ColorRGBA(1, 1, 1, 1));

        DirectionalLight dLight = new DirectionalLight();
        dLight.setColor(new ColorRGBA(1, 1, 1, 1));
        dLight.setDirection(new Vector3f(0, -1, 1));
        
        PointLight pLight = new PointLight();
        pLight.setColor(new ColorRGBA(1, 1, 1, 1));
        pLight.setPosition(new Vector3f(3, 2, 0));
        
        SpotLight sLight = new SpotLight();
        sLight.setColor(new ColorRGBA(1, 1, 1, 1));
        sLight.setDirection(new Vector3f(1, 0, -1));
        sLight.setPosition(new Vector3f(-10, 5, 10));
        sLight.setSpotInnerAngle(75 * FastMath.DEG_TO_RAD);
        sLight.setSpotOuterAngle(90 * FastMath.DEG_TO_RAD);

        // Add the ambient light to the root node, will influence everything.
        rootNode.addLight(aLight);
        // Add the directional, point, and spot lights only to the "nodeWithLight"
        // node, so that only this subgraph will be influenced.
        nodeWithLight.addLight(sLight);
        nodeWithLight.addLight(pLight);
        nodeWithLight.addLight(dLight);
        
        Mesh sphereMesh = new Sphere(50, 50, 1);

        Material lightingMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        lightingMat.setColor("Ambient", ColorRGBA.Blue.mult(0.2f));
        lightingMat.setColor("Diffuse", ColorRGBA.Blue.mult(0.6f));
        lightingMat.setColor("Specular", ColorRGBA.Blue);
        lightingMat.setFloat("Shininess", 16f);
        lightingMat.setBoolean("UseMaterialColors", true);

        // This Geometry is added to the subgraph that has no lights added to
        // it (only influenced by the ambient light added to the root).
        Geometry geom0 = new Geometry("geom0", sphereMesh);
        geom0.setMaterial(lightingMat);
        geom0.setLocalTranslation(-1, 0, 0);
        nodeWithoutLight.attachChild(geom0);
        
        // This Geometry is added to the subgraph that has lights.
        Geometry geom1 = new Geometry("geom1", sphereMesh);
        geom1.setMaterial(lightingMat);
        geom1.setLocalTranslation(1, 0, 0);
        nodeWithLight.attachChild(geom1);

        // This Geometry is added to the subgraph that has lights.
        Geometry geom2 = new Geometry("geom2", sphereMesh);
        // Clone material and make it green.
        Material lightingMat2 = lightingMat.clone();
        lightingMat2.setColor("Ambient", ColorRGBA.Green.mult(0.2f));
        lightingMat2.setColor("Diffuse", ColorRGBA.Green.mult(0.6f));
        lightingMat2.setColor("Specular", ColorRGBA.Green);
        geom2.setMaterial(lightingMat2);
        geom2.setLocalTranslation(1, 2, 0);
        nodeWithLight.attachChild(geom2);

//        // This Geometry is added to the subgraph that has lights.
//        Geometry geom3 = new Geometry("geom3", sphereMesh);
//        // Clone material, and set it to not use material colors.
//        Material lightingMat3 = lightingMat.clone();
//        lightingMat3.setBoolean("UseMaterialColors", false);
//        geom3.setMaterial(lightingMat3);
//        geom3.setLocalTranslation(1, 4, 0);
//        nodeWithLight.attachChild(geom3);

        // Add simple debug world around.
        Node world = World.CreateSimpleWorld(this);
        rootNode.attachChild(world);
    }

    public static void main(String[] args) {
        LightExample app = new LightExample();
        app.start();
    }
}
