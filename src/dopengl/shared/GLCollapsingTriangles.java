package dopengl.shared;

import android.opengl.GLES20;
import android.opengl.Matrix;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.drawable.GLDrawable;
import dopengl.programs.ShapeProgram;
import thed.DRandom;
import thed.DTimer;
import thed.DUtil;
import thed.GLBuffer;

/**
 * @author Derek Mulvihill - Nov 21, 2013
 */
public class GLCollapsingTriangles implements GLDrawable {
    private ShapeProgram program = new ShapeProgram();

    protected GLBuffer buffer;

    protected GLFloatBuffer vertexBuffer;
    protected GLFloatBuffer colorBuffer;

    private float[] basevertices;
    private double[] looptimes;

    public GLCollapsingTriangles(int triangles, float triangleScale) {
        buffer = new GLBuffer(triangles);
        looptimes = new double[triangles];
        basevertices = DUtil.getScaledEqualTriangle(triangleScale);

        for (int i = 0; i < looptimes.length; i++) {
            looptimes[i] = DRandom.get().nextInt(2500) + 1500;
        }

        vertexBuffer = OpenGLUtil.createBuffer(buffer.getVertices());

        colorBuffer = OpenGLUtil.createBuffer(buffer.getColors());
        colorBuffer.setStepSize(4);
    }

    @Override
    public void draw(float[] vpMatrix) {
        draw(vpMatrix, 0);
    }

    public void draw(float[] vpMatrix, int offset) {
        float[] vertexData = vertexBuffer.getData();
        long m = DTimer.get().millis() + offset;
        float angle = 0;
        for (int n = 0; n < buffer.getTriangles(); n++) {
            double wang = 1 - (m % looptimes[n]) / looptimes[n];
            float dx = (float) (Math.cos(angle) * wang);
            float dy = (float) (Math.sin(angle) * wang);

            vertexData[n * 9] = basevertices[0] + dx;
            vertexData[n * 9 + 1] = basevertices[1] + dy;

            vertexData[n * 9 + 3] = basevertices[3] + dx;
            vertexData[n * 9 + 4] = basevertices[4] + dy;

            vertexData[n * 9 + 6] = basevertices[6] + dx;
            vertexData[n * 9 + 7] = basevertices[7] + dy;

            angle += (float) Math.PI * 2 / buffer.getTriangles();
        }

        Matrix.rotateM(vpMatrix, 0, offset, 0, 0, 1);
        program.start(vpMatrix);

        program.bufferVertexPosition(vertexBuffer);
        program.staticVertexColor(colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, buffer.getVertexCount());

        program.end();
    }
}
