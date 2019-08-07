package system;

import common.BaseMaterial;
import common.Mesh;
import common.VertexAttribSetup;
import common.VertexDataType;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;
import world.VoxelGrid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static util.MCTables.*;

public class GridMesh extends Mesh
{
	private class Vertex
	{
		Vertex() {}

		Vector3f pos = new Vector3f();
		Vector3f normal = new Vector3f();


		int normalLength;
	}

	private VoxelGrid grid;

	public GridMesh(VoxelGrid grid)
	{
		this.grid = grid;

		VertexDataType[] types = new VertexDataType[] {VertexDataType.POSITION, VertexDataType.NORMAL};
		VertexAttribSetup vas = new VertexAttribSetup(types);

		material = new BaseMaterial(
				"res/shaders/basic.vert",
				"res/shaders/basic.frag",
				vas);

		MarchingCubes(0);

		initGLObject();
	}




	private void vertInterp(int isolevel, Vector3f p1, Vector3f p2, int val1, int val2, Vector3f dest)
	{
		float mu = (isolevel - val1) / ((float) (val2 - val1));

		p1.lerp(p2, mu, dest);
	}



	private int[] values = new int[8];
	private int[] vertList = new int[12];

	private void f(int x, int y, int z, int isolevel, ArrayList<Vertex> vertices, List<Integer> indices)
	{
		int bits = 0;
		int power = 1;

		for (int i = 0; i < 8; i++) {
			Vector3i d = VOXEL_POSITION[i];
			values[i] = grid.get(x + d.x, y + d.y, z + d.z);

			if (values[i] < isolevel)
				bits |= power;

			power <<= 1;
		}


		int edges = MC_EDGE_TABLE[bits];

		power = 1;
		Vector3f pos = new Vector3f(x, y, z);

		for (int i = 0; i < 12; i++) {
			if ((edges & power) != 0)
			{
				int v1 = EDGE_ORDER[i * 2];
				int v2 = EDGE_ORDER[i * 2 + 1];


				// -z front dir (0)
				// x right dir  (1)
				// y top dir    (2)
				//System.out.printf("x=%d y=%d z=%d\n", x, y, z);

				if (
						!EDGE_CACHED[i]  ||
						(x == 0 && (i == 3 || i == 8 || i == 7 || i == 11)) ||	// left cube face
						(y == 0 && (i == 0 || i == 1 || i == 2 || i == 3)) ||	// bottom cube face
						(z == 0 && (i == 0 || i == 9 || i == 4 || i == 8))		// front cube face
				)

				{
					//System.out.println("Edge is not cached: " + i);



					Vertex vert = new Vertex();

					int index = vertices.size(); // index of created vertex
					//System.out.println("Created vertex with index: " + index + "\n\n");

					Vector3i p1 = VOXEL_POSITION[v1];
					vertCache[(x + p1.x) % 2][y + p1.y][z + p1.z][EDGE_DIR[i]] = index;
					vertList[i] = index;

					vertices.add(vert);

					vertInterp(isolevel,
							pos.add(VOXEL_POSITION_F[v1], new Vector3f()),
							pos.add(VOXEL_POSITION_F[v2], new Vector3f()),
							values[v1], values[v2], vert.pos);

				}

				else
				{
					Vector3i p1 = VOXEL_POSITION[v1];
					vertList[i] = vertCache[(x + p1.x) % 2][y + p1.y][z + p1.z][EDGE_DIR[i]];


					//System.out.println("Edge is cached: " + vertList[i] + "\n\n");
				}
			}

			power <<= 1;
		}

		int i = 0;
		bits *= 16;

		while (MC_TRI_TABLE[bits + i] != -1)
		{
			int ind1 = MC_TRI_TABLE[bits + i];
			int ind2 = MC_TRI_TABLE[bits + i + 1];
			int ind3 = MC_TRI_TABLE[bits + i + 2];


			indices.add(vertList[ind1]);
			indices.add(vertList[ind2]);
			indices.add(vertList[ind3]);

			i += 3;
		}
	}


	private int[][][][] vertCache;

	private void MarchingCubes(int isolevel)
	{
		vertCache = new int[2][grid.sizeY][grid.sizeZ][3];
		// 2 YZ layers of grid nodes
		// Each node of a grid holds 3 indices of vertices


		List<Integer> indices = new LinkedList<>();

		ArrayList<Vertex> vertices = new ArrayList<>(100);

		float scale = 0.3f;

		for (int x = 0; x < grid.sizeX - 1; x++)
			for (int y = 0; y < grid.sizeY - 1; y++)
				for (int z = 0; z < grid.sizeZ - 1; z++)
					f(x, y, z, isolevel, vertices, indices);


		indicesBuffer = BufferUtils.createIntBuffer(indices.size());
		Iterator<Integer> iter = indices.iterator();

		while(iter.hasNext())
		{
			int i1 = iter.next();
			int i2 = iter.next();
			int i3 = iter.next();
			Vertex v1 = vertices.get(i1);
			Vertex v2 = vertices.get(i2);
			Vertex v3 = vertices.get(i3);

			Vector3f normal = calculateNormal(v1.pos, v2.pos, v3.pos);

			v1.normal.add(normal);
			v2.normal.add(normal);
			v3.normal.add(normal);

			v1.normalLength += 1;
			v2.normalLength += 1;
			v3.normalLength += 1;


			indicesBuffer.put(i1);
			indicesBuffer.put(i2);
			indicesBuffer.put(i3);
		}

		indicesBuffer.flip();


		verticesBuffer = BufferUtils.createFloatBuffer(vertices.size() * 6);
		for (Vertex vert: vertices)
		{
			vert.normal.div(vert.normalLength);
			vert.normalLength = 1;

			verticesBuffer.put(vert.pos.x);
			verticesBuffer.put(vert.pos.y);
			verticesBuffer.put(vert.pos.z);

			verticesBuffer.put(vert.normal.x);
			verticesBuffer.put(vert.normal.y);
			verticesBuffer.put(vert.normal.z);
		}
		verticesBuffer.flip();
	}

	private static Vector3f vec1 = new Vector3f();
	private static Vector3f vec2 = new Vector3f();

	private static Vector3f calculateNormal(Vector3f p1, Vector3f p2, Vector3f p3)
	{
		p2.sub(p1, vec1);
		p3.sub(p1, vec2);

		return vec1.cross(vec2).normalize(new Vector3f());
	}
}
