/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;



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

Node prop = new Node("Prop");
Property props = new Property(assetManager);

prop.attachChild(props.getNode());



rootNode.attachChild(prop);
    
    }
    
    
    
    
}
