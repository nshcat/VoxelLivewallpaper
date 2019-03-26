package com.voxel.android.data

import java.io.InputStream

/**
 * An interface describing a very low-level model loader class. Model loaders do not care about
 * things like file paths or extensions, but work directly on raw binary streams to deserialize
 * the voxel model.
 */
interface VoxelModelLoader
{
    /**
     * Try to load voxel model and all its frame from given input stream.
     *
     * @param stream The input stream to read from
     * @return Loaded voxel model
     */
    fun load(stream: InputStream): VoxelModel
}