package com.voxel.android.rendering

import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f
import java.util.*

/**
 * A class implementing a matrix stack used in rendering with hierarchical transformations
 *
 * @property view The view Matrix
 * @property projection The projection Matrix
 */
class RenderParams (val view: Matrix4f,  val projection: Matrix4f)
{
    /**
     * The current model matrix.
     */
    var model: Matrix4f = Matrix4f()

    /**
     * The matrix stack used to implement hierarchical transformations
     */
    protected var stack: Deque<Matrix4f> = ArrayDeque()

    /**
     * Replace current model matrix with the topmost matrix on the stack and
     * popping that matrix.
     */
    fun popMatrix()
    {
        if(this.stack.isEmpty())
            throw IllegalStateException("Matrix stack is empty")
        else
        {
            // Retrieve topmost matrix from stack
            val matrix = this.stack.pop()

            // Replace model matrix with it
            this.model = matrix;
        }
    }

    /**
     * Push the current model matrix onto the matrix stack for later
     * retrieval.
     */
    fun pushMatrix()
    {
        // Save current model matrix on matrix stack
        this.stack.push(this.model)
    }

    /**
     * Add a translation to the current model matrix
     *
     * @param vec Vector to translate by
     */
    fun translate(vec: Vector3f)
    {
        this.model *= Matrix4f().translation(vec)
    }

    /**
     * Rotate around the Z-axis by given angle
     *
     * @param angle Angle to rotate by, in radians
     */
    fun rotateZ(angle: Float)
    {
        this.model *= Matrix4f().rotateZ(angle)
    }

    /**
     * Rotate around the Y-axis by given angle
     *
     * @param angle Angle to rotate by, in radians
     */
    fun rotateY(angle: Float)
    {
        this.model *= Matrix4f().rotateY(angle)
    }

    /**
     * Apply uniform scaling to model
     *
     * @param factor Scaling factor
     */
    fun scale(factor: Float)
    {
        this.model *= Matrix4f().scale(factor)
    }
}