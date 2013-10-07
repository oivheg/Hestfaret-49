package utilities;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 * Outline box, drawn using lines.
 *
 * @author thomasw
 */
public class OutlineBox extends Mesh {

    int datasize = 0;
    int numberOfLines = 0;
    float lineWidth = 0.1f;

    public OutlineBox(float size) {
        setMode(Mode.Lines);

        float xmax = size / 2;
        setLineWidth(1.0f);

        numberOfLines = (int) (size / lineWidth);
        numberOfLines += 1;

        datasize = numberOfLines * 3 * 2;
        int lines = 24;
        float[] vertices = new float[datasize * lines]; // * 12

        int idx = 0;

        // Lower plane
        addOutlinePlane(vertices, idx, new Vector3f(-xmax, -xmax, xmax));
        idx += datasize * 4;

        // Upper plane
        addOutlinePlane(vertices, idx, new Vector3f(-xmax, xmax, xmax));
        idx += datasize * 4;

        // Right front column
        addOutlineColumn(vertices, idx, new Vector3f(xmax - lineWidth, -xmax, xmax - lineWidth));
        idx += datasize * 4;

        // Rigth back column
        addOutlineColumn(vertices, idx, new Vector3f(xmax - lineWidth, -xmax, -xmax));
        idx += datasize * 4;

        // Left front column
        addOutlineColumn(vertices, idx, new Vector3f(-xmax, -xmax, xmax - lineWidth));
        idx += datasize * 4;

        // Left back column
        addOutlineColumn(vertices, idx, new Vector3f(-xmax, -xmax, -xmax));

        setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        updateBound();
    }

    private void addOutlinePlane(float vertices[], int start, Vector3f loc) {
        Vector3f offset = new Vector3f();
        Vector3f growth = new Vector3f();
        Vector3f direction = new Vector3f();

        // Front lower
        offset.set(loc.x, loc.y, loc.z);
        growth.set(lineWidth, 0, 0);
        direction.set(0, 0, -lineWidth);
        addOutline(vertices, start, offset, growth, direction);
        start += datasize;

        // Back lower
        offset.set(loc.x, loc.y, -loc.z);
        growth.set(lineWidth, 0, 0);
        direction.set(0, 0, lineWidth);
        addOutline(vertices, start, offset, growth, direction);
        start += datasize;

        // Left lower
        offset.set(loc.x, loc.y, -loc.z);
        growth.set(0, 0, lineWidth);
        direction.set(lineWidth, 0, 0);
        addOutline(vertices, start, offset, growth, direction);
        start += datasize;

        // Right lower
        offset.set(loc.x + FastMath.abs(loc.x) * 2, loc.y, -loc.z);
        growth.set(0, 0, lineWidth);
        direction.set(-lineWidth, 0, 0);
        addOutline(vertices, start, offset, growth, direction);
        start += datasize;
    }

    private void addOutlineColumn(float[] vertices, int start, Vector3f loc) {
        Vector3f offset = new Vector3f();
        Vector3f growth = new Vector3f();
        Vector3f direction = new Vector3f();


        // Front column
        offset.set(loc.x + lineWidth, loc.y, loc.z + lineWidth);
        growth.set(0, lineWidth, 0);
        direction.set(-lineWidth, 0, 0);
        addOutline(vertices, start, offset, growth, direction);

        start += datasize;

        // Back column
        offset.set(loc.x + lineWidth, loc.y, loc.z);
        growth.set(0, lineWidth, 0);
        direction.set(-lineWidth, 0, 0);
        addOutline(vertices, start, offset, growth, direction);

        start += datasize;

        // Right column
        offset.set(loc.x + lineWidth, loc.y, loc.z + lineWidth);
        growth.set(0, lineWidth, 0);
        direction.set(0, 0, -lineWidth);
        addOutline(vertices, start, offset, growth, direction);

        start += datasize;

        // Left column
        offset.set(loc.x, loc.y, loc.z + lineWidth);
        growth.set(0, lineWidth, 0);
        direction.set(0, 0, -lineWidth);
        addOutline(vertices, start, offset, growth, direction);
    }

    private void addOutline(float[] vertices, int start, Vector3f offset, Vector3f growth, Vector3f direction) {
        for (int i = 0, idx = start; i < numberOfLines; i++, idx += 6) {
            // Start of line
            vertices[idx] = offset.x;
            vertices[idx + 1] = offset.y;
            vertices[idx + 2] = offset.z;

            // End of line
            vertices[idx + 3] = offset.x + direction.x;
            vertices[idx + 4] = offset.y + direction.y;
            vertices[idx + 5] = offset.z + direction.z;

            offset.addLocal(growth);
        }
    }
}
