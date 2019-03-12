package com.voxel.android.rendering

/**
 * Interface for camera navigations, which are classes that control how the camera
 * acts based on user input and the passing of time
 */
interface Navigation
{
    /**
     * Update the navigation with [delta] passed seconds
     */
    fun update(delta: Double)

    /**
     * Perform first time setup for given Camera instance [c]
     */
    fun initialize(c: Camera)
}