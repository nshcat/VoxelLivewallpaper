package com.voxel.android.rendering

import org.joml.Vector3f

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
     * Render the cube mesh to screen
     */
    override fun render(params: RenderParams)
    {
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