/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import Moc.Mocs;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;




 class House extends Building {

     Geometry theHouse;

public  House(AssetManager manager, int x){
      Mocs moc = new Mocs();
      
         theHouse = moc.createBox(manager, x);
}


public Geometry getGeometry() {
         return theHouse;
    
    
}

     
     
   
 }
    
    

   