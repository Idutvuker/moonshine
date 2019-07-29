#version 330 core

in VS_OUT
{
    vec3 FragPos;
    vec3 Normal;
    vec2 TexCoord;
} fs_in;

out vec4 FragColor;

uniform vec4 uf_color = vec4(1.f);

uniform vec3 light_pos = vec3(5.0, 10.0, 4.0);

void main()
{

    vec3 light_dir = normalize(light_pos);

    float ambient = 0.1;
    float diffuse = max(dot(normalize(fs_in.Normal), light_dir), 0.0);

    FragColor = vec4(vec3(ambient + diffuse), 1.0f);
}