package thed;

/**
 * Simple wrapper around a 12 float matrix need for OpenGL.<br>
 * The purpose is just to save the effort of figuring and typing out that index 0 = 4 = 8, 1 = 5 = 9, 2 = 6 = 10, and 3 = 7 = 11 for rgba.
 *
 * @author Derek Mulvihill - Sep 9, 2013
 */
public class GLColor {
	private final float[] colors = new float[12];

	public GLColor(float r, float g, float b) {
		set(r, g, b, 1f);
	}

	public GLColor(float r, float g, float b, float a) {
		set(r, g, b, a);
	}

	public static GLColor random() {
		return new GLColor(DRandom.get().nextFloat(), DRandom.get().nextFloat(), DRandom.get()
				.nextFloat());
	}

	public static GLColor random(float min) {
		float diff = 1 - min;
		return new GLColor(min + DRandom.get().nextFloat() * diff, min + DRandom.get().nextFloat()
				* diff, min + DRandom.get().nextFloat() * diff);
	}

	public float[] get() {
		return colors;
	}

	public GLColor set(int i, float r, float g, float b) {
		set(i, r, g, b, 1f);
		return this;
	}

	public GLColor set(int i, float r, float g, float b, float a) {
		int n = i * 4;
		colors[n] = r;
		colors[n + 1] = g;
		colors[n + 2] = b;
		colors[n + 3] = a;
		return this;
	}

	public void set(float r, float g, float b) {
		set(r, g, b, 1f);
	}

	public void set(float r, float g, float b, float a) {
		colors[0] = colors[4] = colors[8] = r;
		colors[1] = colors[5] = colors[9] = g;
		colors[2] = colors[6] = colors[10] = b;
		colors[3] = colors[7] = colors[11] = a;
	}

	public void set(GLColor color) {
		System.arraycopy(color.colors, 0, colors, 0, colors.length);
	}
}
