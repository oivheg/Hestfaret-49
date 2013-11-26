/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Data.Data;
import Effects.Fire;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
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
import com.jme3.util.SkyFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oivhe_000
 */
public class StartGame implements ActionListener, AnimEventListener {

    public BulletAppState bulletAppState;
    private RigidBodyControl landscape, objects;
    private CharacterControl player, collector;
    private InputManager inputManager;
    private FlyByCamera flyCam;
    private Camera cam1;
    private Node root = new Node("root");
    private AssetManager assetM;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    private Vector3f camDir = new Vector3f(), camLeft = new Vector3f(), camLocation = new Vector3f(), sunDir = new Vector3f();
    private String clickedname;
    private DirectionalLight sun;
    private Data data;
    int NumberGeometries;

    public StartGame(AssetManager manager, AppStateManager stateManager, ViewPort viewPort, FlyByCamera fly, InputManager im, Camera c) {
        assetM = manager;
        inputManager = im;
        flyCam = fly;
        cam1 = c;
        flyCam.setMoveSpeed(100);

        data = new Data(assetM, 3, 4);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().enableDebug(assetM);
        clickedname = "testing";

        root.attachChild(SkyFactory.createSky(
                assetM, "Textures/Sky/Bright/BrightSky.dds", false));
//        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        configureSun();
        configureScene();
        addObjects();
        //fire
        addFire();
        Physics();
        createCollector();
        setPickedGeom();
        //finished addint content to scene
    }

    public void setVectors(Vector3f lcation) {
        camLocation = lcation;
    }

    public Node getRoot() {
        return root;
    }

    public void sunDir() {
        sunDir.x--;
        sun.setDirection(sunDir.normalizeLocal());
    }

    public CharacterControl getPlayer() {
        return player;
    }
    public boolean isplayer;

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Left")) {
            if (value) {
                left = true;
            } else {
                left = false;
            }
        } else if (binding.equals("Right")) {
            if (value) {
                right = true;
            } else {
                right = false;
            }
        } else if (binding.equals("Up")) {
            if (value) {
                up = true;
            } else {
                up = false;
            }
        } else if (binding.equals("Down")) {
            if (value) {
                down = true;
            } else {
                down = false;
            }
        } else if (binding.equals("Jump")) {
            player.jump();
        } else if (binding.equals("view")) {
            flyCam.setEnabled(true);
            isplayer = true;
            cam1.setLocation(camLocation);
            flyCam.setDragToRotate(false);
        }
    }
    boolean view;
    boolean test;
    int counter = 0;

    public void setUpKeys() {

        addMaping("Left", KeyInput.KEY_A);
        addMaping("Right", KeyInput.KEY_D);
        addMaping("Up", KeyInput.KEY_W);
        addMaping("Down", KeyInput.KEY_S);
        addMaping("Jump", KeyInput.KEY_SPACE);
        addMaping("view", KeyInput.KEY_C);

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
        camDir.set(cam1.getDirection()).multLocal(0.6f);
        camLeft.set(cam1.getLeft()).multLocal(0.4f);
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
        if (view) {
//        player.setWalkDirection(walkDirection);
//        cam.setLocation(player.getPhysicsLocation());
//        flyCam.setDragToRotate(true);
        }
        player.setWalkDirection(walkDirection);
    }

    public String GetName() {
        return clickedname;
    }

    private void addObjects() {

        int Length = data.getObjects().size();
        for (int i = 0; i < Length; i++) {

            data.getObjects().get(i).setLocalTranslation(10 * i, 5, 5);

            root.attachChild(data.getObjects().get(i));
        }

    }

    private void addFire() {
        // fire
        Fire fire = new Fire(assetM);
        Node f = new Node("fire");
        f.attachChild(fire.getFire());
        f.attachChild(fire.getDebris());
        f.setLocalTranslation(10, 7, 5);
        root.attachChild(f);
    }

    private void Physics() {
        data.setPhysics();
        setPlayerPhy();
        setCollectorPhy();
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
        bulletAppState.getPhysicsSpace().add(collector);
        for (int i = 0; i < data.getObjects().size(); i++) {
            bulletAppState.getPhysicsSpace().add(data.getObjects().get(i).getChild(0));
        }
        bulletAppState.setDebugEnabled(false);
    }

    private void setCollectorPhy() {

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        collector = new CharacterControl(capsuleShape, 0.05f);
        collector.setJumpSpeed(20);
        collector.setFallSpeed(30);
        collector.setGravity(30);

    }

    private void setPlayerPhy() {
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(-10, 10, 10));
    }

    public void changeDay() {
        sun.setColor(ColorRGBA.Blue);
    }
    private AnimChannel channel;
    private AnimChannel pickChannel;
    private AnimControl control;
    Node collectorNode;
    boolean pickUp;

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

        if (!shoudMove && animName.equals("Walk")) {

            if (!haveItem) {
                channel.setAnim("Dodge", 1f);
                pickUp = true;
                channel.setSpeed(0.1f);
                pickChannel.setAnim("walkGun", 1f);

            } else {

                haveItem = false;
                channel.setAnim("stand", 1f);
                pickChannel.reset(true);
               
                
//               
                restart = true;
                System.out.println("LAST");
//                removeGeometry = true;
                
                 removeGeom();
            }
            channel.setLoopMode(LoopMode.DontLoop);
        }
        if (pickUp && animName.equals("Dodge")) {
            pickUp = false;
            System.out.println("Almoust Last");
            runCollector();
        }
        if (animName.equals("stand")) {
        }
    }
    boolean restart;

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    Vector3f currentPos;
    Vector3f desiredPos = new Vector3f(10, 10, 10);
    Vector3f distance = new Vector3f(0, 0, 0);
    float time = 0f;
    private boolean shoudMove = false;
    private float count = 0;
    private Vector3f start;
    private Vector3f end = new Vector3f(10, 5, 5);

    public void setEndPos(Vector3f pos) {
//        end = data.getObjects().get(geoCounter).getWorldTranslation();
        if (pos != null) {
            end = pos;
        }

    }

    private void createCollector() {

        collectorNode = (Node) assetM.loadModel("Models/Otto/Oto.mesh.xml");
        collectorNode.setLocalTranslation(new Vector3f(-10, 10, 10));
        collectorNode.addControl(collector);
        RigidBodyControl brick_phy = new RigidBodyControl(2f);
        collectorNode.addControl(brick_phy);
        control = collectorNode.getControl(AnimControl.class);
        control.addListener(this);
        channel = control.createChannel();
        pickChannel = control.createChannel();
        channel.setAnim("stand");
        root.attachChild(collectorNode);

    }
    public Node pickedItem;
    private int geoCounter = 0;
    Geometry testGeom;
    List<Geometry> geomInScene = new ArrayList<Geometry>();

   

    private void setPickedGeom() {
        System.out.println("HAS BEEN RUN " + geoCounter);
        NumberGeometries = data.getObjects().size() -1;
        System.out.println("FROM DATA LIST" + NumberGeometries);
        if (NumberGeometries >= 0 ) {
            testGeom = (Geometry) data.getObjects().get(NumberGeometries).getChild(0);
            System.out.println("geoemtries" + testGeom);
            end = testGeom.getWorldTranslation();
            geoCounter++;
//            NumberGeometries --;
        } else {
            end = new Vector3f(10, 5, 5);
        }
    }

    public void moveCollector(float tpf, Geometry pickedGeometry) {
       
            
          if (restart) {
            runCollector();
            restart = false;
        }  
        
        
        if (pickedGeometry == null) {

            pickedGeometry = testGeom;
//           end = testGeom.getWorldTranslation();
        }


        currentPos = collector.getPhysicsLocation();
//        System.out.println("collector pos" + currentPos);
        currentPos = collectorNode.getLocalTranslation();
        Vector3f step;
        if (shoudMove && collectorNode.getLocalTranslation().distance(end) >= 5) {
//            System.out.println("Destinasjon" + end);
//            System.out.println("Lokasjon" + currentPos);
//            System.out.println("dist" + collectorNode.getLocalTranslation().distance(end));
            step = new Vector3f(end).subtract(currentPos);
            step.normalizeLocal();
            step.multLocal(1f);
            step.setY(0f);
            collector.setWalkDirection(step);
            collector.setViewDirection(step);
//            System.out.println(" step ing " + step + "");
        } else {
            step = new Vector3f(0.0f, 0.0f, 0.0f);
            shoudMove = false;
            collector.setWalkDirection(step);

        }
        if (pickUp) {

            SkeletonControl skeletonControl = collectorNode.getControl(SkeletonControl.class);
//            pickedGeometry.move(1, 2, 0);

            pickedGeometry.setLocalTranslation(1, 2, 0);
            pickedItem = skeletonControl.getAttachmentsNode("hand.left");
            pickedItem.attachChild(pickedGeometry);
            pickedGeometry.getControl(RigidBodyControl.class).setEnabled(false);
            pickedGeometry.getControl(RigidBodyControl.class).setKinematic(true);
            Vector3f pos = pickedGeometry.getLocalTranslation();
            Material mat = pickedGeometry.getMaterial();
            mat.setColor("Color", ColorRGBA.Red);
            end = new Vector3f(10, 5, 5);
            pickedGeometry.setMaterial(mat);
            Vector3f tmp = currentPos;
            tmp.x += 10;

            haveItem = true;
        }
    }
    boolean removeGeometry;

    public boolean removeGeometry() {
        return removeGeometry;
    }

    public void resetRemovedGeometry() {
        removeGeometry = false;
    }
    boolean haveItem;

    public void runCollector() {
        
        removeGeometry = false;
        start = collector.getPhysicsLocation();
        channel.setAnim("Walk", 0.1f);
        channel.setSpeed(7);
        channel.setLoopMode(LoopMode.Loop);
        shoudMove = true;
    }

    private void configureSun() {
        sunDir = new Vector3f(-.10f, -.5f, -.5f).normalizeLocal();
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(sunDir);
        root.addLight(sun);
    }

    private void configureScene() {
        Spatial scene = assetM.loadModel("Scenes/Scene1.j3o");
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) scene);
        landscape = new RigidBodyControl(sceneShape, 0);
        scene.addControl(landscape);
        root.attachChild(scene);
    }

    private void removeGeom() {
       pickedItem.getChild(0).removeFromParent();
        data.getObjects().remove(data.getObjects().get(NumberGeometries));
        setPickedGeom();
        runCollector();
    }
}
