package com.voxel.android.rendering

import com.voxel.android.data.Palette
import com.voxel.android.data.VoxelModelFrame
import com.voxel.android.rendering.materials.SimpleMaterial
import com.voxel.android.rendering.materials.SolidColor
import org.joml.Vector3f

/**
 * A class describing the mesh of a single voxel model frame.
 */
class VoxelMesh(model: VoxelModelFrame, palette: Palette): TransformedMesh(SimpleMaterial())
{
    /**
     * Initialize mesh data from voxel model frame.
     */
    init
    {
        // Retrieve frame dimensions
        val dimensions = model.dimensions

        // Go through each voxel in the voxel model
        for(ix in 0 until dimensions.width)
        {
            for(iy in 0 until dimensions.height)
            {
                for(iz in 0 until dimensions.depth)
                {
                    // Only worry about voxels that are actually present
                    if(model.hasVoxelAt(ix, iy, iz))
                    {
                        // Retrieve voxel color value
                        val voxelColor = palette.colorAt(model.colorIndexAt(ix, iy, iz))

                        // Create translation vector. Each voxel is supposed to be 1x1x1 units big.
                        val translation = Vector3f(ix.toFloat() - dimensions.width.toFloat() / 2f, iy.toFloat(), iz.toFloat() - dimensions.depth.toFloat() / 2f)

                        // Create all faces of the cube for now (this can be optimized a lot!)
                        enumValues<VoxelFace>().forEach {
                            this.vertices.addVertices(it.toVertices(voxelColor, translation))
                        }
                    }
                }
            }
        }
    }
}