/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import Moc.Mocs;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;




 class House extends Building {

     Geometry theHouse;
Mocs moc = new Mocs();
public  House(AssetManager manager, int x){
      
      
         theHouse = moc.createBox(manager, x);
}


public Geometry getGeometry() {
         return theHouse;
    
    
}
 public RigidBodyControl getphy() {
        return moc.getphy();
        
    }

     
     
   
 }
    
    

   