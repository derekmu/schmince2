package dopengl.shapes;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.programs.LineProgram;

/**
 * Simple OpenGL line shape.
 *
 * @author Derek Mulvihill
 */
public class GLLine {
	private LineProgram program;

	private GLFloatBuffer vertexBuffer;

	public GLLine() {
		program = new LineProgram();

		vertexBuffer = OpenGLUtil.createBuffer(2 * 3); //2 vertices, 3 coordinates per vertex
	}

	public void draw(float[] vpMatrix, float x1, float y1, float x2, float y2, float red,
					 float green, float blue, float alpha, float linewidth) {
		float[] data = vertexBuffer.getData();
		data[0] = x1;
		data[1] = y1;
		data[3] = x2;
		data[4] = y2;

		program.start(vpMatrix);

		program.bufferVertexPosition(vertexBuffer);
		program.uniformColor(red, green, blue, alpha);
		GLES20.glLineWidth(linewidth);
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2); //number of vertices

		program.end();
	}
}
