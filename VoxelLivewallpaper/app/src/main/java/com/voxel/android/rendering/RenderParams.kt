package com.voxel.android.rendering

import org.joml.Matrix4f
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
}