#version 310 es

in vec4 fragmentColor;
in vec3 fragmentPosition;
in vec3 fragmentNormal;
out vec4 color;

void main()
{
    vec3 n = normalize(fragmentNormal);
    if (!gl_FrontFacing)
        n = -n;
        
    color = fragmentColor * (1.0 + dot(normalize(vec3(1.0) - fragmentPosition), n));
    //color = fragmentColor;
}
