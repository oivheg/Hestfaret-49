/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Data.Data;
import Effects.Fire;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;


/**
 *
 * @author oivhe_000
 */
public class StartGame implements ActionListener{
    
  public BulletAppState bulletAppState;
  public RigidBodyControl landscape;
  public CharacterControl player;
  public InputManager inputManager;
  public FlyByCamera flyCam;
  public Camera cam;
  Node root = new Node("root");
  AssetManager m;
  private Vector3f walkDirection = new Vector3f();
  private boolean left = false, right = false, up = false, down = false;
  private Vector3f camDir = new Vector3f();
  private Vector3f camLeft = new Vector3f();
    
    public void init(AssetManager manager,AppStateManager stateManager,ViewPort viewPort,FlyByCamera fly,InputManager im,Camera c) {
        m = manager;
        inputManager = im;
        flyCam = fly;
        cam = c;
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(0, 0, 1));
        root.addLight(dl);
         Data data = new Data(m);
         
         bulletAppState = new BulletAppState();
         stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().enableDebug(m);
        
        // We re-use the flyby camera for rotation, while positioning is handled by physics
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
   flyCam.setMoveSpeed(100);
    setUpKeys();
       Spatial scene = m.loadModel("Scenes/Scene1.j3o");
     CollisionShape sceneShape =
      CollisionShapeFactory.createMeshShape((Node) scene);
   
    landscape = new RigidBodyControl(sceneShape, 0);
    scene.addControl(landscape);
    
     
        root.attachChild(scene);
        
        
        int Length = data.lstProperties.size();
  
      
      
      for (int i = 0; i< Length; i++){
          Node h3 = new Node(""+i+"");
          h3.setLocalTranslation(10*i, 5, 5);
          h3.attachChild(data.lstProperties.get(i).getNode());
          
          root.attachChild(h3);
          
          
      }
      // fire
      
      Fire fire = new Fire(m);
      Node f = new Node("fire");
      f.attachChild(fire.GetFire());
      f.attachChild(fire.GetDebris());
      f.setLocalTranslation(5, 0, 0);
      root.attachChild(f);
   
      
    // end fire
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
    
    public Node getRoot(){
        return root;
    }
    
public void createBox(){
    Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(m, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        root.attachChild(geom);

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
    }else if (binding.equals("view")) {
        if (value) {view = true;}
       
          
       
    
    }
         
         
        
    }
boolean view;
    private void setUpKeys() {
        addMaping("Left", KeyInput.KEY_A);
        addMaping("Right",KeyInput.KEY_D);
        addMaping("Up",   KeyInput.KEY_W);
        addMaping("Down", KeyInput.KEY_S);
        addMaping("Jump", KeyInput.KEY_SPACE);
        addMaping("view", KeyInput.KEY_Z);

    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
    inputManager.addListener(this, "view");
    
    }
    
     private void addMaping(String Dir, int key) {
       
        inputManager.addMapping(Dir, new KeyTrigger(key));
    }
    
     public void updatePos(float tpf) {
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
        if (view){
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
        }
    }
}
