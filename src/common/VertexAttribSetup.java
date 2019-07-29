package common;

import util.GLTypeSizes;

public class VertexAttribSetup
{
	class LockedAttrib
	{
		public final int size;
		public final int glType;
		public final int strideBytes;
		public final int offsetBytes;

		private LockedAttrib(int size, int glType, int strideBytes, int offsetBytes) {
			this.size = size;
			this.glType = glType;
			this.strideBytes = strideBytes;
			this.offsetBytes = offsetBytes;
		}
	}

	private LockedAttrib[] setup;

	public int getSize() {
		return setup.length;
	}

	public LockedAttrib getAttr(int index)
	{
		return setup[index];
	}

	private void fillSetup(BaseAttrib[] attribs)
	{
		setup = new LockedAttrib[attribs.length];

		int stride = 0;
		for (int i = 0; i < attribs.length; i++)
			stride += attribs[i].size * GLTypeSizes.sizeof(attribs[i].glType);

		int offset = 0;

		for (int i = 0; i < attribs.length; i++) {
			setup[i] = new LockedAttrib(attribs[i].size, attribs[i].glType, stride, offset);
			offset += attribs[i].size * GLTypeSizes.sizeof(attribs[i].glType);
		}
	}

	public VertexAttribSetup(BaseAttrib[] attribs)
	{
		fillSetup(attribs);
	}

	public VertexAttribSetup(VertexDataType[] dataTypes)
	{
		BaseAttrib[] attribs = new BaseAttrib[dataTypes.length];

		for (int i = 0; i < dataTypes.length; i++)
			attribs[i] = new BaseAttrib(dataTypes[i]);

		fillSetup(attribs);
	}

	public int getLayoutSize()
	{
		int size = 0;
		for (LockedAttrib attr: setup)
			size += attr.size;

		return size;
	}
}
