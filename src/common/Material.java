package common;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Material
{
	ShaderProgram shaderProgram;
	Texture texture;

	public Material(String vertexShaderFilename, String fragmentShaderFilename)
	{
		Shader vs = new Shader(vertexShaderFilename, GL_VERTEX_SHADER);
		Shader fs = new Shader(fragmentShaderFilename, GL_FRAGMENT_SHADER);

		shaderProgram = new ShaderProgram(vs, fs);

		vs.delete();
		fs.delete();
	}

	public void setTexture(String textureFilename)
	{
		texture = new Texture(textureFilename);
	}

	public void use()
	{
		if (texture != null)
			texture.bind();

		shaderProgram.use();
	}

	public void setMVP(FloatBuffer MVPbuffer)
	{
		int MVPloc = glGetUniformLocation(shaderProgram.getID(), "MVPmat");

		glUniformMatrix4fv(MVPloc, false, MVPbuffer);
	}
}
