package world;

import common.Node;
import org.joml.Vector3f;
import system.IsoMesh;

public class Landscape
{
	private VoxelGrid voxelGrid;
	private IsoMesh isoMesh;

	private static int sqr(int x)
	{
		return x*x;
	}
	private static int func(int x, int y, int z)
	{
		return sqr(x - 20) + sqr(y - 20) + sqr(z - 20);
	}

	public Landscape(Node root)
	{
		voxelGrid = new VoxelGrid(100, 100, 100);

		/*voxelGrid.set(1, 1, 1, -200);
		voxelGrid.set(2, 2, 1, -200);
		voxelGrid.set(1, 2, 1, -200);
		voxelGrid.set(2, 1, 1, -200);
		*/
		//voxelGrid.set(20, 20, 20, -300);
		//voxelGrid.set(20, 20, 20, -400);

		for (int x = 0; x < voxelGrid.sizeX; x++)
			for (int y = 0; y < voxelGrid.sizeY; y++)
				for (int z = 0; z < voxelGrid.sizeZ; z++)
					voxelGrid.set(x, y, z, func(x, y, z));


		isoMesh = new IsoMesh(voxelGrid, 10.5f, 0.5f);
		isoMesh.setPosition(new Vector3f(-10, -10, -10));
		root.addChild(isoMesh);
	}

}
