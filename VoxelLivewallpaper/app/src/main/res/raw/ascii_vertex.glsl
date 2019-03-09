#version 310 es
#extension GL_EXT_texture_buffer : enable

// fr, fg, fb, glyph
// br, bg, bb, not_used
precision highp usamplerBuffer;

uniform mat4 proj_mat;
uniform int screen_width;  // In glyphs
uniform int screen_height;
uniform int glyph_width;  // In glyphs
uniform int glyph_height;
uniform int sheet_width;  // In glyphs
uniform int sheet_height;
uniform usamplerBuffer input_buffer;

const vec2 vertex_offset[] = vec2[6](
	vec2(1, 1),	// BR
	vec2(1, 0),	// TR
	vec2(0, 0),	// TL

	vec2(1, 1),	// BR
	vec2(0, 0),	// TL
	vec2(0, 1)	// BL
);

const vec2 texture_offset[] = vec2[6](
	vec2(1, 1),	// BR
	vec2(1, 0),	// TR
	vec2(0, 0),	// TL

	vec2(1, 1),	// BR
	vec2(0, 0),	// TL
	vec2(0, 1)	// BL
);

out vec2 tex_coords;
flat out vec4 front_color;
flat out vec4 back_color;



// Calculate vertex for this shader call
void emit_vertex()
{
    // Calculate screen coords in glyphs
    vec2 screen_coords = vec2(
        gl_InstanceID % screen_width,
        gl_InstanceID / screen_width
    );

	// Calculate absolute (in world space) top left coordinates of this cell
	vec2 t_tl = screen_coords * vec2(float(glyph_width), float(glyph_height));

	// Add offset for vertices that are not the top left one
	t_tl += vertex_offset[gl_VertexID] * vec2(float(glyph_width), float(glyph_height));

	// Create homogenous 4D vector and transform using projection matrix,
	// which implements an orthographic view frustum where the y-axis is flipped.
	// This allows us to use "screen-like" coordinates (with the origin being
	// the top left corner of the screen, and the y-axis growing downwards)
	// in world space.
	gl_Position = proj_mat * vec4(t_tl, 0.f, 1.f);
}

void emit_color(uvec4 high, uvec4 low)
{
    vec3 front = vec3(
        float(high.r) / 255.f,
        float(high.g) / 255.f,
        float(high.b) / 255.f
    );


    front_color = vec4(front, 1.f);
    back_color = vec4(vec3(low.rgb) / 255.f, 1.f);
}

void emit_tex_coords(uvec4 high, uvec4 low)
{
    // Dimension of a single glyph texture in texture space (UV)
    vec2 t_dimTex = vec2(1.f/float(sheet_width), 1.f/float(sheet_height));


    // CAUSE: This doesnt work! When taking hard coded values here, everything works just fine.
    vec2 glyph = vec2(
        high.a % uint(sheet_width),
        high.a / uint(sheet_width)
    );

    /*vec2 glyph = vec2(8.f, 8.f);

    if(high.a == 5U)
        glyph = vec2(3.f, 0.f);*/


    // Calculate texture coordinates of top left vertex
    vec2 t_tl = t_dimTex * glyph;

    // If this vertex is in fact not the top left one, we need to add an offset
    // to calculate the texture coordinates.
    // This is simply done by adding the offset (which is either 0 or 1 in both
    // x and y direction) multiplied by the size of one glyph in texture space.
    // We will receive one of the four corners of the glyph texture.
    t_tl += texture_offset[gl_VertexID] * t_dimTex;

    // Write value to output interface block
    tex_coords = t_tl;
}

uint switch_endianess(uint num)
{
    uint swapped = ((num>>24U)&0xffU) | // move byte 3 to byte 0
                        ((num<<8U)&0xff0000U) | // move byte 1 to byte 2
                        ((num>>8U)&0xff00U) | // move byte 2 to byte 1
                        ((num<<24U)&0xff000000U); // byte 0 to byte 3

    return swapped;
}

uvec4 switch_vec_endianess(uvec4 vec)
{
    return uvec4(
        switch_endianess(vec.r),
        switch_endianess(vec.g),
        switch_endianess(vec.b),
        switch_endianess(vec.a)
    );
}

void main()
{
   	emit_vertex();

    uvec4 t_high = texelFetch(input_buffer, gl_InstanceID*2);
    uvec4 t_low = texelFetch(input_buffer, (gl_InstanceID*2)+1);

    t_high = switch_vec_endianess(t_high);
    t_low = switch_vec_endianess(t_low);

   	emit_color(t_high, t_low);

   	emit_tex_coords(t_high, t_low);

    /*if(gl_InstanceID == 1)
        tex_coords = vec2(1.f, 1.f);
    else if (gl_InstanceID == 2)
        tex_coords = vec2(1.f, 0.f);
    else if (gl_InstanceID == 3)
        tex_coords = vec2(0.f, 1.f);
    else
        tex_coords = vec2(0.f, 0.f);*/
}
