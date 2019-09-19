layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;

out vec3 pos;

uniform mat4 uf_MVPMat;

void main()
{
    gl_Position = uf_MVPMat * vec4(position, 1.0);
    pos = normal;
}