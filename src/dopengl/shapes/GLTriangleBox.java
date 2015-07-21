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
 * @author Derek Mulvihill - Nov 28, 2013
 */
public class GLTriangleBox {
    private static final int VERTICES_WIDE = 7;
    private static final int VERTICES_TALL = 8;
    public static final int TRIANGLE_COUNT = (VERTICES_TALL - 1) * ((VERTICES_WIDE - 1) * 2);

    private ShapeProgram program;

    private GLFloatBuffer vertexBuffer;
    private GLFloatBuffer colorBuffer;

    private Woblex[] vertices;
    private Section[] sections;

    public GLTriangleBox() {
        program = new ShapeProgram();

        float dy = 2f / (VERTICES_TALL - 1);
        float dx = 2f / (VERTICES_WIDE + 0.5f);
        float wobx = dx / 8f;
        float woby = dy / 8f;

        float x = -1f;
        float y = -1f;

        vertices = new Woblex[VERTICES_WIDE * VERTICES_TALL];
        for (int i = 0; i < VERTICES_TALL; i++) {
            x = ((i % 2 == 0) ? (0f) : (dx / 2f)) - 1f;
            for (int j = 0; j < VERTICES_WIDE; j++) {
                Woblex wob = new Woblex(x, y, DRandom.get().nextInt(1000) + 1000, DRandom.get()
                        .nextInt(1000) + 1000, DRandom.get().nextFloat() * wobx + wobx, DRandom
                        .get().nextFloat() * woby + woby);
                vertices[j + i * VERTICES_WIDE] = wob;
                x += dx;
            }
            y += dy;
        }

        sections = new Section[TRIANGLE_COUNT];
        int vi1 = 0;
        int vi2 = 7;
        boolean vtrack = true;
        for (int i = 0; i < sections.length; i++) {
            if (i % ((VERTICES_WIDE - 1) * 2) == 0 && i != 0) {
                vi1++;
                vi2++;
                vtrack = !vtrack;
            }

            if (vtrack) {
                sections[i] = new Section(vertices[vi1++], vertices[vi1], vertices[vi2]);
            } else {
                sections[i] = new Section(vertices[vi1], vertices[vi2++], vertices[vi2]);
            }
            vtrack = !vtrack;
        }

        for (int i = 0; i < vertices.length; i++) {
            Woblex wob = vertices[i];
            wob.setWobble(DRandom.get().nextInt(2000) + 1000, DRandom.get().nextInt(2000) + 1000,
                    DRandom.get().nextFloat() * 0.05f + 0.05f,
                    DRandom.get().nextFloat() * 0.05f + 0.05f);
        }

        vertexBuffer = OpenGLUtil.createBuffer(sections.length * 3 * 3);
        colorBuffer = OpenGLUtil.createBuffer(sections.length * 3 * 4); //3 vertices per section, 4 color values per vertex
        colorBuffer.setStepSize(4);
    }

    public void draw(float[] vpMatrix, TBoxTri[] triangles) {
        float[] vertexData = vertexBuffer.getData();
        float[] colorData = colorBuffer.getData();
        long seed = DTimer.get().millis();

        for (int i = 0; i < triangles.length && i < sections.length; i++) {
            TBoxTri t = triangles[i];
            if (t.index >= sections.length) {
                continue;
            }
            Section sect = sections[t.index];
            int vn = i * 9;
            for (int j = 0; j < sect.vertices.length; j++) {
                vertexData[vn++] = sect.vertices[j].getX(seed);
                vertexData[vn++] = sect.vertices[j].getY(seed);
                vertexData[vn++] = 0f;
            }
            System.arraycopy(t.color.get(), 0, colorData, i * 12, 12);
        }

        program.start(vpMatrix);

        //fill triangles
        program.bufferVertexPosition(vertexBuffer);
        program.bufferVertexColor(colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, triangles.length * 3); //number of vertices

        program.end();
    }

    private static class Section {
        final Woblex[] vertices = new Woblex[3];

        Section(Woblex v1, Woblex v2, Woblex v3) {
            vertices[0] = v1;
            vertices[1] = v2;
            vertices[2] = v3;
        }
    }

    public static class TBoxTri {
        private int index;
        private GLColor color;

        public TBoxTri(int index, GLColor color) {
            this.index = index;
            this.color = new GLColor();
            this.color.set(color);
        }
    }
}
