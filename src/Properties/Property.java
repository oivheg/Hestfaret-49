/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author oivhe_000
 */
public class Property {
    
     private String adress = "DefaultAdress";
    private int width = 5, height = 5;
    private Building Buildings [];
    private Node pivot = new Node("pivot");
   public int posX = 0;
    
    public void addHouse(AssetManager manager) {
   Spatial house = manager.loadModel("Scenes/rooms/DemoRoom/Ogre3D/DemoRoom.j3o"); 
   pivot.attachChild(house);
}
    
  public Node getNode() {
      return pivot;
  }
    
public  Property(AssetManager manager) {
    
   Quad q = new Quad(width, height);
   
   
   //skal refaktoreres
   Geometry quad = new Geometry("qube",q);
   quad.setLocalTranslation(new Vector3f(-2,0,2));
   Material mat1 = new Material(manager,
        "Common/MatDefs/Misc/Unshaded.j3md");
   mat1.setColor("Color",ColorRGBA.Red);
         quad.setMaterial(mat1);
   // Ferdig skal refaktoreres
   
   
   quad.rotate(-FastMath.PI / 2, 0, 0);
 
    House house1 = new House(manager,posX);

 
  pivot.attachChild(house1.getGeometry());
  pivot.attachChild(quad);

        
}



}
