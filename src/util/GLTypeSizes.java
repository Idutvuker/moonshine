package util;

import static org.lwjgl.opengl.GL11.*;

public class GLTypeSizes
{
	public static int sizeof(int type)
	{
		switch (type)
		{
			case GL_BYTE: return 1;
			case GL_UNSIGNED_BYTE: return 1;

			case GL_SHORT: return 2;
			case GL_UNSIGNED_SHORT: return 2;

			case GL_INT: return 4;
			case GL_UNSIGNED_INT: return 4;

			case GL_FLOAT: return 4;

			case GL_2_BYTES: return 2;
			case GL_3_BYTES: return 3;
			case GL_4_BYTES: return 4;
			case GL_DOUBLE: return 8;
		}

		System.err.println("Size of nonGL type, returned 0");
		return 0;
	}
}
