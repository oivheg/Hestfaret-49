package utilities;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 * Renders a box using indexed triangles.
 *
 * @author thomasw
 */
public class IndexedTriangleBox extends Mesh {

    public IndexedTriangleBox(float size, boolean useNormals, boolean useTexCoords) {
        float halfSize = size / 2;

        int[] indices = null;
        float[] vertices = null;
        float[] normals = null;
        float[] texCoords = null;

        indices = new int[]{
            2, 0, 1, 1, 3, 2, // front
            6, 4, 5, 5, 7, 6, // back
            10, 8, 9, 9, 11, 10, // right
            14, 12, 13, 13, 15, 14, // left
            18, 16, 17, 17, 19, 18, // top
            22, 20, 21, 21, 23, 22, // bottom
        };

        vertices = new float[]{
            // front face
            -halfSize, -halfSize, halfSize,
            halfSize, -halfSize, halfSize,
            -halfSize, halfSize, halfSize,
            halfSize, halfSize, halfSize,
            // back face
            halfSize, -halfSize, -halfSize,
            -halfSize, -halfSize, -halfSize,
            halfSize, halfSize, -halfSize,
            -halfSize, halfSize, -halfSize,
            // right face
            halfSize, -halfSize, halfSize,
            halfSize, -halfSize, -halfSize,
            halfSize, halfSize, halfSize,
            halfSize, halfSize, -halfSize,
            // left face
            -halfSize, -halfSize, -halfSize,
            -halfSize, -halfSize, halfSize,
            -halfSize, halfSize, -halfSize,
            -halfSize, halfSize, halfSize,
            // top face
            -halfSize, halfSize, halfSize,
            halfSize, halfSize, halfSize,
            -halfSize, halfSize, -halfSize,
            halfSize, halfSize, -halfSize,
            // bottom face
            halfSize, -halfSize, halfSize,
            -halfSize, -halfSize, halfSize,
            halfSize, -halfSize, -halfSize,
            -halfSize, -halfSize, -halfSize,};
        if (useNormals) {
            normals = new float[]{
                // Front face
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                // Back face
                0, 0, -1,
                0, 0, -1,
                0, 0, -1,
                0, 0, -1,
                // Right face
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
                1, 0, 0,
                // Left face
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                -1, 0, 0,
                // Top face
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                0, 1, 0,
                // Bottom face
                0, -1, 0,
                0, -1, 0,
                0, -1, 0,
                0, -1, 0
            };
        }
        if (useTexCoords) {
            texCoords = new float[]{
                // Front face
                0, 0,
                1, 0,
                0, 1,
                1, 1,
                // Back face
                0, 0,
                1, 0,
                0, 1,
                1, 1,
                // Right face
                0, 0,
                1, 0,
                0, 1,
                1, 1,
                // Left face
                0, 0,
                1, 0,
                0, 1,
                1, 1,
                // Top face
                0, 0,
                1, 0,
                0, 1,
                1, 1,
                // Bottom face
                0, 0,
                1, 0,
                0, 1,
                1, 1
            };
        }

        setMode(Mode.Triangles);

//        ByteBuffer indByteBuf = ByteBuffer.allocateDirect(indices.length * 4);
//        indByteBuf.order(ByteOrder.nativeOrder());
//        IntBuffer indIntBuf = indByteBuf.asIntBuffer();
//        indIntBuf.put(indices);
//        setBuffer(VertexBuffer.Type.Index, 3, indIntBuf);
        setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices));

//        ByteBuffer vertByteBuf = ByteBuffer.allocateDirect(vertices.length * 8);
//        vertByteBuf.order(ByteOrder.nativeOrder());
//        FloatBuffer vertFloatBuf = vertByteBuf.asFloatBuffer();
//        vertFloatBuf.put(vertices);
//        setBuffer(VertexBuffer.Type.Position, 3, vertFloatBuf);
        setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));

        if (useNormals) {
//            ByteBuffer normByteBuf = ByteBuffer.allocateDirect(normals.length * 8);
//            normByteBuf.order(ByteOrder.nativeOrder());
//            FloatBuffer normFloatBuf = normByteBuf.asFloatBuffer();
//            normFloatBuf.put(normals);
//            setBuffer(VertexBuffer.Type.Normal, 3, normFloatBuf);
            setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        }

        if (useTexCoords) {
//            ByteBuffer texByteBuf = ByteBuffer.allocateDirect(texCoords.length * 8);
//            texByteBuf.order(ByteOrder.nativeOrder());
//            FloatBuffer texFloatBuf = texByteBuf.asFloatBuffer();
//            texFloatBuf.put(texCoords);
//            setBuffer(VertexBuffer.Type.TexCoord2, 2, texFloatBuf);
            setBuffer(VertexBuffer.Type.TexCoord2, 2, BufferUtils.createFloatBuffer(texCoords));
        }

        updateBound();
    }
}
