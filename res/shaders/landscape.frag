
in vec3 pos;

out vec4 color;

float f(in float x)
{
    return abs(sin(x));
}

const vec3 light_dir = normalize(vec3(1, 5, 1));

void main()
{
    float diffuse = max(dot(normalize(pos), light_dir), 0);

    color = vec4(vec3(diffuse + 0.2f), 1.f);

    //color = vec4(1.f);
}