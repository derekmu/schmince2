package com.schmince.gldraw;

import android.opengl.GLES20;
import dopengl.shared.GLSimpleWobbleDrawer;
import thed.GLColor;

/**
 * @author Derek Mulvihill - Jan 22, 2014
 */
public class GLSurvivor extends GLSimpleWobbleDrawer {
	private static final GLColor defaultColor = new GLColor(0.233f, 0.218f, 0.531f);

	public GLSurvivor() {
		super(5, 1000, 0.05f);

		buffer.tvcb(-0.905, 0.104, 1.000, -0.059, -0.045, 1.000, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.004, 0.398, -0.559, -0.910, 0.627, -1.000, 0.233f, 0.218f, 0.531f)
				.tvcb(0.203, -0.251, 0.996, -0.345, 0.301, -0.561, 0.374f, 0.361f, 0.473f)
				.tvcb(-0.180, -0.285, -0.248, -0.575, -1.000, -0.348, 0.374f, 0.361f, 0.473f)
				.tvcb(-0.018, 0.653, -0.407, 0.175, 0.430, 0.092, 0.624f, 0.608f, 0.295f);
	}

	@Override
	public void draw(float[] vpMatrix, long seed) {
		draw(vpMatrix, seed, defaultColor);
	}

	public void draw(float[] vpMatrix, long seed, GLColor color) {
		buffer.updateVertices(seed);

		System.arraycopy(color.get(), 0, colorBuffer.getData(), 0, 12);
		System.arraycopy(color.get(), 0, colorBuffer.getData(), 12, 12);

		program.start(vpMatrix);

		program.bufferVertexPosition(vertexBuffer);
		program.bufferVertexColor(colorBuffer);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, buffer.getVertexCount());

		program.end();
	}
}
