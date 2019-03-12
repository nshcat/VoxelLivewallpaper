package com.voxel.android.rendering

import com.voxel.android.application.ScreenDimensions
import org.joml.Vector3f
import org.joml.Matrix4f

/**
 * A class representing the main camera in the scene.
 *
 * @property position The position of the camera in world space
 * @property target The center point of the scene, which the camera points to, in world space
 * @property up The vector describing what "up" is for the camera
 * @property fov The desired field of view of the camera
 * @property navigation The navigation to use to control the camera
 * @param screenDimensions The dimensions of the screen
 */
class Camera (var position: Vector3f,
              var target: Vector3f,
              var up: Vector3f,
              private val fov: Float,
              screenDimensions: ScreenDimensions,
              private val navigation: Navigation = FixedNavigation())
{
    /**
     *  The current view matrix. If any camera state changes, this has to be updated
     *  via a call to [refreshView]
     */
    var view: Matrix4f = Matrix4f()
        protected set

    /**
     * The current projection matrix. This has to be updated via
     */
    var projection: Matrix4f = Matrix4f()
        protected set


    /**
     * Initialize camera using given screen dimensions. This will set up both
     * the projection as well as view matrix for first use.
     */
    init
    {
        this.refreshProjection(screenDimensions)
        this.navigation.initialize(this)
        this.refreshView()
    }

    /**
     * Causes the camera to update its view matrix based on the current
     * values for target, position etc
     */
    fun refreshView()
    {
        this.view.setLookAt(this.position, this.target, this.up)
    }

    /**
     * Update the stored projection matrix to conform to the given screen dimensions
     */
    fun refreshProjection(screenDimensions: ScreenDimensions)
    {
        // Overwrite current projection matrix
        this.projection.setPerspective(this.fov, screenDimensions.aspectRatio, 0f, 100f)
    }

    /**
     * Update camera and navigation state, advancing [delta] seconds of simulation time
     */
    fun update(delta: Double)
    {
        this.navigation.update(delta)
    }

    /**
     * Retrieve rendering parameters based on the current camera state
     *
     * @return Rendering parameters base on current camera state
     */
    fun toRenderParams(): RenderParams
    {
        return RenderParams(this.view, this.projection)
    }
}