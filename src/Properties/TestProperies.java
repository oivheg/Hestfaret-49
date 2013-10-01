/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;



/**
 *
 * @author oivhe_000
 */
public class TestProperies  extends SimpleApplication  {

      public static void main (String[] args) {
     TestProperies app = new TestProperies();
      app.start();
    }
    
    @Override
    public void simpleInitApp( ) {
  
        flyCam.setMoveSpeed(100f);
Spatial scene = assetManager.loadModel("Scenes/Hestfaret49.j3o");
Node prop = new Node("Prop");
Property props = new Property();

prop.attachChild(props.Property(assetManager));
rootNode.attachChild(scene);
prop.setLocalTranslation(0,10,0.0f);
rootNode.attachChild(prop);
    
    }
    
    
    
    
}
