package dopengl.shapes;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.programs.LineProgram;

/**
 * Simple OpenGL rectangle shape.
 *
 * @author Derek Mulvihill
 */
public class GLRectangle {
	private LineProgram program;

	private GLFloatBuffer vertexBuffer;

	public GLRectangle() {
		program = new LineProgram();

		vertexBuffer = OpenGLUtil.createBuffer(4 * 3); //4 corners * 3 coordinates per corner
	}

	public void start(float[] vpMatrix) {
		program.start(vpMatrix);
	}

	public void end() {
		program.end();
	}

	public void draw(float[] vpMatrix, float red, float green, float blue, float alpha) {
		start(vpMatrix);

		drawOne(red, green, blue, alpha);

		end();
	}

	public void drawOne(float red, float green, float blue, float alpha) {
		program.bufferVertexPosition(vertexBuffer);
		program.uniformColor(red, green, blue, alpha);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4); //number of vertices
	}

	public void setBounds(float x, float y, float width, float height) {
		float[] vertexData = vertexBuffer.getData();
		vertexData[0] = x;
		vertexData[1] = y + height;
		vertexData[3] = x + width;
		vertexData[4] = y + height;
		vertexData[6] = x;
		vertexData[7] = y;
		vertexData[9] = x + width;
		vertexData[10] = y;
	}
}