package com.voxel.android.livewallpaper

import android.content.Context
import com.voxel.android.application.Application
import com.voxel.android.application.ScreenDimensions
import com.voxel.android.rendering.*
import junit.framework.Test
import org.joml.Vector3f
import java.util.*
import kotlin.math.PI

class TestApplication (context: Context): Application(context)
{
    /**
     * The cube mesh we are going to render. The constructor allocated OpenGL resources,
     * so we have to wait with the initialization until later.
     */
    private lateinit var mesh: CubeMesh

    /**
     * The screen as our sole render target
     */
    private lateinit var screen: ScreenTarget

    /**
     * The current rotation angle
     */
    private var angle = 0.0f

    /**
     * The camera that will capture our scene
     */
    private val camera: Camera = Camera(
            Vector3f(5f, 5f, 0f),
            Vector3f(),
            Vector3f(Axis.Y),
            90f,
            ScreenDimensions.empty())


    override fun onScreenChanged(screenDimensions: ScreenDimensions)
    {
        if(::mesh.isInitialized)
        {
            // Force the screen to resize
            this.screen.updateDimensions(screenDimensions)
            this.camera.refreshProjection(screenDimensions)
        }
    }

    override fun onScreenCreated()
    {
        // The render target might also have OpenGL state that needs to be lazily created
        this.screen = ScreenTarget()

        // The mesh also has to be recreate, in case it has been uploaded before
        this.mesh = CubeMesh()
    }

    override fun onFrame(elapsedSeconds: Double)
    {
        // It might happen that we are requested to draw a frame while we havent been initialized.
        // Prohibit that.
        if(!::screen.isInitialized || !::mesh.isInitialized )
            return

        // Update rotation angle
        this.angle += (elapsedSeconds.toFloat() * 1.0f * PI.toFloat())

        // Update our camera
        this.camera.update(elapsedSeconds)

        // Begin rendering to main screen
        this.screen.beginRender()

        // Render our mesh
        this.mesh.render(this.camera.toRenderParams().apply { rotateY(angle); scale(2.5f) })

        // Begin rendering to main screen
        this.screen.endRender()
    }
}