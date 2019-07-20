package util;


import common.Material;
import common.Mesh;
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

		//TODO replace arrays with Buffers
		float[] vertices = processVertices(aiMesh);
		float[] normals;
		int[] indices = processIndices(aiMesh);

		System.out.println(Arrays.toString(vertices));
		System.out.println(Arrays.toString(indices));

		Material mat1 = new Material(
				"res/shaders/vs_basic.glsl",
				"res/shaders/fs_basic.glsl");

		return new Mesh(aiMesh.mNumVertices(), vertices, indices, mat1);
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

	private static float[] processVertices(AIMesh aiMesh)
	{
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();

		float[] vertices = new float[aiVertices.limit() * 5];


		for (int i = 0; i < aiVertices.limit(); i++)
		{
			AIVector3D aiVertex = aiVertices.get();
			vertices[i * 5] = aiVertex.x();
			vertices[i * 5 + 1] = aiVertex.y();
			vertices[i * 5 + 2] = aiVertex.z();
			vertices[i * 5 + 3] = 0;
			vertices[i * 5 + 4] = 0;
		}

		return vertices;
	}
}
