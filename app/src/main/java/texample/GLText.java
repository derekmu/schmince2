package texample;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.Matrix;
import dopengl.programs.BatchTextProgram;
import thed.Rectangle;

/**
 * This is a OpenGL ES dynamic font rendering system.
 * <p>
 * It loads actual font files, generates a font map (texture) from them, and allows rendering of text strings.
 * <p>
 * NOTE: the rendering portions of this class uses a sprite batcher in order provide decent speed rendering.
 * Rendering assumes a BOTTOM-LEFT origin, and the (x,y) positions are relative to that, as well as the bottom-left of the string to render.
 */
public class GLText {
	public final static int CHAR_START = 32; // First Character (space)
	public final static int CHAR_END = 126; // Last Character (tilde)
	public final static int CHAR_CNT = (((CHAR_END - CHAR_START) + 1) + 1); // Character Count (Including Character to use for Unknown)
	public final static int CHAR_UNKNOWN = (CHAR_CNT - 1); // Index of the Unknown Character
	public final static int CHAR_NONE = 32; // Character to Use for Unknown (space)
	public final static int FONT_SIZE_MIN = 6; // Minumum Font Size (Pixels)
	public final static int FONT_SIZE_MAX = 180; // Maximum Font Size (Pixels)

	public final static int CHAR_BATCH_SIZE = 24; // Number of Characters to Render Per Batch must be the same as the size of u_MVPMatrix in BatchTextProgram (so this should be moved
	private final float[] charWidths; // Width of Each Character (Actual; Pixels)
	private AssetManager assets; // Asset Manager
	private SpriteBatch batch; // Batch Renderer
	private int fontPadX, fontPadY; // Font Padding (Pixels; On Each Side, ie. Doubled on Both X+Y Axis)
	private float fontHeight; // Font Height (Actual; Pixels)
	private int textureId; // Font Texture ID
	private int textureSize; // Texture Size for Font (Square)
	private float charWidthMax; // Character Width (Maximum; Pixels)
	private float charHeight; // Character Height (Maximum; Pixels)
	private TextureRegion[] charRgn; // Region of Each Character (Texture Coordinates)
	private int cellWidth, cellHeight; // Character Cell Width/Height

	private float scaleX, scaleY; // Font Scale (X,Y Axis)
	private float spaceX; // Additional (X,Y Axis) Spacing (Unscaled)

	private BatchTextProgram program; // OpenGL Program object

	public GLText(AssetManager assets) {
		this.assets = assets;

		program = new BatchTextProgram();
		batch = new SpriteBatch(CHAR_BATCH_SIZE, program);

		charWidths = new float[CHAR_CNT]; // Create the Array of Character Widths
		charRgn = new TextureRegion[CHAR_CNT]; // Create the Array of Character Regions

		fontPadX = 0;
		fontPadY = 0;

		fontHeight = 0.0f;

		textureId = -1;
		textureSize = 0;

		charWidthMax = 0;
		charHeight = 0;

		cellWidth = 0;
		cellHeight = 0;

		scaleX = 1.0f;
		scaleY = 1.0f;
		spaceX = 0.0f;
	}

	/**
	 * This will load the specified font file, create a texture for the defined character range, and setup all required values used to render with it.
	 *
	 * @param file Filename of the font (.ttf, .otf) to use. In 'Assets' folder.
	 * @param size Requested pixel size of font (height)
	 * @param padX Extra padding per character to prevent overlapping characters.
	 * @param padY Extra padding per character to prevent overlapping characters.
	 */
	public void load(String file, int size, int padX, int padY) {
		fontPadX = padX; // Set Requested X Axis Padding
		fontPadY = padY; // Set Requested Y Axis Padding

		// load the font and setup paint instance for drawing
		Typeface tf = Typeface.createFromAsset(assets, file); // Create the Typeface from Font File
		Paint paint = new Paint(); // Create Android Paint Instance
		paint.setAntiAlias(true); // Enable Anti Alias
		paint.setTextSize(size); // Set Text Size
		paint.setColor(0xffffffff); // Set ARGB (White, Opaque)
		paint.setTypeface(tf); // Set Typeface

		// get font metrics
		Paint.FontMetrics fm = paint.getFontMetrics(); // Get Font Metrics

		//NOTES: using the ascent/descent because fonts include some extra space above the characters (presumably so printing multiple lines don't get too close?)
		//the ascent is negative from the fontMetrics for some reason, I don't know why
		//default logic here: (float) Math.ceil(Math.abs(fm.bottom) + Math.abs(fm.top));
		fontHeight = -fm.ascent + fm.descent;

		// determine the width of each character (including unknown character)
		// also determine the maximum character width
		char[] s = new char[2]; // Create Character Array
		charWidthMax = charHeight = 0; // Reset Character Width/Height Maximums
		float[] w = new float[2]; // Working Width Value
		int cnt = 0; // Array Counter
		for (char c = CHAR_START; c <= CHAR_END; c++) { // FOR Each Character
			s[0] = c; // Set Character
			paint.getTextWidths(s, 0, 1, w); // Get Character Bounds
			charWidths[cnt] = w[0]; // Get Width
			if (charWidths[cnt] > charWidthMax) // IF Width Larger Than Max Width
				charWidthMax = charWidths[cnt]; // Save New Max Width
			cnt++; // Advance Array Counter
		}
		s[0] = CHAR_NONE; // Set Unknown Character
		paint.getTextWidths(s, 0, 1, w); // Get Character Bounds
		charWidths[cnt] = w[0]; // Get Width
		if (charWidths[cnt] > charWidthMax) {
			charWidthMax = charWidths[cnt]; // Save New Max Width
		}

		// set character height to font height
		charHeight = fontHeight; // Set Character Height

		// find the maximum size, validate, and setup cell sizes
		cellWidth = (int) charWidthMax + (2 * fontPadX); // Set Cell Width
		cellHeight = (int) charHeight + (2 * fontPadY); // Set Cell Height
		int maxSize = Math.max(cellWidth, cellHeight); // Save Max Size (Width/Height)
		if (maxSize < FONT_SIZE_MIN || maxSize > FONT_SIZE_MAX) {
			return;
		}

		// set texture size based on max font size (width or height)
		// NOTE: these values are fixed, based on the defined characters. when
		// changing start/end characters (CHAR_START/CHAR_END) this will need adjustment too!
		if (maxSize <= 24) {
			textureSize = 256;
		} else if (maxSize <= 40) {
			textureSize = 512;
		} else if (maxSize <= 80) {
			textureSize = 1024;
		} else {
			textureSize = 2048;
		}

		// create an empty bitmap (alpha only)
		Bitmap bitmap = Bitmap.createBitmap(textureSize, textureSize, Bitmap.Config.ALPHA_8); // Create Bitmap
		Canvas canvas = new Canvas(bitmap); // Create Canvas for Rendering to Bitmap
		bitmap.eraseColor(0x00000000); // Set Transparent Background (ARGB)

		// render each of the characters to the canvas (ie. build the font map)
		float x = fontPadX; // Set Start Position (X)
		float y = (cellHeight - 1) - (float) Math.ceil(Math.abs(fm.descent)) - fontPadY; // Set Start Position (Y)
		for (char c = CHAR_START; c <= CHAR_END; c++) { // FOR Each Character
			s[0] = c; // Set Character to Draw
			canvas.drawText(s, 0, 1, x, y, paint); // Draw Character
			x += cellWidth; // Move to Next Character
			if ((x + cellWidth - fontPadX) > textureSize) { // IF End of Line Reached
				x = fontPadX; // Set X for New Row
				y += cellHeight; // Move Down a Row
			}
		}
		s[0] = CHAR_NONE; // Set Character to Use for NONE
		canvas.drawText(s, 0, 1, x, y, paint); // Draw Character

		// save the bitmap in a texture
		textureId = TextureHelper.loadTexture(bitmap);

		// set up the array of character texture regions
		x = 0; // Initialize X
		y = 0; // Initialize Y
		for (int c = 0; c < CHAR_CNT; c++) { // FOR Each Character (On Texture)
			charRgn[c] = new TextureRegion(textureSize, textureSize, x, y, cellWidth - 1,
					cellHeight - 1); // Create Region for Character
			x += cellWidth; // Move to Next Char (Cell)
			if (x + cellWidth > textureSize) {
				x = 0; // Reset X Position to Start
				y += cellHeight; // Move to Next Row (Cell)
			}
		}
	}

	/**
	 * Call this method before all draw() calls using a text instance<br>
	 * NOTE: color is set on a per-batch basis, and fonts should be 8-bit alpha only!!!
	 *
	 * @param red      RGB values for font
	 * @param green    RGB values for font
	 * @param blue     RGB values for font
	 * @param alpha    alpha value for font
	 * @param vpMatrix View and projection matrix to use
	 */
	public void begin(float red, float green, float blue, float alpha, float[] vpMatrix) {
		program.start(vpMatrix);

		program.useColor(red, green, blue, alpha);
		program.useTexture(textureId);

		batch.beginBatch(vpMatrix);
	}

	/**
	 * Draw text at the specified x,y position
	 *
	 * @param text     the string to draw
	 * @param x        the x position to draw text at (bottom left of text; including descent)
	 * @param y        the x position to draw text at (bottom left of text; including descent)
	 * @param angleDeg angle to rotate the text
	 */
	public void draw(CharSequence text, float x, float y, float angleDeg) {
		draw(text, x, y, angleDeg, null);
	}

	/**
	 * Draw text at the specified x,y position
	 *
	 * @param text       the string to draw
	 * @param x          the x position to draw text at (bottom left of text; including descent)
	 * @param y          the y position to draw text at (bottom left of text; including descent)
	 * @param angleDeg   angle to rotate the text
	 * @param clipBounds optional clip bounds for where the text should be cut off, in orthographic coordinates
	 */
	public void draw(CharSequence text, float x, float y, float angleDeg, Rectangle clipBounds) {
		float chrHeight = cellHeight * scaleY; // Calculate Scaled Character Height
		float chrWidth = cellWidth * scaleX; // Calculate Scaled Character Width
		int len = text.length(); // Get String Length
		x += (chrWidth / 2.0f) - (fontPadX * scaleX); // Adjust Start X
		y += (chrHeight / 2.0f) - (fontPadY * scaleY); // Adjust Start Y

		if (clipBounds == null) {
			program.setClipBounds(-999999, -999999, 999999, 999999);
		} else {
			program.setClipBounds(clipBounds.x - x, clipBounds.y - y, clipBounds.right() - x,
					clipBounds.bottom() - y);
		}

		// create a model matrix based on x, y and angleDeg
		float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, y, 0);
		Matrix.rotateM(modelMatrix, 0, angleDeg, 0, 0, 1);

		float letterX, letterY;
		letterX = letterY = 0;

		for (int i = 0; i < len; i++) { // FOR Each Character in String
			int c = (int) text.charAt(i) - CHAR_START; // Calculate Character Index (Offset by First Char in Font)
			if (c < 0 || c >= CHAR_CNT) // IF Character Not In Font
				c = CHAR_UNKNOWN; // Set to Unknown Character Index
			batch.drawSprite(letterX, letterY, chrWidth, chrHeight, charRgn[c], modelMatrix); // Draw the Character
			letterX += (charWidths[c] + spaceX) * scaleX; // Advance X Position by Scaled Character Width
		}
	}

	/**
	 * Call this method after all draw() calls using a text instance.
	 */
	public void end() {
		batch.endBatch();
		program.end();
	}

	/**
	 * Set the scaling to use for the font.
	 *
	 * @param sx scale for x axis
	 * @param sy scale for y axis
	 */
	public void setScale(float sx, float sy) {
		scaleX = sx;
		scaleY = sy;
	}

	/**
	 * Return the length of the specified string if rendered using current settings
	 *
	 * @param text the string to get length for
	 * @return the length of the specified string (pixels)
	 */
	public float getLength(CharSequence text) {
		float len = 0.0f; // Working Length
		int strLen = text.length(); // Get String Length (Characters)
		for (int i = 0; i < strLen; i++) { // For Each Character in String (Except Last
			int c = (int) text.charAt(i) - CHAR_START; // Calculate Character Index (Offset by First Char in Font)
			len += (charWidths[c] * scaleX); // Add Scaled Character Width to Total Length
		}
		len += (strLen > 1 ? ((strLen - 1) * spaceX) * scaleX : 0); // Add Space Length
		return len; // Return Total Length
	}


	public float getHeight() {
		return (fontHeight * scaleY); // Return Font Height (Actual)
	}
}
