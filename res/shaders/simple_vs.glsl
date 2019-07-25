#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 1) in vec3 texCoord;

out vec3 fs_normal;

uniform mat4 uf_ModelViewMat;
uniform mat4 uf_MVPMat;

void main()
{
    vec4 pos4 = vec4(position, 1.0);
    gl_Position = uf_MVPMat * pos4;

    fs_normal = vec3(uf_ModelViewMat * pos4);
}