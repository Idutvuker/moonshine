#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec2 fs_texCoord;

uniform mat4 uf_MVPMat;

void main()
{
    gl_Position = uf_MVPMat * vec4(position, 1.0);

    fs_texCoord = texCoord;
}