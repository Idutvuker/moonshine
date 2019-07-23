package common;

import org.lwjgl.BufferUtils;
import util.GLTypeSizes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Mesh extends Spatial
{
	private int verticesCount;
	private FloatBuffer verticesBuffer;
	private IntBuffer indicesBuffer;
	private Material material;

	private int vaoID;
	private int vboID;
	private int iboID;

	public Mesh(int verticesCount, float[] verticesData, int[] indices, Material material)
	{
		this.verticesCount = verticesCount;
		this.material = material;

		verticesBuffer = BufferUtils.createFloatBuffer(verticesData.length);
		verticesBuffer.put(verticesData);
		verticesBuffer.flip();

		indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		vboID = glGenBuffers();
		iboID = glGenBuffers();
		vaoID = glGenVertexArrays();

		glBindVertexArray(vaoID);

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
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

		//Vertex texture coords
		//glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 12);
		//glEnableVertexAttribArray(1);


		//Unbinding
		glBindVertexArray(0);

	}


	public void draw(FloatBuffer MVPBuffer)
	{

		//float[] arr = new float[16];
		//MVPbuffer.get(arr);
		//MVPbuffer.flip();

		//System.out.println(Arrays.toString(arr));
		//System.out.println(MVPmat.toString());

		material.use();
		material.setMVP(MVPBuffer);

		glBindVertexArray(vaoID);
		//System.out.println("Drawing V: " + verticesCount);

		glDrawElements(GL_TRIANGLES, verticesCount, GL_UNSIGNED_INT, 0);
		//System.out.println("Finished!");
		glBindVertexArray(0);


	}

	public int getVaoID() {
		return vaoID;
	}
}
