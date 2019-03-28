package com.voxel.android.rendering

import android.opengl.GLES30

/**
 * An enumeration listing all OpenGL primitive types supported by [VertexList].
 *
 * @property nativeValue The native OpenGL value, can be supplied to various render calls
 */
enum class PrimitiveType(val nativeValue: Int)
{
    Triangles(GLES30.GL_TRIANGLES),
    TriangleStrip(GLES30.GL_TRIANGLE_STRIP),
    TriangleFan(GLES30.GL_TRIANGLE_FAN),

    Lines(GLES30.GL_LINES),
    LineStrip(GLES30.GL_LINE_STRIP),
    LineLoop(GLES30.GL_LINE_LOOP);
}