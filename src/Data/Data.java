package Data;


import Properties.Property;
import com.jme3.asset.AssetManager;
import java.util.ArrayList;
import java.util.List;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author oivhe_000
 */
public class Data {
    
    
   public List<Property> lstProperties = new ArrayList<Property>();
   
    
    public Data(AssetManager manager){
   
   
   for (int i = 0; i <5 ; i++) {
        Property prop1 = new Property();
      prop1.Property(manager);
        lstProperties.add(prop1);
     
   }
        
        
    }
    
}
