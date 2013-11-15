package Data;


import Movable.objects.vehicle;
import Properties.Property;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author oivhe_000
 */


public class Data {
   
    
   public List<Property> lstProperties = new ArrayList<Property>();
   public List<vehicle> lstMObjects = new ArrayList<vehicle>();
    
    public Data(AssetManager manager){
   
   
   for (int i = 0; i <5 ; i++) {
        Property prop1 = new Property(manager);
      
        lstProperties.add(prop1);
     
   }
        
        
    }
    
}
