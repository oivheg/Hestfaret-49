package utilities;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 * AppState that handles an ExamineCamera.
 * 
 * @author thomasw
 */
public class ExamineCameraAppState extends AbstractAppState {

    private Application app;
    private ExamineCamera examineCamera;

    public ExamineCameraAppState() {
    }

    public void setCamera(ExamineCamera cam) {
        this.examineCamera = cam;
    }
    
    public ExamineCamera getCamera() {
        return examineCamera;
    }
    
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        examineCamera.setEnabled(enabled);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        
        this.app = app;
        
        if(app.getInputManager() != null){
            if(examineCamera == null){
                examineCamera = new ExamineCamera(app.getCamera());
            }
            
            examineCamera.registerWithInput(app.getInputManager());
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();

        examineCamera.unregisterInput();
    }
}
