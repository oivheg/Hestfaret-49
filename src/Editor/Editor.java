package Editor;

import Editor.Camera.EditorCamera;
import Game.StartGame;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.terrain.geomipmap.TerrainPatch;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.tools.SizeValue;
import java.awt.Image;


public class Editor extends SimpleApplication implements ActionListener {

    public static void main(String[] args) {
        Editor app = new Editor();
        app.start();
    }
    private StartGame game;
    private Vector3f camDir = new Vector3f(), camLeft = new Vector3f(), walkDirection = new Vector3f(), pos = new Vector3f(0, 200, 300);
    private Nifty nifty;
    private EditorCamera ECam;
    private float width, height, dept, sideWays,upDown,backFourht,mass;
    private Geometry pickedGeometry;
    private String nameofPickedItem;
    private Material mat1;
boolean collectorExists;
    @Override
    public void simpleInitApp() {


        startMenu();
        setCamera();


// initializing StartGame()
        
        game = new StartGame(assetManager, stateManager, viewPort, flyCam, inputManager, cam);
        rootNode.attachChild(game.getRoot());
        addInputs();
    }

    private void addInputs() {
        String[] mName = {"mLeft", "mRight", "mUp", "mDown"};
        String[] mName2 = {"click", "ctrl", "view", "collect","rain"};
        inputManager.addMapping("click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("mLeft", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("mRight", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("mUp", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping("mDown", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("ctrl", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addMapping("view", new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping("collect", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("rain", new KeyTrigger(KeyInput.KEY_R));
        addListener(analogListener, mName);
        addListener(this, mName2);
    }

    private void addListener(InputListener listener, String[] mName) {
        inputManager.addListener(listener, mName);
    }
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (pickedGeometry != null && clicked) {
                if (name.equals("mLeft")) {
                    mouseDragged("left", tpf);
                }
                if (name.equals("mRight")) {
                    mouseDragged("right", tpf);
                }
                if (name.equals("mUp")) {
                    if (ctrl) {
                        mouseDragged("up", tpf);
                    } else {
                        mouseDragged("back", tpf);
                    }
                }
                if (name.equals("mDown")) {
                    if (ctrl) {
                        mouseDragged("down", tpf);
                    } else {
                        mouseDragged("forward", tpf);
                    }
                }
            }
        }
    };
    boolean isPicked = false;

    private void pickItem() {
        // Reset results list.
        CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        // Aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, dir);
        //Vector3f newPos = ray.getDirection();
        // Collect intersections between ray and all nodes in results list.
        rootNode.collideWith(ray, results);
        // (Print the results so we see what is going on:)
        for (int i = 0; i < results.size(); i++) {
            // (For each “hit”, we know distance, impact point, geometry.)
            float dist = results.getCollision(i).getDistance();
            Vector3f pt = results.getCollision(i).getContactPoint();
            String target = results.getCollision(i).getGeometry().getName();

            System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
        }
        // Use the results -- we rotate the selected geometry.
        if (results.size() > 0) {
            Geometry target = null;
            // The closest result is the target that the player picked:
            if (results.getClosestCollision().getGeometry().getName().equals("Sky")) {
                target = results.getCollision(1).getGeometry();
            } else {
                target = results.getClosestCollision().getGeometry();
            }

// if not terrain, prevents user from selecting terrain.
            if (isPicked){
                mat1 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        pickedGeometry.setMaterial(mat1);
        isPicked = false;
            }
          
            if (!(target instanceof TerrainPatch)) {
              
                mat1 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Red);
                        
                width = target.getLocalScale().x;
                height = target.getLocalScale().y;
                dept = target.getLocalScale().z;
                if(target.getControl(RigidBodyControl.class) != null){
                sideWays = target.getControl(RigidBodyControl.class).getGravity().x;
                upDown = target.getControl(RigidBodyControl.class).getGravity().y;
                backFourht = target.getControl(RigidBodyControl.class).getGravity().z;
                mass = target.getControl(RigidBodyControl.class).getMass();
            }
                pickedGeometry = target;
                nameofPickedItem = pickedGeometry.getName();
                if (!pickedGeometry.getName().equals("Oto-geom-1")){
                     isPicked = true;
                    pickedGeometry.setMaterial(mat1);
                }  
            } else {
// default values, if nothing is picked
                nameofPickedItem = "None Selected";
                pickedGeometry = null;
                width = -1;
                height = -1;
                dept = -1;
                sideWays = -1;
                upDown = -1;
                backFourht = -1;
            }
            //check if Geometry has Gravity ( RigidBodyControl. Kinematic = false)
            checkKinematic();
            // if has, Kinematic is turned on, so object can be moved
            if (hasKinematic) {
                pickedGeometry.getControl(RigidBodyControl.class).setKinematic(clicked);
            }
        }
    }
   

    @Override
    public void simpleUpdate(float tpf) {
        if (game != null) {
            updatePos(tpf);
            
            if (game.removeGeometry()) {
                pickedGeometry = null;
                isPicked = false;
//                game.pickedItem.getChild(0).removeFromParent();
////                pickedGeometry.getControl(RigidBodyControl.class).setEnabled(false);
////                pickedGeometry.removeFromParent();                
//                game.resetRemovedGeometry();
            } 
                if (collectorExists){
                    game.moveCollector(tpf, pickedGeometry);
                }
                
            }
        
        // this updates the label field in the hud to match what item is clicked ( will be selectet at a later point)
        if (nifty != null && "hud".equals(nifty.getCurrentScreen().getScreenId())) {
            // find old text
//            nifty.setIgnoreKeyboardEvents(true);
            
           
            Element Label = nifty.getCurrentScreen().findElementByName("name");
            TextField FieldWidth = nifty.getCurrentScreen().findNiftyControl("width", TextField.class);
            TextField FieldHeight = nifty.getCurrentScreen().findNiftyControl("height", TextField.class);
            TextField FieldDepth = nifty.getCurrentScreen().findNiftyControl("depth", TextField.class);
            TextField FieldGravitySideWays = nifty.getCurrentScreen().findNiftyControl("Gsideways", TextField.class);
            TextField FieldGravityUpDown = nifty.getCurrentScreen().findNiftyControl("GUpDown", TextField.class);
//            TextField FieldGravityBackFourth = nifty.getCurrentScreen().findNiftyControl("Gsideways", TextField.class);
            TextField FieldMass = nifty.getCurrentScreen().findNiftyControl("mass", TextField.class);
//           Element element = nifty.getCurrentScreen().findElementByName("imageId");
          
            CheckBox Gravity = nifty.getCurrentScreen().findNiftyControl("gravity", CheckBox.class);

//            if(Button.hasFocus()){
//                
//              game.resetObjects();
//            }
            // change data in nifty
            if (Label != null) {
                Label.getRenderer(TextRenderer.class).setText(getName());
                if (clicked) {
                    FieldWidth.setText(width + "");
                    FieldHeight.setText(height + "");
                    FieldDepth.setText(dept + "");
                    FieldGravitySideWays.setText(sideWays+"");
                    FieldGravityUpDown.setText(upDown+"");
                    FieldMass.setText(mass+"");
//                    FieldGravityBackFourth.setText(backFourht+"");
//element.setConstraintWidth(new SizeValue("5px"));
//element.getParent().layoutElements();
                    Gravity.setEnabled(hasKinematic);
                    if (isChecked) {
                        Gravity.setChecked(isChecked);
                    } else {
                        Gravity.setChecked(isChecked);
                    }
                } else {

                    if (Gravity.isChecked()) {
                        isChecked = true;
                    } else {
                        isChecked = false;
                    }
                    setKinematic(isChecked);
                    try {
                        width = Float.parseFloat(FieldWidth.getDisplayedText());
                        height = Float.parseFloat(FieldHeight.getDisplayedText());
                        dept = Float.parseFloat(FieldDepth.getDisplayedText());
                        sideWays = Float.parseFloat(FieldGravitySideWays.getDisplayedText());
                        upDown = Float.parseFloat(FieldGravityUpDown.getDisplayedText());
                        mass = Float.parseFloat(FieldMass.getDisplayedText());
//                        backFourht = Float.parseFloat(FieldGravityBackFourth.getDisplayedText());
                        if (!(pickedGeometry instanceof TerrainPatch) && pickedGeometry != null && !pickedGeometry.getName().equals("Oto-geom-1") && !pickedGeometry.getName().equals("Sky")) {
                            pickedGeometry.setLocalScale(width, height, dept);
                            Vector3f gravity = new Vector3f(sideWays,upDown,backFourht);
                            if (pickedGeometry.getControl(RigidBodyControl.class) != null){
                                                            pickedGeometry.getControl(RigidBodyControl.class).setGravity(gravity);
                            CollisionShape tmp = pickedGeometry.getControl(RigidBodyControl.class).getCollisionShape();
                            tmp.setScale(new Vector3f(width, height, dept));
                            pickedGeometry.getControl(RigidBodyControl.class).setCollisionShape(tmp);

                            }


                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Error occured ERROR:" + nfe );
                    }
                }
            }
        }
    }
    boolean runOnce;
    boolean view;

    public void updatePos(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
//        walkDirection.set(0, 0, 0);
        int tmp = 0;
        if (view) {
            ECam.setEnabled(false);
            game.setVectors(cam.getLocation());
            game.getPlayer().setWalkDirection(walkDirection);
            cam.setLocation(game.getPlayer().getPhysicsLocation());
            flyCam.setEnabled(true);
            flyCam.setDragToRotate(false);
            
            if (!runOnce) {
//            inputManager.removeListener(this);
                game.setUpKeys();
                runOnce = true;
                
            } else {
                game.updatePos(tpf);
            }
        }else{
            
        }
    }

    public String getName() {

        return nameofPickedItem;
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void startMenu() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/screen.xml", "start");
        // attach the Nifty display to the gui view port
        guiViewPort.addProcessor(niftyDisplay);
    }

    private void setCamera() {
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
        cam.setFrustumFar(5000);
        ECam = new EditorCamera(cam);
        ECam.registerWithInput(inputManager);
        ECam.setMoveSpeed(100);
        ECam.setEnabled(true);
        ECam.setZoomSpeed(100);
        //sets camera start position
        cam.setLocation(pos);
        cam.setLocation(new Vector3f(0, 10, 100));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        cam.update();
    }
    boolean clicked, ctrl;

    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("click")) {
            if (isPressed) {
                clicked = true;
                ECam.setDragToRotate(false);
                pickItem();
            } else {
                clicked = false;
                ECam.setDragToRotate(true);
                if (hasKinematic) {
                    pickedGeometry.getControl(RigidBodyControl.class).setKinematic(clicked);
                }
            }
        }
        if (binding.equals("ctrl")) {
            if (isPressed) {
                ctrl = true;
            } else {
                ctrl = false;
            }
        }
        if (binding.equals("view")) {
            if(isPressed) {
                if (view) {
                    view = false;
                    setCamera();
                }else {
                    view = true;
                }
                
            }
           
            
        }
         if(binding.equals("rain")){
                game.explode();
            }
            
        if (binding.equals("collect")) {
//            game.sunDir();
//            Vector3f geoPos = pickedGeometry.getWorldTranslation();
//            game.setEndPos(geoPos);
//            game.runCollector();
            if (pickedGeometry != null) {
                game.setEndPos(pickedGeometry.getWorldTranslation());
            }else {
                
                game.setEndPos(null);
            }
           if (!hascolelctor) {
//               game.createCollector(); 
           }
           hascolelctor = true;
               game.runCollector();
             collectorExists = true;
            
//            changeDay();
        }
    }
boolean hascolelctor;
    public void changeDay() {
        game.changeDay();
    }

    public void mouseDragged(String direction, float tpf) {
        float posx = pickedGeometry.getLocalTranslation().x;
        float posy = pickedGeometry.getLocalTranslation().y;
        float posz = pickedGeometry.getLocalTranslation().z;
        if (direction.equals("left")) {
            posx += 1.5;
        } else if (direction.equals("right")) {
            posx -= 1.5;
        } else if (direction.equals("back")) {
            posz -= 2;
        } else if (direction.equals("forward")) {
            posz += 2;
        } else if (direction.equals("up")) {
            posy += 2;
        } else if (direction.equals("down")) {
            posy -= 2;
        }
        pickedGeometry.setLocalTranslation(posx, posy, posz);
    }
    boolean hasKinematic = false;
    boolean isChecked = false;

    private void checkKinematic() {
        if (pickedGeometry != null && pickedGeometry.getControl(RigidBodyControl.class) != null) {
            hasKinematic = true;
            isChecked = !pickedGeometry.getControl(RigidBodyControl.class).isKinematic();
        } else {
            hasKinematic = false;
        }
    }

    private void setKinematic(boolean checked) {
        if (pickedGeometry != null && pickedGeometry.getControl(RigidBodyControl.class) != null) {
            pickedGeometry.getControl(RigidBodyControl.class).setKinematic(!checked);
        }
    }
}
