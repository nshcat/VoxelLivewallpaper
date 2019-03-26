package com.voxel.android.data

import com.voxel.android.rendering.Color

/**
 * A single frame of a voxel model. Model frames can be used for animation or to store
 * alternative models in one logical model group.
 *
 * @property dimensions The dimensions of this model frame
 */
class VoxelModelFrame(val dimensions: VoxelModelDimensions)
{
    /**
     * The actual voxel data. A value of 0 indicates a missing voxel.
     */
    private val voxelData: UByteArray

    /**
     * Initialize empty voxel frame
     */
    init
    {
        // Create the byte array
        this.voxelData = UByteArray(dimensions.depth * dimensions.width * dimensions.height)
    }

    /**
     * Determine whether the voxel frame has a voxel present at given position.
     * Note that the coordinates follow the OpenGL coordinate system, y being the UP axis.
     *
     * @param x The position in X direction
     * @param y The position in Y direction (up/down)
     * @param z The position in Z direction (depth)
     * @return A flag indicating whether a voxel is present at given position
     */
    fun hasVoxelAt(x: Int, y: Int, z: Int): Boolean
    {
        return this.voxelData[this.toIndex(x, y, z)] != 0u.toUByte()
    }

    /**
     * Retrieve the color index at given voxel position. The index is meant as an index into
     * the currently active palette instance.
     *
     * @param x The position in X direction
     * @param y The position in Y direction (up/down)
     * @param z The position in Z direction (depth)
     * @return The palette index of the voxel color the voxel at given position has
     */
    fun colorIndexAt(x: Int, y: Int, z: Int): UByte
    {
        return this.voxelData[this.toIndex(x, y, z)]
    }

    /**
     * Set color index for given voxel position.
     *
     * @param x The position in X direction
     * @param y The position in Y direction (up/down)
     * @param z The position in Z direction (depth)
     * @param index The new color index to store
     */
    fun setColorIndexAt(x: Int, y: Int, z: Int, index: UByte)
    {
        this.voxelData[this.toIndex(x, y, z)] = index
    }

    /**
     * Convert given 3D voxel position to an index into the sequential byte array.
     *
     * @param x The position in X direction
     * @param y The position in Y direction (up/down)
     * @param z The position in Z direction (depth)
     * @return The index corresponding to the given position
     */
    private fun toIndex(x: Int, y: Int, z: Int): Int
    {
        return x + (y * this.dimensions.width) + (z * this.dimensions.width * this.dimensions.height)
    }
}