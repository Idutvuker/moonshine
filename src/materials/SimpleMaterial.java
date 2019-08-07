package materials;

import common.BaseMaterial;
import common.Uniform;
import common.VertexAttribSetup;
import common.VertexDataType;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class SimpleMaterial extends BaseMaterial
{
	public class Flags
	{
		public static final int TEXTURED = 0b1;
		public static final int SMOOTH = 0b10;
	}



	public SimpleMaterial() {
		this(0);
	}

	public SimpleMaterial(int flags)
	{
		String[] defines = new String[Integer.bitCount(flags)];

		int t = 0;

		if ((flags & Flags.TEXTURED) != 0)
			defines[t++] = "TEXTURED";

		if ((flags & Flags.SMOOTH) != 0)
			defines[t++] = "SMOOTH_SHADING";


		createShaderProgram(
				"res/shaders/simple.vert",
				"res/shaders/simple.frag",
				defines);

		vertexAttribData = new VertexDataType[] {
				VertexDataType.POSITION,
				VertexDataType.NORMAL,
				VertexDataType.TEXCOORD
		};

		vertexAttribSetup = new VertexAttribSetup(vertexAttribData);

		uniformBitSet.set(Uniform.MODEL);
		uniformBitSet.set(Uniform.MODEL_VIEW_PROJECTION);
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
