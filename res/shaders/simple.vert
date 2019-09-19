

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 texCoord;

out VS_OUT
{
    vec3 FragPos;
    vec3 Normal;
    vec2 TexCoord;
} vs_out;

uniform mat4 uf_ModelMat;
uniform mat4 uf_MVPMat;

void main()
{
    vec4 pos4 = vec4(aPosition, 1.0);
    gl_Position = uf_MVPMat * pos4;


    vs_out.FragPos = mat3(uf_ModelMat) * aPosition;

    vs_out.Normal = mat3(uf_ModelMat) * aNormal;

    vs_out.TexCoord = texCoord;
}