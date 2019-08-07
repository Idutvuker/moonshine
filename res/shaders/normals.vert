#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;

out vec3 fs_normal;

uniform mat4 uf_ModelMat;
uniform mat4 uf_MVPMat;

void main()
{
    gl_Position = uf_MVPMat * vec4(position, 1.0);

    fs_normal = mat3(uf_ModelMat) * normal;
}