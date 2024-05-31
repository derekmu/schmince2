package dopengl.shapes;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.programs.ShapeProgram;
import thed.GLBuffer;

/**
 * Draw a set of up to 20 triangles.
 *
 * @author Derek Mulvihill
 */
public class GLTriangleSet {
	public static final int MAX_TRIANGLES = 100;

	private ShapeProgram program;

	private GLFloatBuffer vertexBuffer;
	private GLFloatBuffer colorBuffer;

	public GLTriangleSet() {
		program = new ShapeProgram();

		GLBuffer buffer = new GLBuffer(MAX_TRIANGLES);
		vertexBuffer = OpenGLUtil.createBuffer(buffer.getVertices());
		colorBuffer = OpenGLUtil.createBuffer(buffer.getColors());
		colorBuffer.setStepSize(4);
	}

	public void draw(float[] vpMatrix, int triangles) {
		program.start(vpMatrix);

		program.bufferVertexPosition(vertexBuffer);
		program.bufferVertexColor(colorBuffer);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, Math.min(MAX_TRIANGLES, triangles) * 3); //number of vertices

		program.end();
	}

	public void setVertices(float[] vertices) {
		float[] vertexValues = vertexBuffer.getData();
		System.arraycopy(vertices, 0, vertexValues, 0,
				Math.min(vertices.length, vertexValues.length));
	}

	public void setColors(float[] colors) {
		float[] colorValues = colorBuffer.getData();
		System.arraycopy(colors, 0, colorValues, 0, Math.min(colors.length, colorValues.length));
	}
}