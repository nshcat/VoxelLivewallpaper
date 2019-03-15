package com.voxel.android.rendering

/**
 * The material used with the class [FullscreenQuad]. It does not have any if the MVP matrices
 * since it directly operates in clip space.
 */
class FullscreenQuadMaterial: Material(ShaderProgram(
        Shader.FromResource(ShaderType.FragmentShader, "res/raw/quad_fs.glsl"),
        Shader.FromResource(ShaderType.VertexShader, "res/raw/quad_vs.glsl")
    ))
{

    /**
     * Apply rendering parameters. This type of material does not have any attributes
     * that need uploading.
     */
    override fun applyParameters(params: RenderParams)
    {
        // Do nothing, since the material shader program does not include any uniforms
        return
    }
}