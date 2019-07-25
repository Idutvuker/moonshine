package common;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Mesh extends Spatial
{
	private FloatBuffer verticesBuffer;
	private IntBuffer indicesBuffer;
	private BaseMaterial material;

	private GLObject glObject;

	private void initGLObject(int verticesCount)
	{
		glObject = new GLObject();
		glObject.verticesCount = verticesCount;

		glObject.vboID = glGenBuffers();
		glObject.iboID = glGenBuffers();
		glObject.vaoID = glGenVertexArrays();


		glBindVertexArray(glObject.vaoID);

		glBindBuffer(GL_ARRAY_BUFFER, glObject.vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glObject.iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		//material.use();

		VertexAttribSetup.Attrib[] setup = material.vertexAttribSetup.getSetup();

		for (int i = 0; i < setup.length; i++)
		{
			VertexAttribSetup.Attrib attr = setup[i];

			if (attr.skip)
				continue;

			glVertexAttribPointer(i, attr.size, attr.glType, false, attr.strideBytes, attr.offsetBytes);
			glEnableVertexAttribArray(i);
		}

		//Unbinding
		glBindVertexArray(0);
	}

	public Mesh(int verticesCount, float[] verticesData, int[] indices, BaseMaterial material)
	{
		verticesBuffer = BufferUtils.createFloatBuffer(verticesData.length);
		verticesBuffer.put(verticesData);
		verticesBuffer.flip();

		indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		this.material = material;

		initGLObject(verticesCount);
	}


	public Mesh(int verticesCount, FloatBuffer verticesData, IntBuffer indices, BaseMaterial material)
	{
		verticesBuffer = verticesData;
		indicesBuffer = indices;

		this.material = material;

		initGLObject(verticesCount);
	}


	public void draw()
	{
		glBindVertexArray(glObject.vaoID);
		glDrawElements(GL_TRIANGLES, glObject.verticesCount, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}

	public BaseMaterial getMaterial(){
		return material;
	}
}
