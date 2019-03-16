package com.voxel.android.rendering

import org.joml.Vector3f
import org.joml.Vector4f

/**
 * A strip of lines. Connects two adjacent vertices with a line. This results in n-1 line
 * segments for n vertices.
 *
 * @property color The color of the line strip. Same for all segments in it.
 */
class LineStrip(val color: Vector4f, vararg points: Vector3f): VertexStructure
{
    /**
     * All points in this line strip
     */
    val points: MutableList<Vector3f> = ArrayList()

    /**
     * Initialize line strip
     */
    init
    {
        // At least two points are required. Otherwise the concept of a line strip
        // makes no sense
        if(points.size <= 1)
            throw IllegalArgumentException("LineStrip needs at least 2 points")

        // Store all points
        this.points.addAll(points)
    }

    /**
     * Retrieve vertices for this line strip.
     */
    override fun toVertices(): VertexCollection
    {
        // We dont need a normal vector for lines.
        return this.points.map { Vertex(it, color, Vector3f()) }
    }
}