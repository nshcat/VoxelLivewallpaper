package com.voxel.android.rendering

import com.voxel.android.application.ScreenDimensions

/**
 * An entity that can be rendered to using OpenGL, for example the screen framebuffer or a render
 * target texture
 */
interface RenderTarget
{
    /**
     * Force the render target to change its dimensions
     *
     * @param renderDimensions The new dimensions of the render target
     */
    fun updateDimensions(renderDimensions: ScreenDimensions)

    /**
     * Begin rendering to this target. All following render calls will result in this render
     * target being used.
     */
    fun beginRender()

    /**
     * Stop rendering to this target.
     */
    fun endRender()
}

