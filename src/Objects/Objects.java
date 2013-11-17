/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author oivhe_000
 */
public class Objects {
    AssetManager manager;
    public Objects (AssetManager AM){
        manager = AM;
    }

    public Geometry createObject(int x, ColorRGBA color, String geometry) {
        Geometry geom = null;
        if (geometry.equals("box")) {
            Box box1 = new Box(1, 1, 1);
            geom = new Geometry("Box", box1);

        } else if (geometry.equals("sphere")) {
            Sphere sphereMesh = new Sphere(32, 32, 2f);
            geom = new Geometry("Shiny rock", sphereMesh);
        }
        geom.setLocalTranslation(new Vector3f(x, 1f, 0));
        Material mat1 = new Material(manager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", color);
        geom.setMaterial(mat1);
        if (geom != null) {
            return geom;
        }else {
            return null;
        }
        
    }

  
}
