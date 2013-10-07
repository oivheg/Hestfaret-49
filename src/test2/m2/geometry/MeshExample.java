package m2.geometry;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 * Example demonstrates how to construct a mesh by hand by defining each
 * vertex. Constructs a quad using indexed triangles, with coloring
 * done per vertex.
 *
 * @author thomasw
 */
public class MeshExample extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        // Illustration of quad:
        // 2\2--3
        // | \  | 
        // |  \ |
        // 0--1\1
        //
        // 0, 1, 2, 3 corresponds to indices.
        
        
        // Each vertex.
        Vector3f[] vertices = new Vector3f[]{
            new Vector3f(0, 0, 0), // Lower left.
            new Vector3f(3, 0, 0), // Lower right.
            new Vector3f(0, 3, 0), // Upper left.
            new Vector3f(3, 3, 0), // Upper right.
        };
        
        // Per vertex coloring. RGBA.
        float[] colors = new float[]{
            1, 0, 0, 1,
            0, 1, 0, 1,
            0, 0, 1, 1,
            1, 0, 0, 1,
        };
        
        // Indices. Counter-clockwise ordering.
        int[] indices = new int[]{
            2, 0, 1,
            1, 3, 2
        };
        
        // Empty mesh object.
        Mesh quadMesh = new Mesh();
        
        // Assign buffers.
        quadMesh.setBuffer(VertexBuffer.Type.Position, 3, 
                BufferUtils.createFloatBuffer(vertices));
        quadMesh.setBuffer(VertexBuffer.Type.Index, 3, 
                BufferUtils.createIntBuffer(indices));
        quadMesh.setBuffer(VertexBuffer.Type.Color, 4, 
                BufferUtils.createFloatBuffer(colors));
        
        // Update the bounds of the mesh, so that bounding box is correctly
        // recalcualted internally by jME.
        quadMesh.updateBound();
        
        
        // Create geometry, based on the mesh representing a quad.
        Geometry quad = new Geometry("quad", quadMesh);

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        // Set material to use the vertex coloring defined.
        mat.setBoolean("VertexColor", true);
        quad.setMaterial(mat);
        
        quad.setLocalTranslation(new Vector3f(-1.5f, -1.5f, 2));

        rootNode.attachChild(quad);
    }

    public static void main(String[] args) {
        MeshExample app = new MeshExample();
        app.start();
    }
}
