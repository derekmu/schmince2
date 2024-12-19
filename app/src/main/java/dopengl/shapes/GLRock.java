package dopengl.shapes;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.programs.LineProgram;

/**
 * Simple OpenGL polygon shape, resembling a rock.
 *
 * @author Derek Mulvihill
 */
public class GLRock {
	private LineProgram program;

	private GLFloatBuffer vertexBuffer;
	private int vertexCount;

	public GLRock() {
		program = new LineProgram();

		// Initialize with enough space for 10 vertices (modifiable as needed)
		vertexBuffer = OpenGLUtil.createBuffer(20 * 3); // 10 vertices * 3 coordinates per vertex
		vertexCount = 0;
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
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
	}

	public void setPolygon(float x, float y, float radius, int sides, double angleOffset) {
		sides = Math.max(Math.min(sides, 13), 3);

		float[] vertexData = vertexBuffer.getData();
		vertexCount = sides + 2; // Center vertex + 1 vertex per side + closing vertex

		// Center vertex of the fan
		vertexData[0] = x;
		vertexData[1] = y;
		vertexData[2] = 0.0f;

		// Calculate the vertices around the center
		double angleStep = 2 * Math.PI / sides;
		for (int i = 0; i <= sides; i++) {
			double angle = angleOffset + i * angleStep;
			float vx = x + (float) (Math.cos(angle) * radius);
			float vy = y + (float) (Math.sin(angle) * radius);

			int offset = (i + 1) * 3;
			vertexData[offset] = vx;
			vertexData[offset + 1] = vy;
			vertexData[offset + 2] = 0.0f;
		}
	}
}
