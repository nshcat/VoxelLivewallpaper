package com.voxel.android.rendering

/**
 * An abstract base class for all meshes that are made up from a set of vertices and a primitive
 * type and that use a material for rendering.
 *
 * @property material The material to use when rendering instances of this class
 */
abstract class Mesh (material: Material, primitiveType: PrimitiveType = PrimitiveType.Triangles): Shadeable(material)
{
    /**
     * The vertices that make up this mesh
     */
    protected val vertices: VertexList = VertexList(primitiveType)

    /**
     * Activate the material and apply render parameters
     */
    override fun render(params: RenderParams)
    {
        // Make sure the material is activated
        super.render(params)

        // Is the vertex list actually ready to be rendered?
        if(!this.vertices.isReady)
            this.vertices.upload()

        // Render the vertices of this mesh
        this.vertices.render()
    }
}
