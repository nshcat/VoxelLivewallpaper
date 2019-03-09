package com.voxel.android.rendering

import android.opengl.GLES31
import org.joml.Vector3f
import org.joml.Vector4f
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * A class implementing a list of triangles on the GPU using VAO and VBO.
 * Note that this class is not useful to render something on its own, since
 * it doesnt manage a shader program that is needed for a successful render.
 * Use any of the mesh classes for this purpose.
 */
class TriangleList
{
    /**
     * A single vertex, containing position, color and normal vector
     */
    class Vertex (val position: Vector3f, val color: Vector4f, val normal: Vector3f)
    {
        companion object
        {
            /**
             * The total size of a vertex, in floats
             */
            const val SIZE = 10

            /**
             * Offset of position vector, in floats
             */
            const val OFFSET_POSITION = 0

            /**
             * Offset of color vector, in floats
             */
            const val OFFSET_COLOR = 3

            /**
             * Offset of normal vector, in floats
             */
            const val OFFSET_NORMAL = 7
        }

        /**
         * Transfer vertex data into given float buffer for upload to GPU.
         *
         * @param buffer Target float buffer
         */
        fun get(buffer: FloatBuffer)
        {
            this.position.get(buffer)
            this.color.get(buffer)
            this.normal.get(buffer)
        }
    }

    /**
     * Reserve space for given number of triangles to avoid large number
     * of reallocations
     *
     * @param size Number of triangles to reserve space for
     */
    fun reserve(size: Int)
    {
        // A triangle is made up of three vertices
        (this.vertices as ArrayList<Vertex>).ensureCapacity(size * 3)
    }

    /**
     * Add given vertex to the triangle list.
     *
     * @param vtx The vertex to add
     */
    fun addVertex(vtx: Vertex)
    {
        // If the list is already ready, no more vertices can be added
        if(this.isReady)
        {
            throw IllegalStateException("Can't add vertex to TriangleList that was already built")
        }

        this.vertices.add(vtx)
    }

    /**
     * Upload triangle to GPU. After this operation, no more vertices may be added
     * to the list. Note than once the list has been uploaded to the GPU, the operation
     * can never be repeated for a given instance.
     */
    fun upload()
    {
        // Cannot upload triangle list to GPU more than once
        if(this.isReady)
        {
            throw IllegalStateException("Cannot build/upload triangle list that has already been built/uploaded")
        }

        // This only works if the number of vertices is a multiple of three
        if(this.vertices.size % 3 != 0)
        {
            throw java.lang.IllegalStateException("Can't upload triangle list if number of vertices is not a multiple of three")
        }

        // Create the VAO
        val vaoBuffer = IntArray(1)
        GLES31.glGenVertexArrays(1, vaoBuffer, 0)
        this.vaoHandle = vaoBuffer[0]

        if(this.vaoHandle == GLES31.GL_NONE)
        {
            throw IllegalStateException("Could not create VAO")
        }

        GLES31.glBindVertexArray(this.vaoHandle)
        // ===

        // Create the VBO
        val vboBuffer = IntArray(1)
        GLES31.glGenBuffers(1, vboBuffer, 1)
        this.vboHandle = vboBuffer[0]

        if(this.vboHandle == GLES31.GL_NONE)
        {
            throw IllegalStateException("Could not create VBO")
        }

        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, this.vboHandle)
        // ===

        // Size of whole vertex data in floats
        val sizeInFloats = Vertex.SIZE * this.vertices.size

        // Size of whole vertex data in bytes
        val sizeInBytes = sizeInFloats * 4

        // Create a float buffer for all vertex data
        val dataBuffer = ByteBuffer.allocateDirect(sizeInFloats).asFloatBuffer()

        // Save all vertex data to buffer
        this.vertices.forEach({ x -> x.get(dataBuffer)})

        // Fill buffer with data
        GLES31.glBufferData(
                GLES31.GL_ARRAY_BUFFER,
                sizeInBytes,
                dataBuffer,
                GLES31.GL_STATIC_DRAW
        )

        // Set up vertex attribute pointers
        // Position
        GLES31.glVertexAttribBinding(0, 0)
        GLES31.glEnableVertexAttribArray(0)
        GLES31.glVertexAttribFormat(0, 3, GLES31.GL_FLOAT, false, Vertex.OFFSET_POSITION * 4)

        // Color
        GLES31.glVertexAttribBinding(1, 0)
        GLES31.glEnableVertexAttribArray(1)
        GLES31.glVertexAttribFormat(1, 4, GLES31.GL_FLOAT, false, Vertex.OFFSET_COLOR * 4)

        // Normal vector
        GLES31.glVertexAttribBinding(2, 0)
        GLES31.glEnableVertexAttribArray(2)
        GLES31.glVertexAttribFormat(2, 3, GLES31.GL_FLOAT, false, Vertex.OFFSET_NORMAL * 4)

        GLES31.glBindVertexBuffer(0, this.vboHandle, 0, Vertex.SIZE * 4)

        // Upload finished. Mark as ready
        this.isReady = true
    }

    /**
     * Render triangle list to screen. This assumes that a shader program is already active
     * and has all needed matrices set.
     */
    fun render()
    {
        // We cant render a triangle list that hasnt been uploaded yet
        if(!this.isReady)
            throw IllegalStateException("Tried to render triangle list that wasn't uploaded yet")

        // Bind buffers for rendering
        GLES31.glBindVertexArray(this.vaoHandle)
        GLES31.glEnableVertexAttribArray(0)
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, this.vboHandle)

        // Draw all vertices
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, this.vertices.size)
    }

    /**
     * All vertices in this triangle list
     */
    protected var vertices: MutableList<Vertex> = ArrayList()

    /**
     * OpenGL handle to the VAO
     */
    protected var vaoHandle = GLES31.GL_NONE

    /**
     * OpenGL handle to the VBO
     */
    protected var vboHandle = GLES31.GL_NONE

    /**
     * A flag indicating whether this triangle list is ready for rendering, aka
     * was already built. If that is the case, no new vertices can be added to it
     * anymore.
     */
    var isReady = false
}