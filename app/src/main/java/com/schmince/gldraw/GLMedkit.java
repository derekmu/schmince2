package com.schmince.gldraw;

import dopengl.shared.GLSimpleBufferDrawer;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class GLMedkit extends GLSimpleBufferDrawer {
	public GLMedkit() {
		super(6);
		buffer.tvcb(-0.75f, 0.75f, 0.75f, 0.75f, -0.75f, -0.75f, 1f, 1f, 1f)
				.tvcb(-0.75f, -0.75f, 0.75f, -0.75f, 0.75f, 0.75f, 1f, 1f, 1f)
				.tvcb(-0.50f, 0.15f, -0.50f, -0.15f, 0.50f, -0.15f, 1f, 0f, 0f)
				.tvcb(-0.50f, 0.15f, 0.50f, 0.15f, 0.50f, -0.15f, 1f, 0f, 0f)
				.tvcb(0.15f, -0.50f, -0.15f, -0.50f, -0.15f, 0.50f, 1f, 0f, 0f)
				.tvcb(0.15f, -0.50f, 0.15f, 0.50f, -0.15f, 0.50f, 1f, 0f, 0f);
		vertexBuffer.pushStaticBuffer();
		colorBuffer.pushStaticBuffer();
	}

}
