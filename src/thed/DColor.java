package thed;

/**
 * RGBA color object.
 *
 * @author Derek Mulvihill - Oct 27, 2013
 */
public class DColor {
    public float Red = 1f;
    public float Green = 1f;
    public float Blue = 1f;
    public float Alpha = 1f;

    public DColor() {
    }

    public DColor(float r, float g, float b) {
        this.Red = r;
        this.Green = g;
        this.Blue = b;
    }

    public DColor(float r, float g, float b, float a) {
        this.Red = r;
        this.Green = g;
        this.Blue = b;
        this.Alpha = a;
    }

    public void set(float r, float g, float b) {
        this.Red = r;
        this.Green = g;
        this.Blue = b;
    }

    public void set(float r, float g, float b, float a) {
        this.Red = r;
        this.Green = g;
        this.Blue = b;
        this.Alpha = a;
    }

    public void set(DColor color) {
        this.Red = color.Red;
        this.Green = color.Green;
        this.Blue = color.Blue;
        this.Alpha = color.Alpha;
    }
}
