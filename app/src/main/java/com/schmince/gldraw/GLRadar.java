package com.schmince.gldraw;

import dopengl.shared.GLSimpleBufferDrawer;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class GLRadar extends GLSimpleBufferDrawer {
	public GLRadar() {
		super(14);
		buffer.tvcb(-0.399, 0.839, -0.356, 0.977, 0.583, 0.731, 0.000f, 0.000f, 0.000f)
				.tvcb(0.397, 0.757, 0.594, 0.736, 0.963, 0.030, 0.000f, 0.000f, 0.000f)
				.tvcb(0.849, 0.129, 1.000, -0.010, 0.601, -0.835, 0.000f, 0.000f, 0.000f)
				.tvcb(0.605, -0.854, 0.640, -0.618, -0.001, -1.000, 0.000f, 0.000f, 0.000f)
				.tvcb(-0.005, -0.997, 0.199, -0.856, -0.605, -0.871, 0.000f, 0.000f, 0.000f)
				.tvcb(-0.561, -0.899, -0.371, -0.926, -1.000, -0.332, 0.000f, 0.000f, 0.000f)
				.tvcb(-0.985, -0.300, -0.854, -0.431, -0.905, 0.246, 0.000f, 0.000f, 0.000f)
				.tvcb(-0.898, 0.117, -0.946, 0.298, -0.330, 1.000, 0.000f, 0.000f, 0.000f)
				.tvcb(0.141, 0.183, 0.220, 0.380, 0.350, 0.155, 0.957f, 0.000f, 0.000f)
				.tvcb(-0.547, -0.444, -0.541, -0.207, -0.353, -0.454, 0.000f, 0.000f, 0.996f)
				.tvcb(0.016, -0.516, 0.009, -0.252, 0.312, -0.481, 0.961f, 0.992f, 0.000f)
				.tvcb(-0.515, 0.035, -0.356, 0.345, -0.216, 0.006, 0.000f, 0.992f, 0.992f)
				.tvcb(0.316, -0.231, 0.391, -0.050, 0.535, -0.301, 0.000f, 0.992f, 0.000f)
				.tvcb(-0.325, -0.659, -0.190, -0.468, -0.076, -0.682, 0.994f, 0.000f, 0.985f);

		vertexBuffer.pushStaticBuffer();
		colorBuffer.pushStaticBuffer();
	}
}
