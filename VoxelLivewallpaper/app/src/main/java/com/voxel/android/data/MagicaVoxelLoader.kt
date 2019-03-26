package com.voxel.android.data

import android.util.Log
import com.voxel.android.rendering.Color
import java.io.BufferedInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and

fun ByteBuffer.getUnsignedByte(): UByte
{
    return (this.get().toShort() and 0xFF).toUByte()
}

/**
 * A voxel model loader implementation capable of loading models in the MagicaVoxel (.vox)
 * format.
 */
class MagicaVoxelLoader: VoxelModelLoader
{
    /**
     * All chunk headers that are used in the MagicaVoxel file format
     *
     * @property identifier The textual identifier used in the file format
     */
    enum class ChunkHeader(val identifier: String)
    {
        Main("MAIN"),
        Size("SIZE"),
        VoxelData("XYZI"),
        Palette("RGBA"),
        Pack("PACK"),
        Unknown("")
    }

    /**
     * The file formats magic number string. The space is intentional.
     */
    private val magicNumber = "VOX "

    /**
     * Try to load voxel model frames from MagicaVoxel file
     *
     * @param stream Input stream to use
     * @return Voxel model that was loaded from file
     */
    override fun load(stream: InputStream): VoxelModel
    {
        // Create voxel model instance
        val model = VoxelModel()

        // Create byte buffer to allow little-endian reading
        val input: ByteBuffer = ByteBuffer.wrap(stream.readBytes())
        input.order(ByteOrder.LITTLE_ENDIAN)

        // Check magic number
        val string = this.readAscii(input, 4)
        if(string != this.magicNumber)
        {
            throw IllegalStateException("Expected MagicaVoxel magic number but got \"$string\"")
        }

        // Ignore version number
        input.int

        // The first chunk should be the MAIN chunk.
        if(this.readChunkId(input) != ChunkHeader.Main)
        {
            throw IllegalStateException("Expected main chunk")
        }

        // Sink two integers (num bytes of chunk content and children chunks)
        input.int
        input.int

        // The number of submodels (frames) we have to expect. If a PACK chunk is not
        // supplied, this defaults to 1.
        var frameCount = 1

        // Check if there is a PACK chunk that tells us how many submodels to expect
        val nextChunk = this.readChunkId(input)

        // If that is the case, we extract the number of models from the pack chunk.
        if(nextChunk == ChunkHeader.Pack)
            frameCount = this.readPackChunk(input)

        Log.d("MagicaVoxelLoader", "Expecting $frameCount sub models")

        // Try to read all frames
        while(frameCount-- > 0)
        {
            // Read voxel model frame
            val frame: VoxelModelFrame = this.readFrame(input)

            // Add to model
            model.frames.add(frame)
        }

        // There might be a palette chunk following this one.
        if(this.readChunkId(input) == ChunkHeader.Palette)
        {
            // Read palette and assign to model
            this.readPaletteChunk(input, model.palette)
        }

        // All done
        return model
    }

    /**
     * Try to read palette chunk
     *
     * @param input Stream to read from
     * @param palette Palette instance to store retrieved data in
     * @return Palette that was read from stream
     */
    private fun readPaletteChunk(input: ByteBuffer, palette: Palette): Palette
    {
        // Sink two integers
        input.int
        input.int

        // There are 255 entries starting at 0. We do need to store them beginning with
        // 1 though, since the value 0 is reserved for empty voxels. This is also consistent with magica
        // voxels way of storing palette indices (they start at 1)
        for(i in 0 .. 254)
        {
            val r = input.getUnsignedByte().toInt()
            val g = input.getUnsignedByte().toInt()
            val b = input.getUnsignedByte().toInt()
            val a = input.getUnsignedByte().toInt()

            val rf = r.toFloat() / 255f
            val gf = g.toFloat() / 255f
            val bf = b.toFloat() / 255f
            val af = a.toFloat() / 255f

            palette.colors[i] = Color(rf, gf, bf, af)
        }

        // All done
        return palette
    }

    /**
     * Try to read pack chunk.
     *
     * @param input Stream to read from
     * @return The number of frames to expect.
     */
    private fun readPackChunk(input: ByteBuffer): Int
    {
        // Sink two integers
        input.int
        input.int

        // Its simply an integer.
        return input.int
    }

    /**
     * Read ASCII string from binary file.
     *
     * @param input Stream to read from
     * @param count Number of characters to read. Has to be positive.
     * @return String that was read from the stream
     */
    private fun readAscii(input: ByteBuffer, count: Int): String
    {
        // Check count for valid range
        if(count <= 0)
            throw IllegalArgumentException("count has to be positive")

        val chars = ByteArray(count) { input.get() }
        return String(chars, Charsets.US_ASCII)
    }

    /**
     * Try to read a frame/submodel
     *
     * @param input Stream to read from
     * @return The frame that was read from stream
     */
    private fun readFrame(input: ByteBuffer): VoxelModelFrame
    {
        // There has to be a size chunk first
        if(this.readChunkId(input) != ChunkHeader.Size)
            throw IllegalStateException("Expected SIZE chunk")

        // Obtain size information
        val dimensions = this.readSizeChunk(input)

        // Create model frame
        val frame = VoxelModelFrame(dimensions)

        // Check that next chunk is a voxel data chunk
        if(this.readChunkId(input) != ChunkHeader.VoxelData)
            throw IllegalStateException("Expected SIZE chunk")

        // Load voxel data
        this.readVoxelDataChunk(input, frame)

        // All done
        return frame
    }

    /**
     * Try to read voxel data chunk
     *
     * @param input The stream to read from
     * @param frame The voxel model frame to populate with data
     */
    private fun readVoxelDataChunk(input: ByteBuffer, frame: VoxelModelFrame)
    {
        // Sink two integers
        input.int
        input.int

        // Read the number of voxels
        val voxelCount = input.int

        if(voxelCount == 0)
            throw IllegalStateException("Encountered submodel with no voxels")

        // Try to read voxel information
        for(i in 0 until voxelCount)
        {
            // Note that y and z are flipped. In this engine, y is height, and not z.
            val x = input.getUnsignedByte().toInt()
            val y = input.getUnsignedByte().toInt()
            val z = input.getUnsignedByte().toInt()
            val paletteIndex = input.getUnsignedByte()

            frame.setColorIndexAt(x, z, y, paletteIndex)
        }
    }

    /**
     * Try to read SIZE chunk
     */
    private fun readSizeChunk(input: ByteBuffer): VoxelModelDimensions
    {
        // Sink two integers
        input.int
        input.int

        // Note: In magicavoxel, z is the height of the model.
        val width = input.int
        val depth = input.int
        val height = input.int

        return VoxelModelDimensions(width, height, depth)
    }

    /**
     * Tries to read next chunk id.
     *
     * @param stream Stream to read from
     * @return In case of success, ChunkHeader instance that fits encountered chunk type
     */
    private fun readChunkId(stream: ByteBuffer): ChunkHeader
    {
        // Read in ASCII string describing the chunk type
        val string = this.readAscii(stream, 4)

        return when(string)
        {
            "MAIN" -> ChunkHeader.Main
            "SIZE" -> ChunkHeader.Size
            "XYZI" -> ChunkHeader.VoxelData
            "PACK" -> ChunkHeader.Pack
            "RGBA" -> ChunkHeader.Palette
            else -> {
                throw RuntimeException("Unknown chunk type \"$string\"")
            }
        }
    }
}