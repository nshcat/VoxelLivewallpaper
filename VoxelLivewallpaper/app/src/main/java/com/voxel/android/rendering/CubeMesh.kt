package com.voxel.android.rendering

import org.joml.Vector3f
import org.joml.Vector4f

/**
 * A mesh containing a single cube.
 */
class CubeMesh: TransformedMesh(SimpleMaterial())
{
    /**
     * Populate the vertex list with all voxel face vertices
     */
    init
    {
        enumValues<VoxelFace>().forEach {
            this.vertices.addVertices(it.toVertices(Vector4f(0.7f, 0f, 1f, 1f)))
        }
    }
}