package common;

import sun.security.util.ArrayUtil;
import util.GLTypeSizes;

import static org.lwjgl.opengl.GL11.*;

public class VertexAttribSetup
{
	public class Attrib
	{
		public int size;
		public int glType = GL_FLOAT;

		public VertexDataType dataType;

		public Attrib(VertexDataType dataType)
		{
			this.dataType = dataType;
			glType = dataType.glType;
			size = dataType.size;
		}

		public Attrib(int size) {
			this.size = size;
		}

		public boolean skip = false;

		//TODO strides and offsets should be stored in VertexAttribSetup
		public int strideBytes;
		public int offsetBytes;
	}

	private Attrib[] attribs;

	private void fillSetup()
	{
		int offset = 0;
		for (Attrib attrib : attribs)
		{
			attrib.offsetBytes = offset;

			offset += attrib.size * GLTypeSizes.sizeof(attrib.glType);
		}

		int stride = offset;
		for (Attrib attrib : attribs)
			attrib.strideBytes = stride;
	}

	public VertexAttribSetup(Attrib[] attribs)
	{
		this.attribs = attribs;

		fillSetup();
	}

	public VertexAttribSetup(VertexDataType[] dataTypes)
	{
		attribs = new Attrib[dataTypes.length];

		for (int i = 0; i < dataTypes.length; i++)
			attribs[i] = new Attrib(dataTypes[i]);

		fillSetup();
	}

	public VertexAttribSetup(int[] attribSizes)
	{
		attribs = new Attrib[attribSizes.length];
		for (int i = 0; i < attribSizes.length; i++)
			attribs[i] = new Attrib(attribSizes[i]);

		fillSetup();
	}

	public int getSize()
	{
		int size = 0;
		for (Attrib attr: attribs)
			size += attr.size;

		return size;
	}

	public Attrib[] getSetup()
	{
		return attribs;
	}
}
