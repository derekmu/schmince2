package com.schmince.gldraw;

import dopengl.shared.GLSimpleBufferDrawer;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class GLArmor extends GLSimpleBufferDrawer {
    public GLArmor() {
        super(6);

        buffer.tvcb(-0.630, -0.997, 0.483, -0.997, 0.385, 0.680, 0.473f, 0.501f, 0.489f)
                .tvcb(-0.646, -1.000, -0.499, 0.765, 0.413, 0.681, 0.473f, 0.501f, 0.489f)
                .tvcb(0.667, -0.010, 1.000, 0.452, -0.096, 0.730, 0.473f, 0.501f, 0.489f)
                .tvcb(-0.336, 0.690, -0.337, 1.000, 0.166, 0.957, 0.473f, 0.501f, 0.489f)
                .tvcb(0.139, 0.953, 0.102, 0.575, -0.348, 0.666, 0.473f, 0.501f, 0.489f)
                .tvcb(0.026, 0.873, -1.000, 0.521, -0.780, 0.024, 0.473f, 0.501f, 0.489f);

        vertexBuffer.pushStaticBuffer();
        colorBuffer.pushStaticBuffer();
    }
}
