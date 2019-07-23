#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;

out vec3 f_normal;

uniform mat4 MVPmat;

void main()
{
    gl_Position = MVPmat * vec4(position, 1.0);

    f_normal = normal;//normalize(position);
}