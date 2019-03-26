package com.voxel.android.data

/**
 * A class managing a set of [VoxelModelFrame].
 */
class VoxelModel
{
    /**
     * The frames contained in this model. This should contain at least one.
     */
    val frames: MutableList<VoxelModelFrame> = ArrayList()

    /**
     * The color palette used by this voxel model group
     */
    val palette: Palette = Palette()
}