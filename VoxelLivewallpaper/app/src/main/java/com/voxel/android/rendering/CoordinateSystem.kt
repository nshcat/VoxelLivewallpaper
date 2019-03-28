package com.voxel.android.rendering

import android.opengl.GLES30
import com.voxel.android.rendering.materials.SimpleMaterial
import com.voxel.android.rendering.materials.SolidColor
import org.joml.Vector4f
import org.joml.Vector3f

/**
 * A class used to render a simple coordinate system into the scene.
 */
class CoordinateSystem: Mesh(SolidColor())
{
    /**
     * The underlying vertex list used to render the axises. We are cheating a bit here
     * since we are not really drawing the line strip as a line strip, but as individual line
     * segments with start and begin points.
     */
    protected val vertexList: VertexList = VertexList(PrimitiveType.LineStrip)

    /**
     * The line segments used to model the coordinate system.
     */
    protected val lines: LineStrip = LineStrip(Vector4f(1.0f, 1.0f, 1.0f, 1.0f), *allLines)

    /**
     * Initialize the vertex list
     */
    init
    {
        // Store all line segment vertices in the vertex list
        this.vertexList.addVertices(this.lines.toVertices())
    }

    companion object
    {
        /**
         * The various line segments making up the three axises of the coordinate system
         */
        private val allLines = arrayOf(
                Vector3f(0.0f, 0.0f, 0.0f),
                Vector3f(1.5f, 0.0f, 0.0f),

                Vector3f(0.0f, 0.0f, 0.0f),
                Vector3f(0.0f, 1.5f, 0.0f),

                Vector3f(0.0f, 0.0f, 0.0f),
                Vector3f(0.0f, 0.0f, 1.5f)
        )
    }

    /**
     * Render coordinate system to screen using given rendering parameters
     *
     * @param params The rendering parameters to use for rendering this mesh
     */
    override fun render(params: RenderParams)
    {
        // Build mesh if needed
        if(!this.vertexList.isReady)
            this.vertexList.upload()

        // Make sure the material is initialized and activated
        super.render(params)

        GLES30.glLineWidth(3f)

        // Render our lines
        this.vertexList.render()
    }
}