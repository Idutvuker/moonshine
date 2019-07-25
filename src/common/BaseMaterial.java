package common;

import org.joml.Vector3f;
import org.joml.sampling.UniformSampling;

import java.nio.FloatBuffer;
import java.util.BitSet;

import static org.lwjgl.opengl.GL33.*;

public class BaseMaterial
{
	public class Uniform
	{
		public static final int MODELVIEWPROJECTION = 	0b1;
		public static final int MODEL = 				0b10;
		public static final int VIEW = 					0b100;
		public static final int PROJECTION = 			0b1000;
		public static final int MODELVIEW = 			0b10000;
		public static final int SMOOTHSHADING = 		0b100000;
		public static final int COLOR = 				0b1000000;
	}

	protected BitSet uniformBitSet = new BitSet(32);

	public boolean usesUniform(int uniformBit)
	{
		return uniformBitSet.get(uniformBit);
	}

	private ShaderProgram shaderProgram;
	private Texture texture;
	VertexAttribSetup vertexAttribSetup;

	public void createShaderProgram(String vertexShaderFilename, String fragmentShaderFilename)
	{
		Shader vs = new Shader(vertexShaderFilename, GL_VERTEX_SHADER);
		Shader fs = new Shader(fragmentShaderFilename, GL_FRAGMENT_SHADER);

		shaderProgram = new ShaderProgram(vs, fs);

		vs.delete();
		fs.delete();
	}

	public BaseMaterial(String vertexShaderFilename,
						String fragmentShaderFilename,
						VertexAttribSetup vertexAttribSetup)
	{
		this.vertexAttribSetup = vertexAttribSetup;
		uniformBitSet.set(Uniform.MODELVIEWPROJECTION);

		createShaderProgram(vertexShaderFilename, fragmentShaderFilename);
	}

	public BaseMaterial(String vertexShaderFilename,
						String fragmentShaderFilename,
						VertexAttribSetup vertexAttribSetup,
						BitSet uniforms)
	{
		this.vertexAttribSetup = vertexAttribSetup;
		uniformBitSet = uniforms;

		createShaderProgram(vertexShaderFilename, fragmentShaderFilename);
	}

	public void setTexture(String textureFilename)
	{
		texture = new Texture(textureFilename);
	}

	public void use()
	{
		if (texture != null)
			texture.bind();

		shaderProgram.use();
	}





	public void set1f(CharSequence name, float value) {
		glUniform1f(glGetUniformLocation(shaderProgram.getID(), name), value);
	}

	public void set1i(CharSequence name, int value) {
		glUniform1i(glGetUniformLocation(shaderProgram.getID(), name), value);
	}


	public void set2f(CharSequence name, FloatBuffer value) {
		glUniform2fv(glGetUniformLocation(shaderProgram.getID(), name), value);
	}
	public void set3f(CharSequence name, FloatBuffer value) {
		glUniform3fv(glGetUniformLocation(shaderProgram.getID(), name), value);
	}

	public void set4f(CharSequence name, FloatBuffer value) {
		glUniform4fv(glGetUniformLocation(shaderProgram.getID(), name), value);
	}


	public void setMatrix3f(CharSequence name, FloatBuffer value) {
		glUniformMatrix3fv(glGetUniformLocation(shaderProgram.getID(), name), false, value);
	}

	public void setMatrix4f(CharSequence name, FloatBuffer value) {
		glUniformMatrix4fv(glGetUniformLocation(shaderProgram.getID(), name), false, value);
	}


	public void setMVP(FloatBuffer MVPBuffer)
	{
		int MVPLoc = glGetUniformLocation(shaderProgram.getID(), "uf_MVPMat");

		glUniformMatrix4fv(MVPLoc, false, MVPBuffer);
	}
}
