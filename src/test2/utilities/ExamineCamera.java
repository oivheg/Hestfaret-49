package utilities;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * ExamineCamera is a camera that lets the user examine the target.
 * The camera rotates around the target when dragging the left button. 
 * It pans parallel to the view when dragging the right button. It 
 * zooms in and out when holding shift, and moving the mouse up or down. 
 * This allows zooming for computers that don't have the middle mouse button.
 * 
 * @author thomasw
 */
public class ExamineCamera implements ActionListener, AnalogListener {

    protected final static String ChaseCamDown = "ChaseCamDown";
    protected final static String ChaseCamUp = "ChaseCamUp";
    protected final static String ChaseCamZoomIn = "ChaseCamZoomIn";
    protected final static String ChaseCamZoomOut = "ChaseCamZoomOut";
    protected final static String ChaseCamMoveLeft = "ChaseCamMoveLeft";
    protected final static String ChaseCamMoveRight = "ChaseCamMoveRight";
    protected final static String ChaseCamToggleRotate = "ChaseCamToggleRotate";
    protected final static String ChaseCamTogglePan = "ChaseCamTogglePan";
    protected final static String ChaseCamZoomModifier = "ChaseCamZoomModifier";
    protected Camera cam;
    protected Vector3f initialUpVec;
    protected InputManager inputManager;
    protected float rotationSpeed = 3f;
    protected float panSpeed = 3f;
    protected float scrollSpeed1 = 1.0f;
    protected float scrollSpeed2 = 0.25f;
    protected float distance = 20;
    protected boolean invertYaxis = true;
    protected boolean invertXaxis = false;
    protected boolean canRotate = false;
    protected boolean canPan = false;
    protected boolean canZoom = false;
    protected Vector3f targetLoc = new Vector3f(0, 0, 0);
    protected Vector3f difTemp = new Vector3f();
    protected float minZoom = 0.1f;
    float rotationHorisontal = 0;
    float targetRotationHorisontal = rotationHorisontal;
    float maxVerticalRotation = FastMath.PI / 2;
    float minVerticalRotation = -maxVerticalRotation;
    float rotationVertical = 0;
    float targetRotationVertical = rotationVertical;
    float targetDistance = distance;
    Vector3f pos;
    boolean enabled = true;

    public ExamineCamera(Camera cam) {
        this(cam, 20.0f);
    }
    
    public ExamineCamera(Camera cam, float camDistanceFromCenter){
        this(cam, new Vector3f(0, 0, camDistanceFromCenter));
    }
    
    public ExamineCamera(Camera cam, Vector3f camLocation){
        this.cam = cam;
        this.distance = camLocation.z;
        initialUpVec = cam.getUp().clone();

        // Place camera initially, and look at [0, 0, 0].
        cam.setLocation(camLocation);
        cam.lookAt(targetLoc, initialUpVec);
        pos = new Vector3f();
    }
    
    public void setSensitivity(float sensitivity){
        rotationSpeed *= sensitivity;
        panSpeed *= sensitivity;
        scrollSpeed1 *= sensitivity;
        scrollSpeed2 *= sensitivity;
    }
    
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    
    public boolean isEnabled(){
        return enabled;
    }

    public void registerWithInput(InputManager inputManager) {
        String[] inputs = {ChaseCamToggleRotate,
            ChaseCamTogglePan,
            ChaseCamDown,
            ChaseCamUp,
            ChaseCamMoveLeft,
            ChaseCamMoveRight,
            ChaseCamZoomIn,
            ChaseCamZoomOut,
            ChaseCamZoomModifier};

        this.inputManager = inputManager;

        if (!invertYaxis) {
            inputManager.addMapping(ChaseCamDown, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
            inputManager.addMapping(ChaseCamUp, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        } else {
            inputManager.addMapping(ChaseCamDown, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
            inputManager.addMapping(ChaseCamUp, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        }
        inputManager.addMapping(ChaseCamZoomIn, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(ChaseCamZoomOut, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping(ChaseCamZoomModifier, new KeyTrigger(KeyInput.KEY_LSHIFT));
        if (!invertXaxis) {
            inputManager.addMapping(ChaseCamMoveLeft, new MouseAxisTrigger(MouseInput.AXIS_X, true));
            inputManager.addMapping(ChaseCamMoveRight, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        } else {
            inputManager.addMapping(ChaseCamMoveLeft, new MouseAxisTrigger(MouseInput.AXIS_X, false));
            inputManager.addMapping(ChaseCamMoveRight, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        }
        inputManager.addMapping(ChaseCamToggleRotate, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(ChaseCamTogglePan, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addListener(this, inputs);
    }
    
    public void unregisterInput() {
//
//        if (inputManager == null) {
//            return;
//        }
//
//        for (String s : mappings) {
//            if (inputManager.hasMapping(s)) {
//                inputManager.deleteMapping(s);
//            }
//        }
//
//        inputManager.removeListener(this);
//        inputManager.setCursorVisible(true);
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if(!isEnabled()){
            return;
        }
        
        if (name.equals(ChaseCamToggleRotate) && !canPan && !canZoom) {
            if (keyPressed) {
                canRotate = true;
                inputManager.setCursorVisible(false);
            } else {
                canRotate = false;
                inputManager.setCursorVisible(true);
            }
        } else if (name.equals(ChaseCamTogglePan) && !canRotate && !canZoom) {
            if (keyPressed) {
                canPan = true;
                inputManager.setCursorVisible(false);
            } else {
                canPan = false;
                inputManager.setCursorVisible(true);
            }
        } else if (name.equals(ChaseCamZoomModifier) && !canPan && !canRotate) {
            if (keyPressed) {
                canZoom = true;
                inputManager.setCursorVisible(false);
            } else {
                canZoom = false;
                inputManager.setCursorVisible(true);
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (!isEnabled()) {
            return;
        }
        
        if (name.equals(ChaseCamMoveLeft)) {
            if (canPan) {
                panCamera(-value, true);
            } else if (canRotate) {
                rotateCameraHorisontal(-value);
            }
        } else if (name.equals(ChaseCamMoveRight)) {
            if (canPan) {
                panCamera(value, true);
            } else if (canRotate) {
                rotateCameraHorisontal(value);
            }
        } else if (name.equals(ChaseCamUp)) {
            if (canPan) {
                panCamera(-value, false);
            } else if (canRotate) {
                rotateCameraVertical(value);
            }else if (canZoom) {
                zoomCamera(scrollSpeed2);
            }
        } else if (name.equals(ChaseCamDown)) {
            if (canPan) {
                panCamera(value, false);
            } else if (canRotate) {
                rotateCameraVertical(-value);
            }else if (canZoom) {
                zoomCamera(-scrollSpeed2);
            }
        } else if (name.equals(ChaseCamZoomIn)) {
            zoomCamera(-value);
        } else if (name.equals(ChaseCamZoomOut)) {
            zoomCamera(value);
        }
    }
    
    void rotateCameraHorisontal(float value){
        if (!canRotate) {
            return;
        }
        
        // Add rotation value to the current rotation, multiplied with 
        // sensitivity.
        targetRotationHorisontal += value * rotationSpeed;
        
        updateCamera();
    }
    
    void rotateCameraVertical(float value) {
        if (!canRotate) {
            return;
        }

        // Store last good rotation.
        float lastGoodRot = targetRotationVertical;
        
        targetRotationVertical += value * rotationSpeed;
        
        // Clamp rotation inside [-Math.PI/2, Math.PI/2], so that we dont
        // rotate upside down.
        if (targetRotationVertical > maxVerticalRotation) {
            targetRotationVertical = lastGoodRot;
        } else if (targetRotationVertical < minVerticalRotation) {
            targetRotationVertical = lastGoodRot;
        }
        
        updateCamera();
    }
    
    void updateCamera(){
        // If the camera have been moved by an external source, recalc rotations.
        if(!pos.equals(cam.getLocation())){
            calculateCameraRotations();
        }
        
        rotationVertical = targetRotationVertical;
        rotationHorisontal = targetRotationHorisontal;
        distance = targetDistance;
        // Compute the new position of the camera.
        computePosition();
        
        // Set the location of the camera.
        cam.setLocation(pos);
        // Use the lookAt function to correctly orient the camera, using the
        // point to look at, and the initial up vector of the camera.
        cam.lookAt(targetLoc, initialUpVec);
    }
    
    private void calculateCameraRotations() {
        targetDistance = getCameraDistanceFromTarget();
        targetRotationVertical = getVerticalRotationOfCamera();
        targetRotationHorisontal = getHorisontalRotationOfCamera();
    }

    private float getCameraDistanceFromTarget() {
        // Distance between the point we are looking at, and the camera.
        return targetLoc.distance(cam.getLocation());
    }

    private float getVerticalRotationOfCamera() {
        // Up vector for camera.
        Vector3f camUp = cam.getUp();
        // Direction of camera.
        Vector3f camDir = cam.getDirection();
        // Get vertical rotation of the camera by calculating angle between
        // the cameras up vector, and the unit_y vector (0, 1, 0).
        float angle = camUp.angleBetween(Vector3f.UNIT_Y);
        // Negate value if looking downwards.
        if (camDir.y > 0) {
            angle *= -1;
        }

        return angle;
    }

    private float getHorisontalRotationOfCamera() {
        // Left vector for camera.
        Vector3f camLeft = cam.getLeft();
        // Get horisontal rotation of the camear by calculating angle between
        // the cameras left vector, and the unit_x vector (1, 0, 0).
        float angle = camLeft.angleBetween(new Vector3f(1, 0, 0));
        // Negate value if looking downwards.
        if (camLeft.z < 0) {
            angle *= -1;
        }
        // Subtract a 90 degree angle from the result for offset.
        angle -= 90 * FastMath.DEG_TO_RAD;
        return angle;
    }
    
    protected void computePosition() {
        // Calculate the horisontal distance from the target.
        float hDistance = (distance) * FastMath.sin((FastMath.PI / 2) - rotationVertical);
        // Calculate x, y and z coordinate for the camera.
        pos.set(hDistance * FastMath.cos(rotationHorisontal), 
                (distance) * FastMath.sin(rotationVertical), 
                hDistance * FastMath.sin(rotationHorisontal));
        
        pos.addLocal(targetLoc);
    }

    protected void panCamera(float value, boolean horizontal) {
        value *= panSpeed;
        Vector3f camLoc = cam.getLocation();

        if (horizontal) {
            // Panning horisontally.
            // Multiply value by left vector for camera to get correct translation.
            // Store in tmp vector, and chaning also returns value so it is
            // added to camera location.
            camLoc.addLocal(cam.getLeft().mult(value, difTemp));
            // Add to the target location, so that target moves accordingly.
            targetLoc.addLocal(difTemp);
        } else {
            // Panning vertically.
            // Multiply value by up vector for camera to get correct translation.
            // Store in tmp vector, and chaning also returns value so it is
            // added to camera location.
            camLoc.subtractLocal(cam.getUp().mult(value, difTemp));
            // Add to the target location, so that target moves accordingly.
            targetLoc.subtractLocal(difTemp);
        }

        cam.setLocation(camLoc);
    }
    
    void zoomCamera(float value) {
        targetDistance += value * scrollSpeed1;
        if (targetDistance < minZoom) {
            targetDistance = minZoom;
        }
        
        updateCamera();
    }
}
