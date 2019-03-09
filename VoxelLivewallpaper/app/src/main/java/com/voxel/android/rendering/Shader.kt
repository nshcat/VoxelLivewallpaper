package com.voxel.android.rendering

import android.opengl.GLES31
import android.util.Log
import java.lang.IllegalStateException

/**
 * A class abstracting the concept of an OpenGL shader object.
 *
 * @property type The type of this shader object, e.g. fragment shader
 */
class Shader (val type: ShaderType)
{
    /**
     * The native OpenGL handle of this shader.
     */
    var handle: Int = GLES31.GL_NONE
        protected set

    /**
     * Shader object initialisation
     */
    init
    {
        // Create new OpenGL shader object
        this.handle = GLES31.glCreateShader(this.type.nativeValue)

        // Check for error
        if(this.handle == GLES31.GL_NONE)
        {
            // We cant recover from this
            Log.e("Shader", "Could not create shader object");
            throw IllegalStateException()
        }
    }

    /**
     * "Static" methods that allow construction from source code string
     */
    companion object
    {
        /**
         * Create a new OpenGL shader object from given shader source code.
         *
         * @param type Shader type
         * @param source Shader source code as string
         * @return Compiled shader object
         */
        fun FromText(type: ShaderType, source: String): Shader
        {
            // Create new shader
            var shader = Shader(type)

            // Attach shader source
            shader.addSource(source)

            // Compile it
            shader.compile()

            // Shader is now fully compiled and ready for use
            return shader
        }
    }

    /**
     * Attach given OpenGL shader source code to this shader object
     *
     * @param source Shader source code
     */
    protected fun addSource(source: String)
    {
        // Empty source is not allowed
        if(source.isEmpty())
            throw IllegalArgumentException("Shader source cannot be empty")

        // Attach it to the shader object
        GLES31.glShaderSource(this.handle, source)
    }

    /**
     * Compile the shader. This is only possible if source code was added first.
     */
    protected fun compile()
    {
        // Try to compile the shader
        GLES31.glCompileShader(this.handle)

        // Check the compilation status to determine if compilation was successful
        val status = IntArray(1)
        GLES31.glGetShaderiv(this.handle, GLES31.GL_COMPILE_STATUS, status, 0)

        if(status[0] == GLES31.GL_NONE)
        {
            // Something went wrong
            Log.e("Shader", "Failed to compiler shader: " + GLES31.glGetShaderInfoLog(this.handle))
            GLES31.glDeleteShader(this.handle)
            throw IllegalStateException("Shader compilation failed")
        }
    }
}