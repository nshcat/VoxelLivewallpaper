package com.voxel.android.application

/**
 * A class encapsulating the dimensions of the screen that is the current rendering target.
 *
 * @property width The width of the screen, in pixels
 * @property height The height of the screen, in pixels
 */
class ScreenDimensions(val width: Int, val height: Int)
{
    /**
     * The aspect ratio of the screen
     */
    val aspectRatio: Float = width.toFloat() / height.toFloat()
}
