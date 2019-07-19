package common;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

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

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		//material.use();

		//Vertex position
		glVertexAttribPointer(0, verticesCount, GL_FLOAT, false, 20, 0);
		glEnableVertexAttribArray(0);

		//Vertex texture coords
		glVertexAttribPointer(1, verticesCount, GL_FLOAT, false, 20, 12);
		glEnableVertexAttribArray(1);


		//Unbinding
		glBindVertexArray(0);



	}


	public void draw(FloatBuffer MVPbuffer)
	{
		//float[] arr = new float[16];
		//MVPbuffer.get(arr);
		//MVPbuffer.flip();

		//System.out.println(Arrays.toString(arr));
		//System.out.println(MVPmat.toString());

		material.use();
		material.setMVP(MVPbuffer);

		glBindVertexArray(vaoID);
		glDrawElements(GL_TRIANGLES, verticesCount, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}

	public int getVaoID() {
		return vaoID;
	}
}
