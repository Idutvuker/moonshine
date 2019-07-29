package materials;

import common.BaseMaterial;
import common.Uniform;
import common.VertexAttribSetup;
import common.VertexDataType;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.CallbackI;

import java.nio.FloatBuffer;

public class SimpleMaterial extends BaseMaterial
{
	public static final VertexDataType[] attribs = new VertexDataType[] {
			VertexDataType.POSITION,
			VertexDataType.NORMAL,
			VertexDataType.TEXCOORD
	};

	public static final VertexAttribSetup attribSetup = new VertexAttribSetup(attribs);

	public SimpleMaterial()
	{
		super(
				"res/shaders/simple_vs.glsl",
				"res/shaders/simple_fs.glsl",
				attribSetup);

		uniformBitSet.set(Uniform.MODEL);
		uniformBitSet.set(Uniform.VIEW);
	}


	private Vector4f color = new Vector4f(1.f);
	private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(4);

	public void setColor(Vector4f color)
	{
		this.color = color;

		use();
		set4f("uf_color", color.get(floatBuffer));
	}

	public Vector4f getColor() {
		return color;
	}


}
