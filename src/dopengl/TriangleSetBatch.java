package dopengl;

import dopengl.shapes.GLTriangleSet;
import thed.GLColor;

/**
 * @author Derek Mulvihill - Feb 1, 2014
 */
public class TriangleSetBatch {
    private float[] trianglesetbatchvertices = new float[9 * GLTriangleSet.MAX_TRIANGLES];
    private float[] trianglesetbatcholors = new float[12 * GLTriangleSet.MAX_TRIANGLES];
    private int trianglesetbatchcount = 0;
    private DRenderer renderer;

    private float[] rectVertices = new float[9];

    public TriangleSetBatch(DRenderer renderer) {
        this.renderer = renderer;
    }

    public void batchTriangle(float[] vertices, GLColor color) {
        System.arraycopy(vertices, 0, trianglesetbatchvertices, trianglesetbatchcount * 9, 9);
        System.arraycopy(color.get(), 0, trianglesetbatcholors, trianglesetbatchcount * 12, 12);
        trianglesetbatchcount++;
        if (trianglesetbatchcount >= GLTriangleSet.MAX_TRIANGLES) {
            finishTriangleBatch();
        }
    }

    public void batchRect(float x, float y, float w, float h, GLColor color) {
        //bottom left
        rectVertices[0] = x;
        rectVertices[1] = y + h;
        rectVertices[2] = 0f;

        rectVertices[3] = x;
        rectVertices[4] = y;
        rectVertices[5] = 0f;

        rectVertices[6] = x + w;
        rectVertices[7] = y;
        rectVertices[8] = 0f;
        batchTriangle(rectVertices, color);

        //top right
        rectVertices[0] = x;
        rectVertices[1] = y + h;
        rectVertices[2] = 0f;

        rectVertices[3] = x + w;
        rectVertices[4] = y;
        rectVertices[5] = 0f;

        rectVertices[6] = x + w;
        rectVertices[7] = y + h;
        rectVertices[8] = 0f;
        batchTriangle(rectVertices, color);
    }

    public void finishTriangleBatch() {
        if (trianglesetbatchcount > 0) {
            GLLibrary glib = renderer.getGlib();
            glib.getTriangleSet().setVertices(trianglesetbatchvertices);
            glib.getTriangleSet().setColors(trianglesetbatcholors);
            glib.getTriangleSet().draw(renderer.getVPMatrix(), trianglesetbatchcount);
            trianglesetbatchcount = 0;
        }
    }
}
