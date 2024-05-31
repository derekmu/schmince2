package thed;

/**
 * Wrapper to handle.
 *
 * @author Derek Mulvihill - Nov 11, 2013
 */
public class GLBuffer {
	private final int triangles;
	private final float[] vertices;
	private final float[] colors;
	private int triIndex = 0;

	public GLBuffer(int triangles) {
		this.triangles = triangles;
		vertices = new float[triangles * 3 * 3];
		colors = new float[triangles * 3 * 4];
	}

	public Tri t() {
		if (triIndex >= triangles) {
			return null;
		}
		Tri tri = new Tri(triIndex);
		triIndex++;
		return tri;
	}

	public GLBuffer tvcb(double x0, double y0, double x1, double y1, double x2, double y2, float r,
						 float g, float b) {
		return t().v(x0, y0, x1, y1, x2, y2).c(r, g, b).b();
	}

	public int getTriangles() {
		return triangles;
	}

	public int getVertexCount() {
		return triangles * 3;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getColors() {
		return colors;
	}

	public class Tri {
		protected int i;

		protected Tri(int i) {
			this.i = i;
		}

		public Tri v(double x0, double y0, double x1, double y1, double x2, double y2) {
			int n = i * 3 * 3;
			vertices[n] = (float) x0;
			vertices[n + 1] = (float) y0;
			vertices[n + 2] = (float) 0;

			vertices[n + 3] = (float) x1;
			vertices[n + 4] = (float) y1;
			vertices[n + 5] = (float) 0;

			vertices[n + 6] = (float) x2;
			vertices[n + 7] = (float) y2;
			vertices[n + 8] = (float) 0;

			return this;
		}

		public Tri c(float r, float g, float b) {
			return c(r, g, b, 1);
		}

		public Tri c(float r, float g, float b, float a) {
			int n = i * 3 * 4;

			colors[n] = r;
			colors[n + 1] = g;
			colors[n + 2] = b;
			colors[n + 3] = a;

			colors[n + 4] = r;
			colors[n + 5] = g;
			colors[n + 6] = b;
			colors[n + 7] = a;

			colors[n + 8] = r;
			colors[n + 9] = g;
			colors[n + 10] = b;
			colors[n + 11] = a;

			return this;
		}

		public Tri c(GLColor color) {
			int n = i * 3 * 4;

			System.arraycopy(color.get(), 0, colors, n, 12);

			return this;
		}

		public Tri c(float[] colorers) {
			int n = i * 3 * 4;

			System.arraycopy(colorers, 0, colors, n, 12);

			return this;
		}

		public GLBuffer b() {
			return GLBuffer.this;
		}
	}
}
