package Data;


import Movable.objects.vehicle;
import Objects.Objects;
import Properties.Property;
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
   
    
//   public List<Property> lstProperties = new ArrayList<Property>();
//   public List<vehicle> lstMObjects = new ArrayList<vehicle>();
   private List<Geometry> lstObjects = new ArrayList<Geometry>();
    
    public Data(AssetManager manager, int boxes, int spheres){
   Objects objects = new Objects(manager);
        for (int i = 0; i < boxes; i++){
            
          
           lstObjects.add(objects.createObject(i, ColorRGBA.Blue, "box"));
        }
         for (int i = 0; i < spheres; i++){
             Node sphere = new Node("sphere"+i);
         
            lstObjects.add(objects.createObject(i, ColorRGBA.Blue, "sphere"));
        }
         
   
//   for (int i = 0; i <5 ; i++) {
//        Property prop1 = new Property(manager);
//      
//        lstProperties.add(prop1);
//     
//   }
        
        
    }
     public void setPhysics() {
        for (int i = 0; i < lstObjects.size() ; i++) {
          RigidBodyControl  brick_phy = new RigidBodyControl(2f);
            lstObjects.get(i).addControl(brick_phy);
        }
    }
    public List<Geometry> getObjects()  {
        return lstObjects;
    }
   
}
