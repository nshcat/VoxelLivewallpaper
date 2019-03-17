package com.voxel.android.rendering

/**
 * An abstract base class for meshes that can be transformed by affine transformations.
 * The abstract function [transform] can be overridden to implement arbitrary mesh transformations.
 * The management of the matrix stack is done automatically, [RenderParams.pushMatrix] and
 * [RenderParams.popMatrix] are not needed here.
 */
abstract class TransformableMesh(material: Material, primitiveType: PrimitiveType = PrimitiveType.Triangles):
        Mesh(material, primitiveType)
{
    /**
     * Transform the mesh by modifying the model matrix stored in [params]. Matrix scope
     * is automatically handled.
     */
    abstract fun transform(params: RenderParams)

    /**
     * Perform render of the mesh. The method [transform] is called after establishing a new matrix
     * scope in order to allow arbitrary transformations.
     *
     * @param params Rendering parameters to use while rendering.
     */
    override fun render(params: RenderParams)
    {
        // Create new matrix scope
        params.pushMatrix()

        // Apply transformations
        this.transform(params)

        // Actually render the mesh
        super.render(params)

        // Reset model matrix
        params.popMatrix()
    }
}