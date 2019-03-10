package com.voxel.android.rendering

/**
 * An abstract base class for all types of simple meshes that can be rendered to screen
 * using a material and a set of render parameters. Note that this class cannot
 * use instances of the Material class for rendering, use ShadedMesh for that.
 *
 * @property material The material to use when rendering instances of this class
 */
abstract class Mesh (val material: Material): Renderable
