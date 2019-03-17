package com.voxel.android.rendering

import org.joml.Quaternionf
import org.joml.Vector3f

/**
 * An abstract class for meshes that contains properties for all types of affine transformations,
 * thus allowing users of derived classes to transform them arbitrarily without those classes having
 * to reimplement that functionality.
 */
abstract class TransformedMesh(material: Material, primitiveType: PrimitiveType = PrimitiveType.Triangles):
    TransformableMesh(material, primitiveType)
{
    /**
     * Translation to apply to the mesh
     */
    var translation: Vector3f = Vector3f()

    /**
     * Scaling in X direction
     */
    var scaleX: Float = 1.0f

    /**
     * Scaling in Y direction
     */
    var scaleY: Float = 1.0f

    /**
     * Scaling in Z direction
     */
    var scaleZ: Float = 1.0f

    /**
     * Uniform scaling in all directions
     */
    var scale: Float = 1.0f

    /**
     * Rotation, expressed as a quaternion
     */
    var rotation: Quaternionf = Quaternionf()

    /**
     * Rotation around the X axis, in radians
     */
    var rotationX: Float = 0.0f

    /**
     * Rotation around the Y axis, in radians
     */
    var rotationY: Float = 0.0f

    /**
     * Rotation around the Y axis, in radians
     */
    var rotationZ: Float = 0.0f

    /**
     * Apply transformations
     */
    override fun transform(params: RenderParams)
    {
        // The last transformation call to render parameters is the
        // first transformation that will be applied. This will be fixed later.

        // First scale it. We first do the individual scalings, and then the uniform scaling.
        params.scale(this.scale)
        params.scaleX(this.scaleX)
        params.scaleY(this.scaleY)
        params.scaleZ(this.scaleZ)

        // Now rotate it. We first do the individual rotations.
        params.rotate(this.rotation)
        params.rotateX(this.rotationX)
        params.rotateY(this.rotationY)
        params.rotateZ(this.rotationZ)

        // Lastly, apply translation
        params.translate(this.translation)
    }
}