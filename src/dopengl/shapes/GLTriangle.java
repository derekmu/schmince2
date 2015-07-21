package dopengl.shapes;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.programs.ShapeProgram;
import thed.GLBuffer;
import thed.GLColor;

/**
 * Simple OpenGL triangle shape.
 *
 * @author Derek Mulvihill
 */
public class GLTriangle {
    private ShapeProgram program;

    private GLBuffer buffer = new GLBuffer(1);

    private GLFloatBuffer vertexBuffer;
    private GLFloatBuffer colorBuffer;

    public GLTriangle() {
        program = new ShapeProgram();

        vertexBuffer = OpenGLUtil.createBuffer(buffer.getVertices());
        colorBuffer = OpenGLUtil.createBuffer(buffer.getColors());
        colorBuffer.setStepSize(4);
    }

    public void draw(float[] vpMatrix) {
        program.start(vpMatrix);

        program.bufferVertexPosition(vertexBuffer);
        program.bufferVertexColor(colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, buffer.getVertexCount());

        program.end();
    }

    public void drawLineLoop(float[] vpMatrix) {
        program.start(vpMatrix);

        program.bufferVertexPosition(vertexBuffer);
        program.bufferVertexColor(colorBuffer);
        GLES20.glLineWidth(1f);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, buffer.getVertexCount());

        program.end();
    }

    /**
     * Must be at least a length 9 array.
     */
    public void setVertices(float[] vertices) {
        if (vertices.length < 9) {
            return;
        }
        float[] vertexValues = vertexBuffer.getData();
        System.arraycopy(vertices, 0, vertexValues, 0, 9);
    }

    public void setColor(GLColor color) {
        float[] colorValues = colorBuffer.getData();
        System.arraycopy(color.get(), 0, colorValues, 0, 12);
    }
}