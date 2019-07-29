package util;


import common.BaseMaterial;
import common.Mesh;
import common.VertexDataType;
import javafx.scene.paint.Material;
import materials.SimpleMaterial;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.assimp.Assimp.AI_SCENE_FLAGS_NON_VERBOSE_FORMAT;
import static org.lwjgl.assimp.Assimp.aiImportFile;

public class ModelLoader
{
	private static class MeshVertex
	{
		public float posX;
		public float posY;
		public float posZ;


		public float normalX;
		public float normalY;
		public float normalZ;


		public float texCoordX;
		public float texCoordY;
	}

	public static Mesh[] load(String resourcePath) {
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

		BaseMaterial mat1 = new SimpleMaterial();
		VertexDataType[] attribs = SimpleMaterial.attribs;

		MeshVertex[] vertices = new MeshVertex[aiMesh.mNumVertices()];
		for (int i = 0; i < aiMesh.mNumVertices(); i++)
			vertices[i] = new MeshVertex();

		for (VertexDataType vdt: attribs)
		{
			if (vdt == VertexDataType.POSITION)
				processVertices(aiMesh, vertices);

			else if (vdt == VertexDataType.NORMAL)
				processNormals(aiMesh, vertices);

			else if (vdt == VertexDataType.TEXCOORD)
				processTexCoords(aiMesh, vertices);
		}


		IntBuffer indices = BufferUtils.createIntBuffer(aiMesh.mNumFaces() * 3);
		processIndices(aiMesh, indices, vertices, aiMesh.mNormals() == null);
		indices.flip();


		int size = SimpleMaterial.attribSetup.getLayoutSize() * aiMesh.mNumVertices();
		FloatBuffer verticesData = BufferUtils.createFloatBuffer(size);


		for (MeshVertex vertex: vertices)
		{
			for (VertexDataType vdt: attribs)
			{
				if (vdt == VertexDataType.POSITION)
				{
					verticesData.put(vertex.posX);
					verticesData.put(vertex.posY);
					verticesData.put(vertex.posZ);
				}

				else if (vdt == VertexDataType.NORMAL)
				{
					verticesData.put(vertex.normalX);
					verticesData.put(vertex.normalY);
					verticesData.put(vertex.normalZ);
				}

				else if (vdt == VertexDataType.TEXCOORD)
				{
					verticesData.put(vertex.texCoordX);
					verticesData.put(vertex.texCoordY);
				}
			}
		}

		verticesData.flip();

		return new Mesh(aiMesh.mNumVertices(), verticesData, indices, mat1);
	}

	private static Vector3f vec1 = new Vector3f();
	private static Vector3f vec2 = new Vector3f();

	private static void calculateNormal(MeshVertex v1, MeshVertex v2, MeshVertex v3)
	{
		vec1.set(v2.posX - v1.posX, v2.posY - v1.posY, v2.posZ - v1.posZ);
		vec2.set(v3.posX - v1.posX, v3.posY - v1.posY, v3.posZ - v1.posZ);

		vec1.cross(vec2).normalize();
		System.out.println(vec1);

		v1.normalX = vec1.x; v1.normalY = vec1.y; v1.normalZ = vec1.z;
		v2.normalX = vec1.x; v2.normalY = vec1.y; v2.normalZ = vec1.z;
		v3.normalX = vec1.x; v3.normalY = vec1.y; v3.normalZ = vec1.z;
	}


	private static void processIndices(AIMesh aiMesh, IntBuffer indices,
									   MeshVertex[] vertices, boolean calcNormals)
	{
		AIFace.Buffer aiFaces = aiMesh.mFaces();

		for (int i = 0; i < aiFaces.limit(); i++)
		{
			//Faces should be only triangles
			IntBuffer indBuff = aiFaces.get().mIndices();

			if (indBuff.limit() != 3)
				System.err.println("Face is not a triangle! " + aiMesh.toString());

			int v1_ind = indBuff.get();
			int v2_ind = indBuff.get();
			int v3_ind = indBuff.get();

			indices.put(v1_ind);
			indices.put(v2_ind);
			indices.put(v3_ind);

			if (calcNormals)
				calculateNormal(vertices[v1_ind], vertices[v2_ind], vertices[v3_ind]);
		}
	}


	private static boolean processVertices(AIMesh aiMesh, MeshVertex[] vertices)
	{
		AIVector3D.Buffer buffer = aiMesh.mVertices();

		for (int i = 0; i < buffer.limit(); i++)
		{
			AIVector3D aiVertex = buffer.get();
			vertices[i].posX = aiVertex.x();
			vertices[i].posY = aiVertex.y();
			vertices[i].posZ = aiVertex.z();
		}

		return true;
	}

	private static boolean processNormals(AIMesh aiMesh, MeshVertex[] vertices)
	{
		AIVector3D.Buffer buffer = aiMesh.mNormals();

		if (buffer == null)
			return false;

		for (int i = 0; i < buffer.limit(); i++)
		{
			AIVector3D aiVertex = buffer.get();
			vertices[i].normalX = aiVertex.x();
			vertices[i].normalY = aiVertex.y();
			vertices[i].normalZ = aiVertex.z();
		}

		return true;
	}

	private static boolean processTexCoords(AIMesh aiMesh, MeshVertex[] vertices)
	{
		AIVector3D.Buffer buffer = aiMesh.mTextureCoords(0);

		if (buffer == null)
			return false;

		for (int i = 0; i < buffer.limit(); i++)
		{
			AIVector3D aiVertex = buffer.get();

			vertices[i].texCoordX = aiVertex.x();
			vertices[i].texCoordY = aiVertex.y();
		}

		return true;
	}
}
