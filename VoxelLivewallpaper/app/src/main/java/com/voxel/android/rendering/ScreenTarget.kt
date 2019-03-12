package com.voxel.android.rendering

import android.opengl.GLES31
import com.voxel.android.application.ScreenDimensions

/**
 * A class encapsulating the main screen as a render target
 */
class ScreenTarget: RenderTarget
{
    /**
     * React to screen dimensions changes
     */
    override fun updateDimensions(renderDimensions: ScreenDimensions)
    {
        // Update the viewport
        GLES31.glViewport(0, 0, renderDimensions.width, renderDimensions.height)
    }

    /**
     * Begin rendering to screen
     */
    override fun beginRender()
    {
        // Clear the screen, both color and depth buffer
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT or GLES31.GL_DEPTH_BUFFER_BIT)
    }

    /**
     * Stop rendering to screen
     */
    override fun endRender()
    {
        // No action required
    }
}