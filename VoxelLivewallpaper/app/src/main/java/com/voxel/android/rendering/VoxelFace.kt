package com.voxel.android.rendering

import org.joml.Vector3f
import org.joml.Vector4f

/**
 * An enumeration describing the various faces a voxel can have
 *
 * @property vertexData The vertices associated with a given voxel face
 */
enum class VoxelFace(private val vertexData: Collection<VertexData>)
{
    North(listOf(
            VertexData(Vector3f(1f, 1f, 1f), Vector3f(Axis.X)),
            VertexData(Vector3f(1f, 0f, 0f), Vector3f(Axis.X)),
            VertexData(Vector3f(1f, 1f, 0f), Vector3f(Axis.X)),
            VertexData(Vector3f(1f, 0f, 0f), Vector3f(Axis.X)),
            VertexData(Vector3f(1f, 1f, 1f), Vector3f(Axis.X)),
            VertexData(Vector3f(1f, 0f, 1f), Vector3f(Axis.X))
    )),

    West(listOf(
            VertexData(Vector3f(1f, 1f, 0f), -Vector3f(Axis.Z)),
            VertexData(Vector3f(0f, 0f, 0f), -Vector3f(Axis.Z)),
            VertexData(Vector3f(0f, 1f, 0f), -Vector3f(Axis.Z)),
            VertexData(Vector3f(1f, 1f, 0f), -Vector3f(Axis.Z)),
            VertexData(Vector3f(1f, 0f, 0f), -Vector3f(Axis.Z)),
            VertexData(Vector3f(0f, 0f, 0f), -Vector3f(Axis.Z))
    )),

    South(listOf(
            VertexData(Vector3f(0f, 0f, 0f), -Vector3f(Axis.X)),
            VertexData(Vector3f(0f, 0f, 1f), -Vector3f(Axis.X)),
            VertexData(Vector3f(0f, 1f, 1f), -Vector3f(Axis.X)),
            VertexData(Vector3f(0f, 0f, 0f), -Vector3f(Axis.X)),
            VertexData(Vector3f(0f, 1f, 1f), -Vector3f(Axis.X)),
            VertexData(Vector3f(0f, 1f, 0f), -Vector3f(Axis.X))
    )),

    East(listOf(
            VertexData(Vector3f(0f, 1f, 1f), Vector3f(Axis.Z)),
            VertexData(Vector3f(0f, 0f, 1f), Vector3f(Axis.Z)),
            VertexData(Vector3f(1f, 0f, 1f), Vector3f(Axis.Z)),
            VertexData(Vector3f(1f, 1f, 1f), Vector3f(Axis.Z)),
            VertexData(Vector3f(0f, 1f, 1f), Vector3f(Axis.Z)),
            VertexData(Vector3f(1f, 0f, 1f), Vector3f(Axis.Z))
    )),

    Up(listOf(
            VertexData(Vector3f(1f, 1f, 1f), Vector3f(Axis.Y)),
            VertexData(Vector3f(1f, 1f, 0f), Vector3f(Axis.Y)),
            VertexData(Vector3f(0f, 1f, 0f), Vector3f(Axis.Y)),
            VertexData(Vector3f(1f, 1f, 1f), Vector3f(Axis.Y)),
            VertexData(Vector3f(0f, 1f, 0f), Vector3f(Axis.Y)),
            VertexData(Vector3f(0f, 1f, 1f), Vector3f(Axis.Y))
    )),

    Down(listOf(
            VertexData(Vector3f(1f, 0f, 1f), -Vector3f(Axis.Y)),
            VertexData(Vector3f(0f, 0f, 0f), -Vector3f(Axis.Y)),
            VertexData(Vector3f(1f, 0f, 0f), -Vector3f(Axis.Y)),
            VertexData(Vector3f(1f, 0f, 1f), -Vector3f(Axis.Y)),
            VertexData(Vector3f(0f, 0f, 1f), -Vector3f(Axis.Y)),
            VertexData(Vector3f(0f, 0f, 0f), -Vector3f(Axis.Y))
    ));

    /**
     * A class containing both the position as well as the normal of a vertex.
     * This is only used to create the final vertex instance from a voxel face
     * by combining with a color and translation
     *
     * @property position Vertex position
     * @property normal Normal vector
     */
    private class VertexData(val position: Vector3f, val normal: Vector3f)


    companion object
    {
        /**
         * Retrieve the vertices associated with given voxel face. This is most often used
         * to create a voxel mesh by adding the vertices to a triangle list.
         *
         * @param face The voxel face to retrieve vertices for
         * @param translation Translation to apply to the vertices
         * @param color Color of the vertices
         * @return List of vertices for the given face
         */
        fun toVertices(face: VoxelFace, color: Vector4f, translation: Vector3f = Vector3f()): VertexCollection
        {
            // Apply translation to the stored vertex information for the given face
            return face.vertexData
                    .map{ Vertex(it.position + translation, color, it.normal) }
        }
    }
}