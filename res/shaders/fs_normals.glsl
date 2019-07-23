#version 330 core

in vec3 f_normal;

out vec4 color;

uniform mat4 MVPmat;

void main()
{
    vec3 light_pos = vec3(1, 1, 1);

    float ambient = 0.1;
    float diffuse = max(dot(f_normal, normalize(light_pos)), 0);

    color = vec4(vec3(ambient + diffuse), 1.f);
}