package com.voxel.android.rendering

import org.joml.Vector3f
import org.joml.Vector4f

/**
 * A mesh containing a single cube.
 */
class CubeMesh (var position: Vector3f = Vector3f()): TransformableMesh(SimpleMaterial())
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

    /**
     * Transform mesh by the given translation
     */
    override fun transform(params: RenderParams)
    {
        // Apply translation by vector position
        params.translate(this.position)
    }
}