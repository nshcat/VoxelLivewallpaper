package com.voxel.android.rendering

import android.opengl.GLES31
import android.util.Log
import org.joml.Vector3f
import org.joml.Vector4f
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * A class implementing a list of vertices on the GPU using VAO and VBO.
 * The user can decide how the vertices are rendered by selecting a primitive type.
 * Note that this class is not useful to render something on its own, since
 * it doesnt manage a shader program that is needed for a successful render.
 * Use any of the mesh classes for this purpose.
 */
class VertexList(val primitiveType: PrimitiveType = PrimitiveType.Triangles)
{
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
            throw IllegalStateException("Can't add vertex to VertexList that was already built")
        }

        this.vertices.add(vtx)
    }

    /**
     * Add given vertices to the triangle list.
     *
     * @param vtxs The vertices to add
     */
    fun addVertices(vtxs: VertexCollection)
    {
        // If the list is already ready, no more vertices can be added
        if(this.isReady)
        {
            throw IllegalStateException("Can't add vertices to VertexList that was already built")
        }

        this.vertices.addAll(vtxs)
    }

    /**
     * Set up the VBO containing vertex position information.
     */
    protected fun createPositionBuffer()
    {
        // Create the VBO
        this.vboPositionHandle = this.createBuffer()

        // Collect vertex position data. Every position value is a 3D vector, with each
        // entry being a four byte float.
        val byteBuffer = ByteBuffer.allocateDirect(this.vertices.size * 3 * SIZE_FLOAT)

        // Use the devices native byte order
        byteBuffer.order(ByteOrder.nativeOrder())

        // Create float buffer based on the byte buffer.
        val floatBuffer = byteBuffer.asFloatBuffer()

        // Store vertex position in float buffer
        this.vertices.forEach {
            floatBuffer.put(it.position.x)
            floatBuffer.put(it.position.y)
            floatBuffer.put(it.position.z)
        }

        // Reset buffer position to the beginning
        floatBuffer.position(0)

        // Bind buffer and upload position data
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, this.vboPositionHandle)
        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, this.vertices.size * 3 * SIZE_FLOAT, byteBuffer, GLES31.GL_STATIC_DRAW)

        // Setup VAO for this attribute
        GLES31.glEnableVertexAttribArray(0)
        GLES31.glVertexAttribPointer(0, 3, GLES31.GL_FLOAT, false, 0, 0)
    }

    /**
     * Set up the VBO containing vertex color information.
     */
    protected fun createColorBuffer()
    {
        // Create the VBO
        this.vboColorHandle = this.createBuffer()

        // Collect vertex position data. Every position value is a 3D vector, with each
        // entry being a four byte float.
        val byteBuffer = ByteBuffer.allocateDirect(this.vertices.size * 4 * SIZE_FLOAT)

        // Use the devices native byte order
        byteBuffer.order(ByteOrder.nativeOrder())

        // Create float buffer based on the byte buffer.
        val floatBuffer = byteBuffer.asFloatBuffer()

        // Store vertex position in float buffer
        this.vertices.forEach {
            floatBuffer.put(it.color.x)
            floatBuffer.put(it.color.y)
            floatBuffer.put(it.color.z)
            floatBuffer.put(it.color.w)
        }

        // Reset buffer position to the beginning
        floatBuffer.position(0)

        // Bind buffer and upload position data
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, this.vboColorHandle)
        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, this.vertices.size * 4 * SIZE_FLOAT, byteBuffer, GLES31.GL_STATIC_DRAW)

        // Setup VAO for this attribute
        GLES31.glEnableVertexAttribArray(1)
        GLES31.glVertexAttribPointer(1, 4, GLES31.GL_FLOAT, false, 0, 0)
    }

    /**
     * Set up the VBO containing vertex normal information.
     */
    protected fun createNormalBuffer()
    {
        // Create the VBO
        this.vboNormalHandle = this.createBuffer()

        // Collect vertex position data. Every position value is a 3D vector, with each
        // entry being a four byte float.
        val byteBuffer = ByteBuffer.allocateDirect(this.vertices.size * 3 * SIZE_FLOAT)

        // Use the devices native byte order
        byteBuffer.order(ByteOrder.nativeOrder())

        // Create float buffer based on the byte buffer.
        val floatBuffer = byteBuffer.asFloatBuffer()

        // Store vertex position in float buffer
        this.vertices.forEach {
            floatBuffer.put(it.normal.x)
            floatBuffer.put(it.normal.y)
            floatBuffer.put(it.normal.z)
        }

        // Reset buffer position to the beginning
        floatBuffer.position(0)

        // Bind buffer and upload position data
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, this.vboNormalHandle)
        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, this.vertices.size * 3 * SIZE_FLOAT, byteBuffer, GLES31.GL_STATIC_DRAW)

        // Setup VAO for this attribute
        GLES31.glEnableVertexAttribArray(2)
        GLES31.glVertexAttribPointer(2, 3, GLES31.GL_FLOAT, false, 0, 0)
    }

    /**
     * Create an OpenGL VBO
     *
     * @return The OpenGL handle of the created VBO
     */
    protected fun createBuffer(): Int
    {
        val vboBuffer = IntArray(1)
        GLES31.glGenBuffers(1, vboBuffer, 0)

        val vboHandle = vboBuffer[0]

        if(vboHandle == GLES31.GL_NONE)
        {
            throw IllegalStateException("Could not create VBO")
        }
        else return vboHandle
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

        // Create the VBOs and attribute bindings
        this.createPositionBuffer()
        this.createColorBuffer()
        this.createNormalBuffer()

        // Deactivate VAO for now
        GLES31.glBindVertexArray(0)

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

        // Activate the VAO. It includes all information about how attributes should be bound
        // to the buffers.
        GLES31.glBindVertexArray(this.vaoHandle)

        // Draw all triangles in this list.
        GLES31.glDrawArrays(this.primitiveType.nativeValue, 0, this.vertices.size)

        // Deactivate VAO to avoid interference with other rendering jobs afterwards
        GLES31.glBindVertexArray(GLES31.GL_NONE)
    }

    /**
     * The size of a float value, in bytes
     */
    private val SIZE_FLOAT = 4

    /**
     * All vertices in this triangle list
     */
    protected var vertices: MutableList<Vertex> = ArrayList()

    /**
     * OpenGL handle to the VAO
     */
    protected var vaoHandle = GLES31.GL_NONE

    /**
     * OpenGL handle to the color VBO
     */
    protected var vboColorHandle = GLES31.GL_NONE

    /**
     * OpenGL handle to the normal VBO
     */
    protected var vboNormalHandle = GLES31.GL_NONE

    /**
     * OpenGL handle to the position VBO
     */
    protected var vboPositionHandle = GLES31.GL_NONE

    /**
     * A flag indicating whether this triangle list is ready for rendering, aka
     * was already built. If that is the case, no new vertices can be added to it
     * anymore.
     */
    var isReady = false
}