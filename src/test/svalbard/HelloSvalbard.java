/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.svalbard;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
        Box b = new Box(1,1,1) ; //create cube shape
        Geometry geom = new Geometry("Box", b); // create cube geometry from teh shape
        Material mat = new Material(assetManager,
                "common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }
    
}
