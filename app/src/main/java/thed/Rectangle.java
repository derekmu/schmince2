package thed;

public class Rectangle {
	public float x;
	public float y;
	public float w;
	public float h;

	public Rectangle() {
	}

	public float right() {
		return x + w;
	}

	public float bottom() {
		return y + h;
	}

	public boolean intersects(Rectangle location) {
		return x < location.right() && location.x < right() && y < location.bottom() && location.y < bottom();
	}

	public boolean contains(Rectangle location) {
		return x < location.x && right() > location.right() && y < location.y && bottom() > location.bottom();
	}

	public void center(Point point) {
		point.x = x + w / 2;
		point.y = y + h / 2;
	}

	public float centerX() {
		return x + w / 2f;
	}

	public float centerY() {
		return y + h / 2f;
	}

	public boolean contains(Point target) {
		return contains(target.x, target.y);
	}

	public boolean contains(float xx, float yy) {
		return xx >= x && xx < right() && yy >= y && yy < bottom();
	}

	public float size() {
		return w * h;
	}

	public void set(Rectangle rect) {
		this.x = rect.x;
		this.y = rect.y;
		this.w = rect.w;
		this.h = rect.h;
	}

	public void set(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
}