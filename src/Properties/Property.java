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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author oivhe_000
 */
public class Property  {
    
     private String adress = "DefaultAdress";
    private int width = 5, height = 5;
    private Building Buildings [];

public Spatial Property(AssetManager manager) {
    
   Quad q = new Quad(width, height);
   
   
   //skal refaktoreres
   Geometry quad = new Geometry("qube",q);
quad.setLocalTranslation(new Vector3f(1,-1,1));
Material mat1 = new Material(manager,
        "Common/MatDefs/Misc/Unshaded.j3md");
mat1.setColor("Color",ColorRGBA.Red);
         quad.setMaterial(mat1);
   // Ferdig skal refaktoreres
   
   
   quad.rotate(180, 0, 0);
    House house1 = new House();
  Node pivot = new Node("pivot");
  
  pivot.attachChild(quad);
  pivot.attachChild(house1.createHouse(manager));
        return  pivot;
}



}
