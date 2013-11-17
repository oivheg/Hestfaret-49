/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Moc;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author oivhe_000
 */
public class Mocs {

    private RigidBodyControl brick_phy;

    public Geometry createBox(AssetManager manager, int x) {

        Box box1 = new Box(1, 1, 1);
        Geometry box = new Geometry("Box", box1);
        box.setLocalTranslation(new Vector3f(x, 1f, 0));

        Material mat1 = new Material(manager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        box.setMaterial(mat1);
        brick_phy = new RigidBodyControl(2f);
        box.addControl(brick_phy);
        return box;
    }
    public RigidBodyControl getphy() {
        return brick_phy;

    }
}
