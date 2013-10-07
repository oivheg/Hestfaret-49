package utilities;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 * Rotation control that continously rotates around the y-axis.
 *
 * @author thomasw
 */
public class RotationControl extends AbstractControl {

    private final Quaternion rotQuat;
    private float angle = 0;

    public RotationControl() {
        rotQuat = new Quaternion();
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
    }

    @Override
    protected void controlUpdate(float tpf) {
        // Increase the angle with a speed of 1, multiplied by the time per frame,
        // to ensure smooth animation steps.
        angle += tpf * 1;
        
        // Reset after a round.
        if (angle > 360) {
            angle = 0;
        }

        // Calculate rotation.
        rotQuat.fromAngleNormalAxis(angle, Vector3f.UNIT_Y);
        // Set rotation for spatial.
        spatial.setLocalRotation(rotQuat);
    }

    @Override
    protected void controlRender(RenderManager arg0, ViewPort arg1) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final RotationControl newControl = new RotationControl();
        newControl.setSpatial(spatial);
        return newControl;
    }
}