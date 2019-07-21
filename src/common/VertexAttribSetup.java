package common;

import util.GLTypeSizes;

import static org.lwjgl.opengl.GL11.*;

public class VertexAttribSetup
{
	public class Attrib
	{
		public boolean skip = false;
		public int size;
		public int type = GL_FLOAT;
		public int stride;
		public int offset;

		public String name = "Undefined";

		public Attrib() {}

		public Attrib(int size) {
			this.size = size;
		}

		public Attrib(int size, int type) {
			this.size = size;
			this.type = type;
		}

		public Attrib(int size, int type, String name)
		{
			this.size = size;
			this.type = type;
			this.name = name;
		}


		@Override
		public String toString() {
			return name + ": " + size + " bytes";
		}
	}


	private Attrib[] attribs;

	private void fillSetup()
	{
		int offset = 0;
		for (Attrib attrib : attribs)
		{
			attrib.offset = offset;

			offset += attrib.size * GLTypeSizes.sizeof(attrib.type);
		}

		int stride = offset;
		for (Attrib attrib : attribs)
			attrib.stride = stride;
	}

	public VertexAttribSetup(Attrib[] attribs)
	{
		this.attribs = attribs;

		fillSetup();
	}

	public VertexAttribSetup(int[] attribSizes)
	{
		attribs = new Attrib[attribSizes.length];
		for (int i = 0; i < attribSizes.length; i++)
			attribs[i] = new Attrib(attribSizes[i]);

		fillSetup();
	}

	public void setAttrib(int attrib, int size)
	{
		attribs[attrib].size = size;
	}

	public Attrib[] getSetup()
	{
		return attribs;
	}
}
