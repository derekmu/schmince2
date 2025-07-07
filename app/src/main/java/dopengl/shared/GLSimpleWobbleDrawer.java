package dopengl.shared;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.drawable.GLDrawable;
import dopengl.programs.ShapeProgram;
import thed.DTimer;
import thed.WobbleBuffer;

/**
 * @author Derek Mulvihill - Nov 21, 2013
 */
public class GLSimpleWobbleDrawer implements GLDrawable {
    protected final ShapeProgram program;

    protected final WobbleBuffer buffer;

    protected final GLFloatBuffer vertexBuffer;
    protected final GLFloatBuffer colorBuffer;

    public GLSimpleWobbleDrawer(int triangles, int wobtime, double wobdist) {
        program = new ShapeProgram();
        buffer = new WobbleBuffer(triangles, wobtime, wobdist);

        vertexBuffer = OpenGLUtil.createBuffer(buffer.getVertices());
        colorBuffer = OpenGLUtil.createBuffer(buffer.getColors());
        colorBuffer.setStepSize(4);
    }

    @Override
    public void draw(float[] vpMatrix) {
        draw(vpMatrix, DTimer.get().millis());
    }

    public void draw(float[] vpMatrix, long seed) {
        buffer.updateVertices(seed);

        program.start(vpMatrix);

        program.bufferVertexPosition(vertexBuffer);
        program.staticVertexColor(colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, buffer.getVertexCount());

        program.end();
    }
}
