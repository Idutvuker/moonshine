package common;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram
{
	private int shaderProgramID;

	public ShaderProgram(Shader vertexShader, Shader fragmentShader)
	{

		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexShader.getShaderID());
		glAttachShader(shaderProgramID, fragmentShader.getShaderID());

		glLinkProgram(shaderProgramID);

		if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE)
		{
			String log = glGetProgramInfoLog(shaderProgramID);
			System.err.println("Shader program linking error:\n" + log);
		}
	}

	public void use()
	{
		glUseProgram(shaderProgramID);
	}

	public int getID() {
		return shaderProgramID;
	}
}
