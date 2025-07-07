package com.schmince.gldraw;

import dopengl.shared.GLSimpleWobbleDrawer;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class GLEnemy extends GLSimpleWobbleDrawer {
    private static final int VERTEX_DEGREE = 45;
    private static final int CIRCLE_VERTICES = 360 / VERTEX_DEGREE;
    private static final float[] colors = {1f, 0f, 1f, 1f, 0f, 0f, 0.15f, 1f, 0f, 0f, 0.15f, 1f,};

    public GLEnemy() {
        super(CIRCLE_VERTICES, 1000, 0.1f);

        for (int n = 0; n < CIRCLE_VERTICES; n++) {
            int degree = n * VERTEX_DEGREE;
            float y = (float) Math.sin(degree / 180f * Math.PI);
            float x = (float) Math.cos(degree / 180f * Math.PI);

            float xxx = x * 0.25f;
            float yyy = y * 0.25f;
            buffer.t().v(x, y, -yyy, xxx, yyy, -xxx).c(colors);
        }

        colorBuffer.pushStaticBuffer();
    }
}
