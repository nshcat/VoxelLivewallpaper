package com.voxel.android.data

/**
 * The dimensions of a single voxel model frame. This most commonly describes the smallest
 * possible bounding box a model fits in.
 *
 * @property width Width of the model
 * @property height Height of the model
 * @property depth Depth of the model
 */
data class VoxelModelDimensions(val width: Int, val height: Int, val depth: Int)