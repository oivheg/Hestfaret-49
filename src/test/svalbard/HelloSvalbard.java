/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.svalbard;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author oivhe_000
 */
public class HelloSvalbard extends SimpleApplication {

    public static void main (String[] args) {
        HelloSvalbard app = new HelloSvalbard();
        app.start(); // start the game
    }
    @Override
    public void simpleInitApp() {
         Box b1 = new Box(1, 1, 1);
        Geometry geom = new Geometry("blue cube", b1);
        geom.setLocalTranslation(new Vector3f(1,3,1));
        Material mat1 = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Red);
        geom.setMaterial(mat1);
        rootNode.attachChild(geom);
    }
    
}
