package com.voxel.android.rendering

/**
 * An abstract base class for all types of meshes that can be rendered to screen
 * using a shader program and a set of render parameters.
 *
 * @property program The shader program to use
 */
abstract class Mesh (val program: ShaderProgram)
{
    /**
     * Render this mesh to screen using given rendering parameters
     *
     * @param params Rendering parameters to use
     */
    abstract fun render(params: RenderParams)
}