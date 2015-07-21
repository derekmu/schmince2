package thed;

/**
 * Random static functions.
 *
 * @author Derek Mulvihill - Aug 27, 2013
 */
public class DUtil {
    public static float triangleSize(float p0x, float p0y, float p1x, float p1y, float p2x,
                                     float p2y) {
        return Math.abs(0.5f * (p0x * (p1y - p2y) + p1x * (p2y - p0y) + p2x * (p0y - p1y))); //why? i don't know
    }

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
