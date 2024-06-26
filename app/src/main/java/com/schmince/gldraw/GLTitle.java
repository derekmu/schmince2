package com.schmince.gldraw;

import dopengl.shared.GLSimpleWobbleDrawer;
import thed.DRandom;

/**
 * @author Derek Mulvihill - Jan 23, 2014
 */
public class GLTitle extends GLSimpleWobbleDrawer {
	public GLTitle() {
		super(33, 1000, 0.005f);
		buffer.tvcb(-0.797, 0.769, -0.958, 0.898, -0.786, 0.989, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.961, 0.894, -0.925, 0.838, -0.976, 0.250, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.971, 0.220, -0.959, 0.434, -0.816, 0.006, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.811, -0.030, -0.848, 0.072, -0.854, -0.843, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.853, -0.843, -0.845, -0.552, -0.970, -0.889, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.962, -0.887, -0.932, -0.780, -1.000, -0.539, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.787, 0.964, -0.804, 0.777, -0.785, 0.511, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.525, 0.340, -0.626, 0.949, -0.630, 0.703, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.627, 0.998, -0.630, 0.717, -0.734, 0.484, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.742, 0.455, -0.712, 0.501, -0.714, -0.335, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.711, 0.139, -0.715, -0.712, -0.680, -0.750, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.716, -0.715, -0.598, -0.851, -0.604, -1.000, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.605, -0.970, -0.602, -0.839, -0.523, -0.546, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.482, 0.914, -0.436, 0.893, -0.459, -0.917, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.448, -0.157, -0.450, 0.247, -0.330, -0.004, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.352, -0.817, -0.316, -0.810, -0.332, 0.842, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.239, 0.988, -0.240, -0.854, -0.196, -0.882, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.235, 0.889, -0.223, 0.609, -0.110, -0.435, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.106, -0.462, -0.142, -0.154, -0.008, 0.920, 0.500f, 0.500f, 0.500f)
				.tvcb(-0.003, 0.907, -0.034, 0.717, 0.007, -0.882, 0.500f, 0.500f, 0.500f)
				.tvcb(0.132, 0.911, 0.136, -0.889, 0.189, -0.914, 0.500f, 0.500f, 0.500f)
				.tvcb(0.299, -0.873, 0.289, 0.859, 0.339, -0.919, 0.500f, 0.500f, 0.500f)
				.tvcb(0.288, 0.765, 0.310, 0.323, 0.453, -0.940, 0.500f, 0.500f, 0.500f)
				.tvcb(0.465, -0.966, 0.405, -0.589, 0.465, 0.691, 0.500f, 0.500f, 0.500f)
				.tvcb(0.739, 0.395, 0.627, 1.000, 0.627, 0.646, 0.500f, 0.500f, 0.500f)
				.tvcb(0.627, 0.988, 0.625, 0.624, 0.537, 0.149, 0.500f, 0.500f, 0.500f)
				.tvcb(0.536, 0.110, 0.567, 0.268, 0.575, -0.703, 0.500f, 0.500f, 0.500f)
				.tvcb(0.733, -0.599, 0.642, -0.681, 0.642, -0.981, 0.500f, 0.500f, 0.500f)
				.tvcb(0.643, -0.989, 0.644, -0.672, 0.565, -0.546, 0.500f, 0.500f, 0.500f)
				.tvcb(0.810, 1.000, 0.802, -0.965, 0.837, -0.957, 0.500f, 0.500f, 0.500f)
				.tvcb(0.810, 0.961, 1.000, 0.951, 0.998, 0.725, 0.500f, 0.500f, 0.500f)
				.tvcb(0.828, -0.018, 0.820, 0.308, 0.935, 0.130, 0.500f, 0.500f, 0.500f)
				.tvcb(0.808, -0.944, 0.997, -0.955, 0.998, -0.683, 0.500f, 0.500f, 0.500f);

		float[] colors = buffer.getColors();
		for (int i = 0; i < colors.length; i += 12) {
			colors[i] = colors[i + 4] = colors[i + 8] = 0.2f + DRandom.get().nextFloat() * 0.8f;
			colors[i + 1] = colors[i + 5] = colors[i + 9] = 0.2f + DRandom.get().nextFloat() * 0.8f;
			colors[i + 2] = colors[i + 6] = colors[i + 10] = 0.2f + DRandom.get().nextFloat() * 0.8f;
		}
		colorBuffer.pushStaticBuffer();
	}
}
