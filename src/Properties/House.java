/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;




 class House extends Building {


public  House(){
     
}



public Geometry createHouse(AssetManager manager, int x){
   
      Box box1 = new Box(1,1,1);
     Geometry blue = new Geometry("Box",box1);
blue.setLocalTranslation(new Vector3f(x,1f,0));
Material mat1 = new Material(manager,
        "Common/MatDefs/Misc/Unshaded.j3md");
mat1.setColor("Color",ColorRGBA.Blue);
         blue.setMaterial(mat1);
    
         return blue;
    
    
 
    
}

     
     
   
 }
    
    

   