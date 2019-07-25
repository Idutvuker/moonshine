package materials;

import common.BaseMaterial;
import common.VertexAttribSetup;
import common.VertexDataType;

public class SimpleMaterial extends BaseMaterial
{
	private static final VertexDataType[] attribs = new VertexDataType[] {
			VertexDataType.POSITION,
			VertexDataType.NORMAL,
			VertexDataType.TEXCOORD
	};

	private static final VertexAttribSetup vas = new VertexAttribSetup(attribs);

	public SimpleMaterial()
	{
		super(
				"res/shaders/simple_vs.glsl",
				"res/shaders/simple_fs.glsl",
				vas
		);

		uniformBitSet.set(Uniform.MODEL);
	}
}
