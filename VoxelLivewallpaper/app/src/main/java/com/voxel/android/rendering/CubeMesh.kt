package com.voxel.android.rendering

import com.voxel.android.rendering.materials.SimpleMaterial
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
            this.vertices.addVertices(it.toVertices(Vector4f(0.09f, 0.59f, 0.13f, 1f)))
        }
    }
}