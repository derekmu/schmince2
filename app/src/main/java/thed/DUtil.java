package thed;

/**
 * Random static functions.
 *
 * @author Derek Mulvihill - Aug 27, 2013
 */
public class DUtil {
	private static final float[] EQUILATERAL = {0f, 1f, 0f, //
			0.86f, -0.5f, 0f, //
			-0.86f, -0.5f, 0f //
	};

	/**
	 * Creates a 9 dimensional matrix with each 3 values being coordinates of a equilateral triangle with the points approximately on the unit circle.<br>
	 * The first point is at the top of the triangle, the second is the bottom right and the third is the bottom left.
	 */
	public static float[] getScaledEqualTriangle(float scale) {
		float[] retval = new float[EQUILATERAL.length];
		for (int i = 0; i < retval.length; i++) {
			retval[i] = EQUILATERAL[i] * scale;
		}
		return retval;
	}
}
