package dopengl.shapes;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.programs.LineProgram;

/**
 * Simple OpenGL triangle shape.<br>
 * Same as GLTriangle except uses uniform coloring.
 *
 * @author Derek Mulvihill
 */
public class GLTriangleUniform {
    private LineProgram program;

    private GLFloatBuffer vertexBuffer;

    public GLTriangleUniform() {
        program = new LineProgram();

        vertexBuffer = OpenGLUtil.createBuffer(3 * 3); //3 vertices * 3 coordinates per vertex
    }

    public void draw(float[] vpMatrix, float red, float green, float blue, float alpha) {
        program.start(vpMatrix);

        program.bufferVertexPosition(vertexBuffer);
        program.uniformColor(red, green, blue, alpha);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3); //number of vertices

        program.end();
    }

    /**
     * Must be at least a length 9 array.
     */
    public void setVertices(float[] vertices) {
        if (vertices.length < 9)
            return;
        float[] vertexValues = vertexBuffer.getData();
        System.arraycopy(vertices, 0, vertexValues, 0, 9);
    }
}