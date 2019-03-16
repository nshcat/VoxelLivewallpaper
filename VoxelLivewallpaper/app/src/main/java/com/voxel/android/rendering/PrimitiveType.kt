package com.voxel.android.rendering

import android.opengl.GLES31

/**
 * An enumeration listing all OpenGL primitive types supported by [VertexList].
 *
 * @property nativeValue The native OpenGL value, can be supplied to various render calls
 */
enum class PrimitiveType(val nativeValue: Int)
{
    Triangles(GLES31.GL_TRIANGLES),
    TriangleStrip(GLES31.GL_TRIANGLE_STRIP),
    TriangleFan(GLES31.GL_TRIANGLE_FAN),

    Lines(GLES31.GL_LINES),
    LineStrip(GLES31.GL_LINE_STRIP),
    LineLoop(GLES31.GL_LINE_LOOP);
}