package com.voxel.android.rendering

import org.joml.Vector3f

/**
 * All axises in a 3D coordinate system
 */
enum class Axis
{
    X,
    Y,
    Z
}

/**
 * Create a unit vector of any of the three axises
 */
fun Vector3f(axis: Axis): Vector3f =
    when(axis)
    {
        Axis.X -> Vector3f(1.0f, 0.0f, 0.0f)
        Axis.Y -> Vector3f(0.0f, 1.0f, 0.0f)
        Axis.Z -> Vector3f(0.0f, 0.0f, 1.0f)
    }