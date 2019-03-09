#version 310 es

precision mediump float;

in vec2 tex_coords;
flat in vec4 front_color;
flat in vec4 back_color;

uniform sampler2D tex;

layout (location = 0) out vec4 fragmentColor;

// SYMPTOM: Ganzer bildschirm ist oben linkes pixel der textur.
vec4 calc_pixel()
{
	vec4 t_texColor = texture(tex, tex_coords);

    //return vec4(t_texColor.r, t_texColor.g, t_texColor.b, 1.f);

    //return mix(back_color, front_color, t_texColor.r);

	return mix(	back_color,
				front_color * t_texColor,
				t_texColor.a );
}


void main()
{
    fragmentColor = calc_pixel();
}
