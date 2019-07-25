package util;


import common.BaseMaterial;
import common.Mesh;
import common.VertexAttribSetup;
import common.VertexDataType;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.BitSet;

import static org.lwjgl.assimp.Assimp.*;

public class ModelLoader {
	public static Mesh[] load(String resourcePath) {
		AIScene aiScene = aiImportFile(resourcePath, AI_SCENE_FLAGS_NON_VERBOSE_FORMAT);

		if (aiScene == null) {
			System.err.println("Error loading model " + resourcePath);
			return null;
		}

		int numMeshes = aiScene.mNumMeshes();

		PointerBuffer aiMeshes = aiScene.mMeshes();

		Mesh[] meshes = new Mesh[numMeshes];

		for (int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get());
			meshes[i] = processMesh(aiMesh);
		}

		return meshes;
	}

	private static Mesh processMesh(AIMesh aiMesh) {
		System.out.println("NumVertices: " + aiMesh.mNumVertices());
		System.out.println("NumFaces: " + aiMesh.mNumFaces());

		VertexDataType[] vdt = new VertexDataType[]{
				VertexDataType.POSITION, VertexDataType.NORMAL,
		};

		VertexAttribSetup vas = new VertexAttribSetup(vdt);

		//int stride = vas.getSize();
		//int offset = 0;

		//float[] verticesData = new float[stride * aiMesh.mNumVertices()];

		AIVector3D.Buffer[] buffers = new AIVector3D.Buffer[vdt.length];
		for (int i = 0; i < vdt.length; i++) {
			if (vdt[i] == VertexDataType.POSITION)
				buffers[i] = aiMesh.mVertices();

			else if (vdt[i] == VertexDataType.NORMAL)
				buffers[i] = aiMesh.mNormals();

			else if (vdt[i] == VertexDataType.TEXCOORD)
				buffers[i] = aiMesh.mTextureCoords(0);
		}

		FloatBuffer verticesData = BufferUtils.createFloatBuffer(vas.getSize() * aiMesh.mNumVertices());
		for (int i = 0; i < aiMesh.mNumVertices(); i++)
		{
			for (int j = 0; j < vdt.length; j++) {
				if (vdt[j] == VertexDataType.POSITION)
					processVertex(buffers[j], verticesData);

				else if (vdt[j] == VertexDataType.NORMAL)
					processNormal(buffers[j], verticesData);

				else if (vdt[j] == VertexDataType.TEXCOORD)
					processTexCoord(buffers[j], verticesData);
			}
		}
		verticesData.flip();


		IntBuffer indices = BufferUtils.createIntBuffer(aiMesh.mNumFaces() * 3);
		processIndices(aiMesh, indices);
		indices.flip();

		BitSet bs = new BitSet(32);
		bs.set(BaseMaterial.Uniform.MODELVIEWPROJECTION);
		bs.set(BaseMaterial.Uniform.MODEL);
		BaseMaterial mat1 = new BaseMaterial(
				"res/shaders/normals_vs.glsl",
				"res/shaders/normals_fs.glsl",
				vas,
				bs);

		return new Mesh(aiMesh.mNumVertices(), verticesData, indices, mat1);
	}

	private static void processIndices(AIMesh aiMesh, IntBuffer indices) {
		AIFace.Buffer aiFaces = aiMesh.mFaces();


		for (int i = 0; i < aiFaces.limit(); i++)
		{
			//Faces should be only triangles
			IntBuffer indBuff = aiFaces.get().mIndices();

			if (indBuff.limit() != 3)
				System.err.println("Face is not a triangle!" + aiMesh.toString());

			indices.put(indBuff.get(0));
			indices.put(indBuff.get(1));
			indices.put(indBuff.get(2));
		}
	}


	private static void processVertex(AIVector3D.Buffer buffer, FloatBuffer data)
	{
		AIVector3D aiVertex = buffer.get();
		data.put(aiVertex.x());
		data.put(aiVertex.y());
		data.put(aiVertex.z());
	}

	private static void processNormal(AIVector3D.Buffer buffer, FloatBuffer data)
	{
		AIVector3D aiVertex = buffer.get();
		data.put(aiVertex.x());
		data.put(aiVertex.y());
		data.put(aiVertex.z());
	}

	private static void processTexCoord(AIVector3D.Buffer buffer, FloatBuffer data)
	{
		AIVector3D aiVertex = buffer.get();
		data.put(aiVertex.x());
		data.put(aiVertex.y());
	}


	private static boolean processVertices(AIMesh aiMesh, float[] data, int stride, int offset)
	{
		AIVector3D.Buffer buffer = aiMesh.mVertices();

		if (buffer == null)
		{
			//System.err.println("Couldn't load vertices on " + aiMesh);
			return false;
		}

		for (int i = 0; i < buffer.limit(); i++)
		{
			AIVector3D aiVertex = buffer.get();
			data[i * stride + offset] = aiVertex.x();
			data[i * stride + offset + 1] = aiVertex.y();
			data[i * stride + offset + 2] = aiVertex.z();
		}

		return true;
	}

	private static boolean processNormals(AIMesh aiMesh, float[] data, int stride, int offset)
	{
		AIVector3D.Buffer buffer = aiMesh.mNormals();

		if (buffer == null)
		{
			//System.err.println("Couldn't load normals coords on " + aiMesh);
			return false;
		}

		for (int i = 0; i < buffer.limit(); i++)
		{
			AIVector3D aiVertex = buffer.get();
			data[i * stride + offset] = aiVertex.x();
			data[i * stride + offset + 1] = aiVertex.y();
			data[i * stride + offset + 2] = aiVertex.z();
		}

		return true;
	}

	private static boolean processTexCoords(AIMesh aiMesh, float[] data, int stride, int offset)
	{
		AIVector3D.Buffer buffer = aiMesh.mTextureCoords(0);

		if (buffer == null)
		{
			//System.err.println("Couldn't load texture coords on " + aiMesh);
			return false;
		}

		for (int i = 0; i < buffer.limit(); i++)
		{
			AIVector3D aiVertex = buffer.get();

			//Only 2D texture coords are used
			data[i * stride + offset] = aiVertex.x();
			data[i * stride + offset + 1] = aiVertex.y();
		}

		return true;
	}
}
