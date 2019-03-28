package com.voxel.android.procedual

import android.util.Log
import com.voxel.android.procedual.SimplexNoise
import com.voxel.android.rendering.TransformedMesh
import com.voxel.android.rendering.VoxelFace
import com.voxel.android.rendering.materials.SimpleMaterial
import org.joml.Vector3f
import org.joml.Vector4f

/**
 * A class used to procedually generate the voxel terrain.
 *
 * @property dimensions The dimensions of the terrain, in meters
 * @property voxelScale The voxel resolution of the terrain, in voxels/meter
 * @property frequency The frequency the noise source is sampled at
 * @property amplitude The amplitude of the terrain, which is the highest possible height value
 */
class VoxelTerrain(val dimensions: TerrainDimensions, val voxelScale: Float, val frequency: Float, val amplitude: Float): TransformedMesh(SimpleMaterial())
{
    /**
     * The width of the terrain, in voxels.
     */
    private val voxelWidth: Int = (dimensions.width * voxelScale).toInt()

    /**
     * The depth of the terrain, in voxels
     */
    private val voxelDepth: Int = (dimensions.depth * voxelScale).toInt()

    /**
     * Size of a single voxel, in meters
     */
    private val voxelSize: Float = 1f / this.voxelScale

    /**
     * Create voxel terrain based on perlin noise
     */
    init
    {
        for(ix in 0 until this.voxelWidth)
        {
            for(iz in 0 until this.voxelDepth)
            {
                // Calculate sampling points in noise, between 0.0 and 1.0
                val sampleIx = ((ix.toFloat() / this.voxelWidth.toFloat()).toDouble() + voxelSize/2.0) * this.dimensions.width
                val sampleIz = ((iz.toFloat() / this.voxelDepth.toFloat()).toDouble() + voxelSize/2.0) * this.dimensions.depth

                Log.d("VoxelTerrain", "$sampleIx, $sampleIz")

                // Sample noise
                var noiseVal = SimplexNoise.noise(sampleIx * frequency,sampleIz * frequency)
                noiseVal = (noiseVal + 1.0) / 2.0 // Value between 0 and 1
                noiseVal *= this.amplitude * this.voxelScale

                // Calculate voxel translation
                val translation = Vector3f(ix.toFloat(), noiseVal.toFloat(), iz.toFloat())

                // Generate voxel
                enumValues<VoxelFace>().forEach {
                    this.vertices.addVertices(it.toVertices(Vector4f(0f, 1f, 0f, 1f), translation))
                }
            }
        }

        Log.d("VoxelTerrain", "VoxelSize: $voxelSize")

        // Calculate scaling factor
        this.scale = this.voxelSize
    }
}