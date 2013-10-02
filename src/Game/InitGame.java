/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Data.Data;
import Properties.Property;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import Properties.Property;

/**
 *
 * @author oivhe_000
 */
public class InitGame extends SimpleApplication implements ActionListener{

    
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
  private CharacterControl player;
  
  private Vector3f walkDirection = new Vector3f();
  private boolean left = false, right = false, up = false, down = false;
  private Vector3f camDir = new Vector3f();
  private Vector3f camLeft = new Vector3f();
    
  
  
    public static void main (String [] args){
        InitGame app = new InitGame();
         app.start();
    }

     
    @Override
    public void simpleInitApp() {
            Spatial scene = assetManager.loadModel("Scenes/Hestfaret49.j3o");
            Data data = new Data(assetManager);

        /** Set up Physics */
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
    
     // We re-use the flyby camera for rotation, while positioning is handled by physics
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    flyCam.setMoveSpeed(100);
    setUpKeys();

    Property props = new Property();

       CollisionShape sceneShape =
      CollisionShapeFactory.createMeshShape((Node) scene);
   
    landscape = new RigidBodyControl(sceneShape, 0);
    scene.addControl(landscape);
    //scene.setLocalScale(2f);
    
    
    //add stuff to scene
    
 
    rootNode.attachChild(scene);
  //  rootNode.attachChild(props.addHouse(assetManager));
  
    //rootNode.attachChild(props.getNode());
    
    
   int Length = data.lstProperties.size();
  
      
      
      for (int i = 0; i< Length; i++){
          Node h3 = new Node(""+i+"");
          h3.setLocalTranslation(10*i, 5, 5);
          h3.attachChild(data.lstProperties.get(i).getNode());
          
          rootNode.attachChild(h3);
          
          
      }
     
    //finished addint stuff to scene
    CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
    
    player = new CharacterControl(capsuleShape, 0.05f);
    player.setJumpSpeed(20);
    player.setFallSpeed(30);
    player.setGravity(30);
    player.setPhysicsLocation(new Vector3f(-10, 10, 10));
    
    bulletAppState.getPhysicsSpace().add(landscape);
    bulletAppState.getPhysicsSpace().add(player);
    
    
}  
    
    
    private void setUpKeys() {
        addMaping("Left", KeyInput.KEY_A);
        addMaping("Right",KeyInput.KEY_D);
        addMaping("Up",   KeyInput.KEY_W);
        addMaping("Down", KeyInput.KEY_S);
        addMaping("Jump", KeyInput.KEY_SPACE);

    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
  }

    public void onAction(String binding, boolean value, float tpf) {

    if (binding.equals("Left")) {
      if (value) { left = true; } else { left = false; }
    } else if (binding.equals("Right")) {
      if (value) { right = true; } else { right = false; }
    } else if (binding.equals("Up")) {
      if (value) { up = true; } else { up = false; }
    } else if (binding.equals("Down")) {
      if (value) { down = true; } else { down = false; }
    } else if (binding.equals("Jump")) {
      player.jump();
    }
    
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
    }

    private void addMaping(String Dir, int key) {
       
        inputManager.addMapping(Dir, new KeyTrigger(key));
    }
    
    
}
