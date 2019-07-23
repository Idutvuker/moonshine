package util;


import common.Material;
import common.Mesh;
import common.VertexAttribSetup;
import common.VertexDataType;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.assimp.Assimp.*;

public class ModelLoader
{
	public static Mesh[] load(String resourcePath)
	{
		AIScene aiScene = aiImportFile(resourcePath, AI_SCENE_FLAGS_NON_VERBOSE_FORMAT);

		if (aiScene == null) {
			System.err.println("Error loading model " + resourcePath);
			return null;
		}

		int numMeshes = aiScene.mNumMeshes();

		PointerBuffer aiMeshes = aiScene.mMeshes();

		Mesh[] meshes = new Mesh[numMeshes];

		for (int i = 0; i < numMeshes; i++)
		{
			AIMesh aiMesh = AIMesh.create(aiMeshes.get());
			meshes[i] = processMesh(aiMesh);
		}

		return meshes;
	}

	private static Mesh processMesh(AIMesh aiMesh)
	{
		System.out.println("NumVertices: " + aiMesh.mNumVertices());
		System.out.println("NumFaces: " + aiMesh.mNumFaces());

		VertexDataType[] vdt  = new VertexDataType[] {
				VertexDataType.POSITION, VertexDataType.NORMAL,
		};

		VertexAttribSetup vas = new VertexAttribSetup(vdt);

		int stride = vas.getSize();
		int offset = 0;

		float[] verticesData = new float[stride * aiMesh.mNumVertices()];

		for (int i = 0; i < vdt.length; i++)
		{
			if (vdt[i] == VertexDataType.POSITION)
				processVertices(aiMesh, verticesData, stride, offset);

			else if (vdt[i] == VertexDataType.NORMAL)
				processNormals(aiMesh, verticesData, stride, offset);

			else if (vdt[i] == VertexDataType.TEXCOORD)
				processTexCoords(aiMesh, verticesData, stride, offset);

			offset += vdt[i].size;
		}


		int[] indices = processIndices(aiMesh);

		//System.out.println(Arrays.toString(vertices));
		//System.out.println(Arrays.toString(indices));

		Material mat1 = new Material(
				"res/shaders/vs_normals.glsl",
				"res/shaders/fs_normals.glsl",
				vas);

		return new Mesh(aiMesh.mNumVertices(), verticesData, indices, mat1);
	}

	private static int[] processIndices(AIMesh aiMesh) {
		AIFace.Buffer aiFaces = aiMesh.mFaces();

		int[] indices = new int[aiFaces.limit() * 3];


		for (int i = 0; i < aiFaces.limit(); i++)
		{
			//Faces should be only triangles
			IntBuffer indBuff = aiFaces.get().mIndices();

			indices[i * 3] = indBuff.get(0);
			indices[i * 3 + 1] = indBuff.get(1);
			indices[i * 3 + 2] = indBuff.get(2);
		}

		return indices;
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
			//System.err.println("Couldn't normals coords on " + aiMesh);
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
