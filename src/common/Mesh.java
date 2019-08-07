package common;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Mesh extends Spatial
{
	protected FloatBuffer verticesBuffer;
	protected IntBuffer indicesBuffer;
	protected BaseMaterial material;

	protected GLObject glObject = new GLObject();

	protected void initGLObject()
	{
		glObject.verticesCount = indicesBuffer.limit();

		glObject.vboID = glGenBuffers();
		glObject.iboID = glGenBuffers();
		glObject.vaoID = glGenVertexArrays();


		glBindVertexArray(glObject.vaoID);

		glBindBuffer(GL_ARRAY_BUFFER, glObject.vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glObject.iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		//material.use();

		VertexAttribSetup setup = material.getVertexAttribSetup();

		for (int i = 0; i < setup.getSize(); i++)
		{
			VertexAttribSetup.LockedAttrib attr = setup.getAttr(i);

			//if (attr.skip)
			//	continue;

			glVertexAttribPointer(i, attr.size, attr.glType, false, attr.strideBytes, attr.offsetBytes);
			glEnableVertexAttribArray(i);
		}

		//Unbinding
		glBindVertexArray(0);
	}

	public Mesh(float[] verticesData, int[] indices, BaseMaterial material)
	{
		verticesBuffer = BufferUtils.createFloatBuffer(verticesData.length);
		verticesBuffer.put(verticesData);
		verticesBuffer.flip();

		indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		this.material = material;

		initGLObject();
	}

	protected Mesh() {}

	public Mesh(FloatBuffer verticesData, IntBuffer indices, BaseMaterial material)
	{
		verticesBuffer = verticesData;
		indicesBuffer = indices;

		this.material = material;

		initGLObject();
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
