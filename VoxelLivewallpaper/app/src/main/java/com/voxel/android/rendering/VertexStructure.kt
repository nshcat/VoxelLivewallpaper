package com.voxel.android.rendering

/**
 * An interface for all types that are made up out of vertices, and thus can be converted
 * to a vertex collection and rendered using [VertexList]
 */
interface VertexStructure
{
    /**
     * Retrieve vertices that make up the vertex structure
     */
    fun toVertices(): VertexCollection
}