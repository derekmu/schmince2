package com.schmince.gldraw;

import dopengl.shared.GLSimpleBufferDrawer;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class GLPick extends GLSimpleBufferDrawer {
    public GLPick() {
        super(4);
        buffer.tvcb(-1.000, 0.463, -0.077, 0.994, -0.077, 0.758, 0.473f, 0.501f, 0.489f)
                .tvcb(-0.080, 1.000, -0.086, 0.755, 1.000, 0.410, 0.473f, 0.501f, 0.489f)
                .tvcb(-0.092, 0.772, -0.193, -0.963, -0.099, -1.000, 0.510f, 0.236f, 0.000f)
                .tvcb(-0.091, -0.937, -0.069, 0.776, -0.133, 0.784, 0.510f, 0.236f, 0.000f);
        vertexBuffer.pushStaticBuffer();
        colorBuffer.pushStaticBuffer();
    }
}
