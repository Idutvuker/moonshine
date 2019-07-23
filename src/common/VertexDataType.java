package common;

import static org.lwjgl.opengl.GL11.*;

public enum VertexDataType
{
	POSITION(3, GL_FLOAT),
	TEXCOORD(2, GL_FLOAT),
	NORMAL(3, GL_FLOAT);

	public int glType;
	public int size;

	VertexDataType(int size, int glType)
	{
		this.size = size;
		this.glType = glType;
	}
}
