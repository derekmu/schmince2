package dopengl.shapes;

import android.opengl.GLES20;
import dopengl.GLFloatBuffer;
import dopengl.OpenGLUtil;
import dopengl.programs.ShapeProgram;
import thed.DRandom;
import thed.DTimer;
import thed.GLColor;
import thed.Woblex;

/**
 * OpenGL hexagon shape made of 24 connected triangles with wobbling vertices.
 * The edges of the hexagon are +-1 on the x and y axes.
 *
 * @author Derek Mulvihill
 */
public class GLHexagon {
    private ShapeProgram program;

    private GLFloatBuffer vertexBuffer;
    //private GLFloatBuffer lineVertexBuffer;
    private GLFloatBuffer colorBuffer;
    //private GLFloatBuffer lineColorBuffer;

    //19 vertices
    private Woblex[] vertices = new Woblex[]{
            new Woblex(-0.5f, -1.0f),
            new Woblex(0.0f, -1.0f),
            new Woblex(0.5f, -1.0f), //top/first row
            new Woblex(-0.75f, -0.5f),
            new Woblex(-0.25f, -0.5f),
            new Woblex(0.25f, -0.5f),
            new Woblex(0.75f, -0.5f), //second row
            new Woblex(-1.0f, 0.0f), new Woblex(-0.5f, 0.0f), new Woblex(0.0f, 0.0f),
            new Woblex(0.5f, 0.0f),
            new Woblex(1.0f, 0.0f), //middle/third row
            new Woblex(-0.75f, 0.5f), new Woblex(-0.25f, 0.5f), new Woblex(0.25f, 0.5f),
            new Woblex(0.75f, 0.5f), //fourth row
            new Woblex(-0.5f, 1.0f), new Woblex(0.0f, 1.0f), new Woblex(0.5f, 1.0f), //bottom/fifth row
    };
    //24 sections
    private Section[] sections = new Section[]{
            new Section(vertices[0], vertices[3], vertices[4]),
            new Section(vertices[0], vertices[4], vertices[1]),
            new Section(vertices[1], vertices[4], vertices[5]),
            new Section(vertices[1], vertices[5], vertices[2]),
            new Section(vertices[2], vertices[5], vertices[6]),//top row
            new Section(vertices[3], vertices[7], vertices[8]),
            new Section(vertices[3], vertices[8], vertices[4]),
            new Section(vertices[4], vertices[8], vertices[9]),
            new Section(vertices[4], vertices[9], vertices[5]),
            new Section(vertices[5], vertices[9], vertices[10]),
            new Section(vertices[5], vertices[10], vertices[6]),
            new Section(vertices[6], vertices[10], vertices[11]),//second row
            new Section(vertices[7], vertices[12], vertices[8]),
            new Section(vertices[8], vertices[12], vertices[13]),
            new Section(vertices[8], vertices[13], vertices[9]),
            new Section(vertices[9], vertices[13], vertices[14]),
            new Section(vertices[9], vertices[14], vertices[10]),
            new Section(vertices[10], vertices[14], vertices[15]),
            new Section(vertices[10], vertices[15], vertices[11]),//third row
            new Section(vertices[12], vertices[16], vertices[13]),
            new Section(vertices[13], vertices[16], vertices[17]),
            new Section(vertices[13], vertices[17], vertices[14]),
            new Section(vertices[14], vertices[17], vertices[18]),
            new Section(vertices[14], vertices[18], vertices[15]),//bottom row
    };
    //42 lines
    /*
	private Line[] lines = new Line[] { new Line(vertices[0], vertices[3]),
			new Line(vertices[0], vertices[4]), new Line(vertices[0], vertices[1]),
			new Line(vertices[1], vertices[4]), new Line(vertices[1], vertices[5]),
			new Line(vertices[1], vertices[2]), new Line(vertices[2], vertices[5]),
			new Line(vertices[2], vertices[6]), new Line(vertices[3], vertices[7]),
			new Line(vertices[3], vertices[8]), new Line(vertices[3], vertices[4]),
			new Line(vertices[4], vertices[8]), new Line(vertices[4], vertices[9]),
			new Line(vertices[4], vertices[5]), new Line(vertices[5], vertices[9]),
			new Line(vertices[5], vertices[10]), new Line(vertices[5], vertices[6]),
			new Line(vertices[6], vertices[10]), new Line(vertices[6], vertices[11]),
			new Line(vertices[7], vertices[12]), new Line(vertices[7], vertices[8]),
			new Line(vertices[8], vertices[12]), new Line(vertices[8], vertices[13]),
			new Line(vertices[8], vertices[9]), new Line(vertices[9], vertices[13]),
			new Line(vertices[9], vertices[14]), new Line(vertices[9], vertices[10]),
			new Line(vertices[10], vertices[14]), new Line(vertices[10], vertices[15]),
			new Line(vertices[10], vertices[11]), new Line(vertices[11], vertices[15]),
			new Line(vertices[12], vertices[16]), new Line(vertices[12], vertices[13]),
			new Line(vertices[13], vertices[16]), new Line(vertices[13], vertices[17]),
			new Line(vertices[13], vertices[14]), new Line(vertices[14], vertices[17]),
			new Line(vertices[14], vertices[18]), new Line(vertices[14], vertices[15]),
			new Line(vertices[15], vertices[18]), new Line(vertices[16], vertices[17]),
			new Line(vertices[17], vertices[18]) };
	*/

    private boolean colorDataDirty = true;

    public GLHexagon() {
        program = new ShapeProgram();

        for (int i = 0; i < vertices.length; i++) {
            Woblex wob = vertices[i];
            wob.setWobble(DRandom.get().nextInt(2000) + 1000, DRandom.get().nextInt(2000) + 1000,
                    DRandom.get().nextFloat() * 0.05f + 0.05f,
                    DRandom.get().nextFloat() * 0.05f + 0.05f);
        }

        vertexBuffer = OpenGLUtil.createBuffer(sections.length * 3 * 3); //3 vertices per section, 3 coordinates per vertex
        //lineVertexBuffer = OpenGLUtil.createBuffer(lines.length * 2 * 3); // 2 vertices per line, 3 coordinates per vertex

        colorBuffer = OpenGLUtil.createBuffer(sections.length * 3 * 4); //3 vertices per section, 4 color values per vertex
        colorBuffer.setStepSize(4);

		/*
		lineColorBuffer = OpenGLUtil.createBuffer(lines.length * 2 * 4); //2 vertices per line, 4 color values per vertex
		float[] color = lineColorBuffer.getData();
		for (int i = 3; i < color.length; i += 4) {
			color[i] = 1f;
		}
		lineColorBuffer.setStepSize(4);
		lineColorBuffer.pushStaticBuffer();
		*/
    }

    public void draw(float[] vpMatrix) {
        float[] vertexData = vertexBuffer.getData();
        int n = 0;
        long seed = DTimer.get().millis();
        int size = sections.length;
        for (int i = 0; i < size; i++) {
            Section section = sections[i];
            Woblex v = section.vertices[0];
            vertexData[n++] = v.getX(seed);
            vertexData[n++] = v.getY(seed);
            n++;

            v = section.vertices[1];
            vertexData[n++] = v.getX(seed);
            vertexData[n++] = v.getY(seed);
            n++;

            v = section.vertices[2];
            vertexData[n++] = v.getX(seed);
            vertexData[n++] = v.getY(seed);
            n++;
        }

		/*
		float[] lineVertexData = lineVertexBuffer.getData();
		n = 0;
		size = lines.length;
		for (int i = 0; i < size; i++) {
			Line line = lines[i];
			Woblex v = line.vertices[0];
			lineVertexData[n++] = v.getX(seed);
			lineVertexData[n++] = v.getY(seed);
			n++;

			v = line.vertices[1];
			lineVertexData[n++] = v.getX(seed);
			lineVertexData[n++] = v.getY(seed);
			n++;
		}
		*/

        if (colorDataDirty) {
            float[] colorData = colorBuffer.getData();
            n = 0;
            size = sections.length;
            for (int i = 0; i < size; i++) {
                Section section = sections[i];
                System.arraycopy(section.color.get(), 0, colorData, n, 12);
                n += 12;
            }
            colorDataDirty = false;
        }

        program.start(vpMatrix);

        //fill triangles
        program.bufferVertexPosition(vertexBuffer);
        program.bufferVertexColor(colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, sections.length * 3); //number of vertices

        //draw outline triangles
		/*
		program.bufferVertexPosition(lineVertexBuffer);
		program.staticVertexColor(lineColorBuffer);
		GLES20.glLineWidth(1.0f);
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, lines.length * 2); //number of vertices
		*/

        program.end();
    }

    /**
     * Index must be less than 24 (number of sections) and greater than or equal to 0, otherwise has no effect.<br>
     * The indices of the sections of the hexagon start at the top left triangle proceeding to the right and then moving to the next row.
     */
    public void setColor(int index, GLColor color) {
        if (index >= 24 || index < 0) {
            return;
        }

        Section section = sections[index];
        section.color.set(color);

        colorDataDirty = true;
    }

    private static class Section {
        final Woblex[] vertices = new Woblex[3];
        final GLColor color = new GLColor();

        Section(Woblex v1, Woblex v2, Woblex v3) {
            vertices[0] = v1;
            vertices[1] = v2;
            vertices[2] = v3;
        }
    }

	/*
	private static class Line {
		Woblex[] vertices = new Woblex[2];

		Line(Woblex v1, Woblex v2) {
			vertices[0] = v1;
			vertices[1] = v2;
		}
	}
	*/
}