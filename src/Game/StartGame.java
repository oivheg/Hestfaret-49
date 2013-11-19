/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Data.Data;
import Effects.Fire;
import NOTInUSEObserver.Observer;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
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
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 * @author oivhe_000
 */
public class StartGame implements ActionListener, AnimEventListener {

    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private RigidBodyControl objects;
    private CharacterControl player;
    private InputManager inputManager;
    private FlyByCamera flyCam;
    private Camera cam1;
    private Node root = new Node("root");
    private AssetManager assetM;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private String clickedname = "testing";
    private ArrayList<Observer> obsList;
    private Vector3f camLocation;
    private DirectionalLight sun;
    private Vector3f sunDir = new Vector3f(-.10f, -.5f, -.5f).normalizeLocal();
    private Data data;
    private CharacterControl collector;

    public StartGame(AssetManager manager, AppStateManager stateManager, ViewPort viewPort, FlyByCamera fly, InputManager im, Camera c) {
        assetM = manager;
        inputManager = im;
        flyCam = fly;
        cam1 = c;
        obsList = new ArrayList();
        data = new Data(assetM, 3, 4);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().enableDebug(assetM);

        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(sunDir);
        root.addLight(sun);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        Spatial scene = assetM.loadModel("Scenes/Scene1.j3o");
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) scene);

        landscape = new RigidBodyControl(sceneShape, 0);
        scene.addControl(landscape);
        root.attachChild(scene);
        addObjects();
        //fire
        addFire();
        Physics();
        createCollector();
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
            Node h3 = new Node("+i+");
            h3.setLocalTranslation(10 * i, 5, 5);
            h3.attachChild(data.getObjects().get(i));

            root.attachChild(h3);
        }
    }

    private void addFire() {
        // fire
        Fire fire = new Fire(assetM);
        Node f = new Node("fire");
        f.attachChild(fire.GetFire());
        f.attachChild(fire.GetDebris());
        f.setLocalTranslation(0, 20, 0);

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
            bulletAppState.getPhysicsSpace().add(data.getObjects().get(i));
        }
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
    private AnimControl control;
    Node collectorNode;

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (!shoudMove) {
            channel.setAnim("Dodge", 0.50f);
            channel.setLoopMode(LoopMode.DontLoop);
            channel.setSpeed(1f);
        }
    }

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
        end = pos;
    }

    private void createCollector() {

        collectorNode = (Node) assetM.loadModel("Models/Otto/Oto.mesh.xml");
        collectorNode.setLocalTranslation(new Vector3f(-10, 10, 10));
        collectorNode.addControl(collector);
        control = collectorNode.getControl(AnimControl.class);
        control.addListener(this);
        channel = control.createChannel();
        channel.setAnim("stand");
        root.attachChild(collectorNode);
    }

    public void moveCollector(float tpf) {
        currentPos = collector.getPhysicsLocation();
        System.out.println("collector pos" + currentPos);
        currentPos = collectorNode.getLocalTranslation();
        Vector3f step;
        if (shoudMove){
            if (collectorNode.getLocalTranslation().distance(end) >= 5) {
            step = new Vector3f(end).subtract(currentPos);
            step.normalizeLocal();
            step.multLocal(0.1f);
            step.setY(0f);
            collector.setWalkDirection(step);
            collector.setViewDirection(step);
            System.out.println(" step ing " + step + "");
        } else {
            step = new Vector3f(0.0f, 0.0f, 0.0f);
            shoudMove = false;
            collector.setWalkDirection(step);
        }
        }
    }

    public void runcollector(float tpf) {
        start = collector.getPhysicsLocation();
        channel.setAnim("Walk", 0.1f);
        channel.setLoopMode(LoopMode.Loop);
        shoudMove = true;

    }
}
