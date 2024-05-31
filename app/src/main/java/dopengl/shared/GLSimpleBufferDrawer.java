package dopengl.shared;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.drawable.GLDrawable;
import dopengl.programs.ShapeProgram;
import thed.GLBuffer;

/**
 * @author Derek Mulvihill - Nov 21, 2013
 */
public class GLSimpleBufferDrawer implements GLDrawable {
	protected final ShapeProgram program;

	protected final GLBuffer buffer;

	protected final GLFloatBuffer vertexBuffer;
	protected final GLFloatBuffer colorBuffer;

	public GLSimpleBufferDrawer(int triangles) {
		program = new ShapeProgram();

		buffer = new GLBuffer(triangles);

		vertexBuffer = OpenGLUtil.createBuffer(buffer.getVertices());
		colorBuffer = OpenGLUtil.createBuffer(buffer.getColors());
		colorBuffer.setStepSize(4);
	}

	@Override
	public void draw(float[] vpMatrix) {
		program.start(vpMatrix);

		program.staticVertexPosition(vertexBuffer);
		program.staticVertexColor(colorBuffer);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, buffer.getVertexCount());

		program.end();
	}
}