package com.schmince.gldraw;

import dopengl.shared.GLSimpleBufferDrawer;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class GLFlare extends GLSimpleBufferDrawer {
	public GLFlare() {
		super(8);
		buffer.tvcb(0.095, 0.329, -1.000, -0.734, -0.742, -0.979, 0.968f, 0.000f, 0.000f)
				.tvcb(0.095, 0.340, -0.741, -1.000, 0.454, 0.149, 0.968f, 0.000f, 0.000f)
				.tvcb(0.166, 0.262, 0.692, 0.738, 0.283, 0.155, 0.460f, 0.458f, 0.450f)
				.tvcb(0.019, 0.244, 0.104, 0.340, 0.550, 0.088, 0.983f, 0.465f, 0.000f)
				.tvcb(-0.992, -0.727, -0.692, -0.993, -0.853, -0.632, 0.983f, 0.465f, 0.000f)
				.tvcb(0.661, 0.665, 0.528, 0.969, 0.717, 0.687, 0.983f, 0.992f, 0.000f)
				.tvcb(0.703, 0.714, 0.940, 1.000, 0.674, 0.648, 0.983f, 0.992f, 0.000f)
				.tvcb(0.711, 0.657, 1.000, 0.679, 0.674, 0.611, 0.983f, 0.992f, 0.000f);

		vertexBuffer.pushStaticBuffer();
		colorBuffer.pushStaticBuffer();
	}
}
