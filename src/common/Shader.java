package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL33.*;

public class Shader
{
	private int shaderID;

	public Shader(String filename, int type)
	{
		StringBuilder shaderSource = new StringBuilder();

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;

			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");

			reader.close();

		} catch (IOException e)
		{
			System.err.println("Could not open file " + filename);
			e.printStackTrace();
		}

		shaderID = glCreateShader(type);

		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);


		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Compile error:\n" + glGetShaderInfoLog(shaderID));
		}
	}

	public int getShaderID() {
		return shaderID;
	}

	public void delete()
	{
		glDeleteShader(shaderID);
	}
}
