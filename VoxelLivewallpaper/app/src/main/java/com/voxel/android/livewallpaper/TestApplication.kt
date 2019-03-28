package com.voxel.android.livewallpaper

import android.content.Context
import android.util.Log
import com.voxel.android.R
import com.voxel.android.application.Application
import com.voxel.android.application.ScreenDimensions
import com.voxel.android.data.MagicaVoxelLoader
import com.voxel.android.data.VoxelModelLoader
import com.voxel.android.procedual.TerrainDimensions
import com.voxel.android.procedual.VoxelTerrain
import com.voxel.android.rendering.*
import org.joml.Vector3f
import java.io.BufferedInputStream
import java.nio.Buffer
import kotlin.math.PI

class TestApplication (context: Context): Application(context)
{
    /**
     * The cube mesh we are going to render. The constructor allocated OpenGL resources,
     * so we have to wait with the initialization until later.
     */
    private lateinit var mesh: CubeMesh

    /**
     * A texture render target as our first render pass
     */
    private lateinit var firstPass: TextureTarget

    /**
     * The screen as our second render pass
     */
    private lateinit var secondPass: ScreenTarget

    /**
     * The coordinate system visualizer
     */
    private lateinit var coordinateSystem: CoordinateSystem

    /**
     * A full screen quad we use to render the scene to screen in the second pass
     */
    private lateinit var fullscreenQuad: FullscreenQuad

    /**
     * The voxel mesh used to test the voxel model loader
     */
    private lateinit var testVoxelMesh: VoxelMesh

    /**
     * Test voxel terrain
     */
    private lateinit var terrain: VoxelTerrain

    /**
     * The current rotation angle
     */
    private var angle = 0.0f

    /**
     * The camera that will capture our scene
     */
    private val camera: Camera = Camera(
            Vector3f(5f, 4f, 5f),
            Vector3f(),
            Vector3f(Axis.Y),
            90f,
            ScreenDimensions.empty())


    override fun onScreenChanged(screenDimensions: ScreenDimensions)
    {
        if(::mesh.isInitialized)
        {
            // Force the screen to resize
            this.firstPass.updateDimensions(screenDimensions.scaleDown(1.0f))
            this.secondPass.updateDimensions(screenDimensions)
            this.camera.refreshProjection(screenDimensions)
        }
    }

    override fun onScreenCreated()
    {
        // The render target might also have OpenGL state that needs to be lazily created
        this.firstPass = TextureTarget()
        this.secondPass = ScreenTarget()

        // The mesh also has to be recreate, in case it has been uploaded before
        this.mesh = CubeMesh().apply { scale = 2.0f; translation = Vector3f(-.5f, 0f,-.5f) }

        // Create the fullscreen quad
        this.fullscreenQuad = FullscreenQuad()

        // Create the coordinate system
        this.coordinateSystem = CoordinateSystem()

        // Test the voxel model loader
        val modelLoader = MagicaVoxelLoader()
        val model = modelLoader.load(
                this.context.resources.openRawResource(R.raw.fox)
        )

        this.testVoxelMesh = VoxelMesh(model.frames[0], model.palette).apply { scale = 0.25f }

        // Test voxel terrain
        this.terrain = VoxelTerrain(
                TerrainDimensions(3f, 3f),
                50f,
                1f,
                0.5f
        ).apply { scale *= 2 }
    }

    override fun onFrame(elapsedSeconds: Double)
    {
        // It might happen that we are requested to draw a frame while we havent been initialized.
        // Prohibit that.
        if(!::firstPass.isInitialized || !::mesh.isInitialized )
            return

        // Update rotation angle
        this.mesh.rotationY += (elapsedSeconds.toFloat() * 1.0f * PI.toFloat())

        this.testVoxelMesh.rotationY += (elapsedSeconds.toFloat() * 1.0f * PI.toFloat())

        // Update our camera
        this.camera.update(elapsedSeconds)

        // Begin rendering to texture
        this.firstPass.beginRender()

        // First render the coordinate system
        //this.coordinateSystem.render(this.camera.toRenderParams().apply { scale(2.5f) })

        // Render our mesh
        //this.mesh.render(this.camera.toRenderParams())

        // Render the other mesh
        //this.testVoxelMesh.render(this.camera.toRenderParams())
        this.terrain.render(this.camera.toRenderParams())

        // Begin rendering to main screen
        this.firstPass.endRender()

        // Now do the second rendering pass where we render to a full screen quad
        this.secondPass.beginRender()

        // Use the texture the first pass rendered to. The full screen quad material
        // expects it to be bound to the first texture unit.
        this.firstPass.texture.use(TextureUnit.Unit0)

        // Render the fullscreen quad. The material doesnt use any of the rendering parameters,
        // so we just pass an empty instance here.
        this.fullscreenQuad.render(RenderParams.empty())

        // Finish rendering
        this.secondPass.endRender()
    }
}