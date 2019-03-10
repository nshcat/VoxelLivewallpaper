package com.voxel.android.rendering

import org.joml.Vector3f
import org.joml.Vector4f
import java.nio.FloatBuffer

/**
 * A single vertex, containing position, color and normal vector
 */
class Vertex (val position: Vector3f, val color: Vector4f, val normal: Vector3f)
{
    companion object
    {
        /**
         * The total size of a vertex, in floats
         */
        const val SIZE = 10

        /**
         * Offset of position vector, in floats
         */
        const val OFFSET_POSITION = 0

        /**
         * Offset of color vector, in floats
         */
        const val OFFSET_COLOR = 3

        /**
         * Offset of normal vector, in floats
         */
        const val OFFSET_NORMAL = 7
    }

    /**
     * Transfer vertex data into given float buffer for upload to GPU.
     *
     * @param buffer Target float buffer
     */
    fun get(buffer: FloatBuffer)
    {
        this.position.get(buffer)
        this.color.get(buffer)
        this.normal.get(buffer)
    }
}

/**
 * A collection of vertices, used in conjunction with a triangle list
 */
typealias VertexCollection = Collection<Vertex>