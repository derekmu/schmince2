package thed;

/**
 * A 2d vertex/point that 'wobbles'.
 * <br>
 * getX() and getY() return values that oscillate depending on the seed given.
 *
 * @author Derek Mulvihill - Aug 21, 2013
 */
public class Woblex {
	double x;
	double y;

	double loopTimeX = 0;
	double loopTimeY = 0;

	double changeX = 0;
	double changeY = 0;

	public Woblex(double x, double y, double wobbleTimeX, double wobbleTimeY,
				  double wobbleDistanceX, double wobbleDistanceY) {
		this.x = x;
		this.y = y;
		setWobble(wobbleTimeX, wobbleTimeY, wobbleDistanceX, wobbleDistanceY);
	}

	/**
	 * TL;DR: wobbleTimeX/wobbleTimeY are how many milliseconds to oscillate and wobbleDistanceX/wobbleDistanceY is how much the x/y values will change. <br>
	 * explanation of how these variables work:<br>
	 * TODO: these comments are out of date
	 * The seed value given to getX() and getY() should be constantly increasing, and we want to oscillate -1 up to 1 back to -1, etc...
	 * It should take a Z change in the seed to get back to the same value.<br>
	 * wobbleTimeX and wobbleTimeY are the Z.<br>
	 * <br>
	 * Do a mod/remainder of the milliseconds by Z, which will increase from 0 to Z but then it starts over at 0.
	 * It will take Z milliseconds for it to run between 0 and Z.<br>
	 * Multiply by 4 so it increases from 0 up to 4Z.<br>
	 * Subtract 2Z so it increases from -2Z up to 2Z.<br>
	 * Take an absolute value to get a proper/steady oscillation from 0 up to 2Z then down to 0, etc... This gets rid of the 'snap' between the 0 and the maximum.<br>
	 * Subtract Z so it oscillates between -Z and Z. <br>
	 * Divide by Z to oscillate between -1 and 1.<br>
	 * <br>
	 * Multiply by wobbleDistanceX/wobbleDistanceY to get the amount changed.
	 */
	public void setWobble(double wobbleTimeX, double wobbleTimeY, double wobbleDistanceX,
						  double wobbleDistanceY) {
		loopTimeX = wobbleTimeX;
		loopTimeY = wobbleTimeY;

		changeX = wobbleDistanceX;
		changeY = wobbleDistanceY;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public float getX(long seed) {
		return (float) (x + ((Math.abs(((seed % loopTimeX) / loopTimeX) - 0.5) - 0.25) * 4.0)
				* changeX);
	}

	public float getY(long seed) {
		return (float) (y + ((Math.abs(((seed % loopTimeX) / loopTimeX) - 0.5) - 0.25) * 4.0)
				* changeY);
	}
}
