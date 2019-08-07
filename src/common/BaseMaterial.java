package common;

import com.sun.istack.internal.Nullable;

import java.nio.FloatBuffer;
import java.util.BitSet;

import static org.lwjgl.opengl.GL33.*;

public class BaseMaterial
{
	protected BitSet uniformBitSet = new BitSet(32);

	public boolean usesUniform(int uniformBit)
	{
		return uniformBitSet.get(uniformBit);
	}

	private ShaderProgram shaderProgram;
	private Texture texture;

	protected VertexDataType[] vertexAttribData;
	protected VertexAttribSetup vertexAttribSetup;

	protected void createShaderProgram(String vertexShaderFilename, String fragmentShaderFilename, CharSequence[] defines)
	{
		Shader vs = new Shader(vertexShaderFilename, GL_VERTEX_SHADER, defines);
		Shader fs = new Shader(fragmentShaderFilename, GL_FRAGMENT_SHADER, defines);

		shaderProgram = new ShaderProgram(vs, fs);

		vs.delete();
		fs.delete();
	}

	protected BaseMaterial() {}

	public BaseMaterial(BaseMaterial mat)
	{
		uniformBitSet = mat.uniformBitSet;
		shaderProgram = mat.shaderProgram;
		vertexAttribSetup = mat.vertexAttribSetup;
		vertexAttribData = mat.vertexAttribData;
	}

	public BaseMaterial(String vertexShaderFilename,
						String fragmentShaderFilename,
						VertexAttribSetup vertexAttribSetup)
	{
		this.vertexAttribSetup = vertexAttribSetup;
		uniformBitSet.set(Uniform.MODEL_VIEW_PROJECTION);

		createShaderProgram(vertexShaderFilename, fragmentShaderFilename, null);
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


	public VertexAttribSetup getVertexAttribSetup() {
		return vertexAttribSetup;
	}


	public VertexDataType[] getVertexAttribData() {
		return vertexAttribData;
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
