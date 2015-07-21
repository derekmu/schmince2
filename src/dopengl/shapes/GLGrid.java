package dopengl.shapes;

import android.opengl.GLES20;
import android.opengl.Matrix;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.programs.LineProgram;

/**
 * Draw lines that form a grid.
 *
 * @author Derek Mulvihill
 */
public class GLGrid {
    private LineProgram program;

    private GLFloatBuffer vertexBuffer;

    private static final int linecount = 21; //how many parallel lines
    private static final float linestride = 10.0f; //how many world space units between lines

    public GLGrid() {
        program = new LineProgram();

        float[] vertexData = new float[linecount * 2 * 2 * 3]; //2 sets of lines, 2 vertices per line, 3 dimensions per vertex
        int n = 0;
        float x = -((linecount - 1) / 2.0f * linestride);
        float y = x;
        float top = -y;
        float right = -x;
        for (int i = 0; i < linecount; i++) {
            //bottom vertex
            vertexData[n++] = x;
            vertexData[n++] = y;
            vertexData[n++] = 0.0f;

            //top vertex
            vertexData[n++] = x;
            vertexData[n++] = top;
            vertexData[n++] = 0.0f;

            x += linestride;
        }
        x = y;
        for (int i = 0; i < linecount; i++) {
            //left vertex
            vertexData[n++] = x;
            vertexData[n++] = y;
            vertexData[n++] = 0.0f;

            //right vertex
            vertexData[n++] = right;
            vertexData[n++] = y;
            vertexData[n++] = 0.0f;

            y += linestride;
        }

        vertexBuffer = OpenGLUtil.createBuffer(vertexData);
        vertexBuffer.pushStaticBuffer();
    }

    public void draw(float[] vpMatrix, float cx, float cy) {
        cx /= linestride;
        cx = Math.round(cx);
        cx *= linestride;
        cy /= linestride;
        cy = Math.round(cy);
        cy *= linestride;
        Matrix.translateM(vpMatrix, 0, cx, cy, 0);

        program.start(vpMatrix);

        program.staticVertexPosition(vertexBuffer);
        program.uniformColor(0.25f, 0.25f, 0.25f, 1f);

        //draw lines
        GLES20.glLineWidth(2.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, linecount * 2 * 2); //number of vertices

        program.end();
    }
}
