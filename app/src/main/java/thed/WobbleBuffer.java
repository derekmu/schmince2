package thed;

/**
 * Wrapper to handle.
 *
 * @author Derek Mulvihill - Nov 11, 2013
 */
public class WobbleBuffer extends GLBuffer {
	private final Woblex[] woblices;
	private int triIndex = 0;

	public WobbleBuffer(int triangles, int wobtime, double wobdist) {
		super(triangles);
		woblices = new Woblex[triangles * 3];
		for (int n = 0; n < woblices.length; n++) {
			woblices[n] = new Woblex(0f, 0f, DRandom.get().nextInt(wobtime * 2) + wobtime, DRandom
					.get().nextInt(wobtime * 2) + wobtime, DRandom.get().nextDouble() * wobdist
					+ wobdist, DRandom.get().nextDouble() * wobdist + wobdist);
		}
	}

	public void updateVertices(long seed) {
		float[] vertices = getVertices();
		int n = 0;
		for (Woblex wob : woblices) {
			vertices[n++] = wob.getX(seed);
			vertices[n++] = wob.getY(seed);
			vertices[n++] = 0f;
		}
	}

	@Override
	public Tri t() {
		if (triIndex >= getTriangles()) {
			return null;
		}
		Tri tri = new WobTri(triIndex);
		triIndex++;
		return tri;
	}

	public class WobTri extends Tri {
		protected WobTri(int i) {
			super(i);
			this.i = i;
		}

		@Override
		public Tri v(double x0, double y0, double x1, double y1, double x2, double y2) {
			int n = i * 3;

			woblices[n].set(x0, y0);
			woblices[n + 1].set(x1, y1);
			woblices[n + 2].set(x2, y2);

			return this;
		}

		public WobTri v(Woblex wob1, Woblex wob2, Woblex wob3) {
			int n = i * 3;

			woblices[n] = wob1;
			woblices[n + 1] = wob2;
			woblices[n + 2] = wob3;

			return this;
		}
	}
}
