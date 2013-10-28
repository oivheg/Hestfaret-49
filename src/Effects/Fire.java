/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 *
 * @author oivhe_000
 */
public class Fire {
    
    private ParticleEmitter Fire, Debris;
    
    public Fire(AssetManager manager){
        
           ParticleEmitter fire = 
            new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    Material mat_red = new Material(manager, 
            "Common/MatDefs/Misc/Particle.j3md");
    mat_red.setTexture("Texture", manager.loadTexture(
            "Effects/Explosion/flame.png"));
    fire.setMaterial(mat_red);
    fire.setImagesX(2); 
    fire.setImagesY(2); // 2x2 texture animation
    fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
    fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
    fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    fire.setStartSize(1.5f);
    fire.setEndSize(0.1f);
    fire.setGravity(1,1,0);
    fire.setLowLife(1f);
    fire.setHighLife(3f);
    fire.getParticleInfluencer().setVelocityVariation(0.3f);
    Fire = fire;
 
    ParticleEmitter debris = 
            new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
    Material debris_mat = new Material(manager, 
            "Common/MatDefs/Misc/Particle.j3md");
    debris_mat.setTexture("Texture", manager.loadTexture(
            "Effects/Explosion/Debris.png"));
    debris.setMaterial(debris_mat);
    debris.setImagesX(3); 
    debris.setImagesY(3); // 3x3 texture animation
    debris.setRotateSpeed(4);
    debris.setSelectRandomImage(true);
    debris.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 4, 0));
    debris.setStartColor(ColorRGBA.White);
    debris.setGravity(0, 6, 0);
    debris.getParticleInfluencer().setVelocityVariation(.60f);
    Debris = debris;
    debris.emitAllParticles();
    }
    
    public ParticleEmitter GetFire(){
        return Fire;
    }
    
    public ParticleEmitter GetDebris() {
        return Debris;
    }
}
