package Data;

import Objects.Objects;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oivhe_000
 */
public class Data {

    private List<Node> lstObjects = new ArrayList<Node>();
 Objects objects;
    public Data(AssetManager manager, int boxes, int spheres) {
      objects  = new Objects(manager);
        createObjects(boxes, spheres);
    }

    public void setPhysics() {
        for (int i = 0; i < lstObjects.size(); i++) {
            RigidBodyControl brick_phy = new RigidBodyControl(2f);
            lstObjects.get(i).getChild(0).addControl(brick_phy);
        }
    }

    public List<Node> getObjects() {
        return lstObjects;
    }
    
    public Geometry createOneBox(){
        return objects.createObject(1, ColorRGBA.Green, "box");
        
    }

    private void createObjects(int boxes, int spheres) {
        for (int i = 0; i < boxes; i++) {
            Node box = new Node("box" + i);
            box.attachChild(objects.createObject(i, ColorRGBA.Green, "box"));
            lstObjects.add(box);

           
        }
        for (int i = 0; i < spheres; i++) {
            Node sphere = new Node("sphere" + i);
            sphere.attachChild(objects.createObject(i, ColorRGBA.Blue, "sphere"));
            lstObjects.add(sphere);
        }
    }
}
