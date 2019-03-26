package com.voxel.android.data

import com.voxel.android.rendering.Color

/**
 * A palette used for voxel models.
 */
class Palette
{
    /**
     * The color values.
     */
    var colors: Array<Color> = defaultColors.clone()
        private set

    /**
     * Retrieve voxel color based on given palette index
     *
     * @param index The palette index to use
     * @return Voxel color at given palette index
     */
    fun colorAt(index: UByte): Color
    {
        // Check for empty chunk
        if(index == 0U.toUByte())
            throw IllegalArgumentException("Tried to retrieve color for empty chunk with value 0")

        // Retrieve integer value
        val intIndex = index.toInt()

        // No bounds check required.
        return this.colors[intIndex]
    }

    companion object
    {
        /**
         * The default palette. For now, completely white.
         */
        val defaultColors: Array<Color> = Array(256) { Color(1f, 1f, 1f, 1f) }
    }
}