package dopengl;

import android.content.Context;
import dopengl.drawable.GLDrawable;
import dopengl.drawable.GLDrawableProvider;
import dopengl.shapes.*;
import texample.GLText;
import texample.GLTextCache;
import texample.GLTextType;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle instantiation of GL shapes, icons, text, etc...
 *
 * @author Derek Mulvihill - Aug 14, 2013
 */
public class GLLibrary implements GLTextCache {
	private Map<GLTextType, GLText> fontText = new HashMap<>();
	private Map<GLDrawableProvider, GLDrawable> drawables = new HashMap<>();

	private GLLine line = null;
	private GLTriangle triangle = null;
	private GLTriangleUniform triangleUniform = null;
	private GLRectangle rectangle = null;
	private GLTriangleSet triangleset = null;

	private Context context;

	public GLLibrary(Context context) {
		this.context = context;
	}

	public GLLine getLine() {
		if (line == null) {
			line = new GLLine();
		}
		return line;
	}

	public GLTriangle getTriangle() {
		if (triangle == null) {
			triangle = new GLTriangle();
		}
		return triangle;
	}

	public GLTriangleUniform getTriangleUniform() {
		if (triangleUniform == null) {
			triangleUniform = new GLTriangleUniform();
		}
		return triangleUniform;
	}

	public GLRectangle getRectangle() {
		if (rectangle == null) {
			rectangle = new GLRectangle();
		}
		return rectangle;
	}

	public GLTriangleSet getTriangleSet() {
		if (triangleset == null) {
			triangleset = new GLTriangleSet();
		}
		return triangleset;
	}

	public GLDrawable getDrawer(GLDrawableProvider provider) {
		if (!drawables.containsKey(provider)) {
			try {
				drawables.put(provider, provider.getDrawClass().getDeclaredConstructor().newInstance());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		return drawables.get(provider);
	}

	public GLText getText(GLTextType type, float scale) {
		if (fontText.containsKey(type)) {
			GLText text = fontText.get(type);
			assert text != null;
			text.setScale(scale, scale);
			return text;
		}
		GLText glText = new GLText(context.getAssets());
		glText.load(type.FileName, 50, 10, 10);
		glText.setScale(scale, scale);
		fontText.put(type, glText);
		return glText;
	}
}
