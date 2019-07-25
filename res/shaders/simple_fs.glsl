#version 330 core

in vec3 fs_normal;

out vec4 color;

const vec3 light_dir = normalize(vec3(1, 1, 1));

void main()
{
    float ambient = 0.1;
    float diffuse = max(dot(fs_normal, light_dir), 0);

    color = vec4(vec3(ambient + diffuse), 1.f);
}