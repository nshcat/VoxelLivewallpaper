package com.voxel.android.rendering

/**
 * An abstract base class for all types of meshes that can be rendered to screen
 * using a material and a set of render parameters.
 *
 * @property material The material to use when rendering instances of this class
 */
abstract class Mesh (val material: Material): Renderable
{
    /**
     * Activate the material and apply render parameters
     */
    override fun render(params: RenderParams)
    {
        this.material.use()
        this.material.applyParameters(params)
    }
}
