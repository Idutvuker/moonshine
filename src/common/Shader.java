package common;

import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL33.*;

public class Shader
{
	public static final String VERSION = "330 core";

	private int shaderID;

	public Shader(String filename, int type, @Nullable CharSequence[] defines)
	{
		StringBuilder shaderSource = new StringBuilder();

		shaderSource.append("#version ").append(VERSION).append("\n");

		if (defines != null)
			for (CharSequence def: defines)
				shaderSource.append("#define ").append(def).append("\n");

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
