package com.voxel.android.rendering

import android.opengl.GLES31

/**
 * A class representing a white quad spanning the whole screen. This is meant for testing purposes.
 */
class FullscreenQuad: Mesh(FullscreenQuadMaterial())
{
    // TODO: make this class more useful by allowing the use of a texture. TextureRenderTarget could
    // then be the source of that texture.

    override fun render(params: RenderParams)
    {
        // Make sure the material is loaded and setup correctly
        super.render(params)

        // Render the quad using instancing. The material shader will generate the vertices
        // for use based on the instance vertex id.
        GLES31.glDrawArraysInstanced(GLES31.GL_TRIANGLES, 0, 6, 1)
    }
}