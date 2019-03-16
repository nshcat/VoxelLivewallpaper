package com.voxel.android.rendering

import org.joml.Vector3f
import org.joml.Vector4f

/**
 * A mesh containing a single cube.
 */
class CubeMesh (var position: Vector3f = Vector3f()): Mesh(SimpleMaterial())
{
    /**
     * The triangle list used to hold all vertices of the cube mesh
     */
    protected val vertices = TriangleList()

    /**
     * Populate the vertex list with all voxel face vertices
     */
    init
    {
        enumValues<VoxelFace>().forEach {
            this.vertices.addVertices(it.toVertices(Vector4f(0.7f, 0f, 1f, 1f), Vector3f(-0.5f, -0.5f,-0.5f)))
        }
    }

    /**
     * Render the cube mesh to screen
     */
    override fun render(params: RenderParams)
    {
        // Build mesh if needed
        if(!this.vertices.isReady)
            this.vertices.upload()

        // Save current model matrix
        params.pushMatrix()

        // Apply translation
        params.translate(this.position)

        // Activate and set up material
        super.render(params)

        // Render all triangles in the underlying triangle list
        vertices.render()

        // Restore previous matrix state
        params.popMatrix()
    }
}