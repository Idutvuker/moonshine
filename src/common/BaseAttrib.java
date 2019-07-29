package common;

public class BaseAttrib
{
	public int size;
	public int glType;

	public VertexDataType dataType;

	public BaseAttrib(VertexDataType dataType)
	{
		this.dataType = dataType;
		glType = dataType.glType;
		size = dataType.size;
	}

	public BaseAttrib() {}

	public BaseAttrib(int size) {
		this.size = size;
	}

	public boolean skip = false;
}
