package com.voxel.android.rendering

import org.joml.Matrix4f
import org.joml.Vector3f

/**
 * Matrix multiplication for 4x4 matrices
 */
operator fun Matrix4f.times(other: Matrix4f): Matrix4f
{
    val result = Matrix4f()

    this.mul(other, result)

    return result
}

/**
 * Swap vector direction
 */
operator fun Vector3f.unaryMinus(): Vector3f
{
    val result = Vector3f()

    this.mul(-1.0f, result)

    return result
}

/**
 * Vector subtraction
 */
operator fun Vector3f.minus(other: Vector3f): Vector3f
{
    val result = Vector3f()

    this.sub(other, result)

    return result
}

/**
 * Vector addition
 */
operator fun Vector3f.plus(other: Vector3f): Vector3f
{
    val result = Vector3f()

    this.add(other, result)

    return result
}

/**
 * Vector scalar multiplication
 */
operator fun Vector3f.times(other: Float): Vector3f
{
    val result = Vector3f()

    this.mul(other, result)

    return result
}