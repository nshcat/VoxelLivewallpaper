package com.voxel.android.rendering

import android.opengl.GLES31
import android.util.Log
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import java.nio.FloatBuffer

/**
 * Upload 4x4 float matrix as uniform value
 *
 * @param program ShaderProgram the uniform is contained in
 * @param name Uniform name
 * @param mat Matrix to upload
 */
fun uniformMat4f(program: ShaderProgram, name: String, mat: Matrix4f)
{
    // Try to find the uniform location
    val location = checkedUniformLocation(program, name)

    // Allocate a buffer of appropiate size
    val buffer = allocateFloatBuffer(16)

    // Save matrix contents
    mat.get(buffer)

    // Upload contents
    GLES31.glUniformMatrix4fv(location, 1, false, buffer)
}

/**
 * Upload 3d float vector as uniform value
 *
 * @param program ShaderProgram the uniform is contained in
 * @param name Uniform name
 * @param vec Vector
 */
fun uniformVec3f(program: ShaderProgram, name: String, vec: Vector3f)
{
    // Try to find the uniform location
    val location = checkedUniformLocation(program, name)

    // Allocate a buffer of appropiate size
    val buffer = allocateFloatBuffer(3)

    // Save matrix contents
    vec.get(buffer)

    // Upload contents
    GLES31.glUniform3fv(location, 1, buffer)
}

/**
 * Upload 4d float vector as uniform value
 *
 * @param program ShaderProgram the uniform is contained in
 * @param name Uniform name
 * @param vec Vector
 */
fun uniformVec4f(program: ShaderProgram, name: String, vec: Vector4f)
{
    // Try to find the uniform location
    val location = checkedUniformLocation(program, name)

    // Allocate a buffer of appropiate size
    val buffer = allocateFloatBuffer(4)

    // Save matrix contents
    vec.get(buffer)

    // Upload contents
    GLES31.glUniform4fv(location, 1, buffer)
}

/**
 * Allocate a float buffer for use with uniform upload
 *
 * @param size Size of the buffer in number of floats
 * @return Allocated FloatBuffer of requested size
 */
private fun allocateFloatBuffer(size: Int): FloatBuffer
{
    return FloatBuffer.allocate(size)
}

/**
 * Tries to receive the uniform location for given named uniform in given program.
 * This will emit a debug message and throw an exception if the location wasnt found.
 *
 * @param program Shader program to search uniform in
 * @param name Name of the uniform
 * @return OpenGL uniform location value
 */
private fun checkedUniformLocation(program: ShaderProgram, name: String): Int
{
    // Retrieve uniform location
    val location = GLES31.glGetUniformLocation(program.handle, name)

    // Check if location is known
    if(location == -1)
    {
        Log.e("setUniform", "Unknown uniform location: \"$name\"")
        throw IllegalArgumentException("unknown uniform location")
    }
    else return location;
}