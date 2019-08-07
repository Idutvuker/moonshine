package world;

import java.util.Arrays;

public class VoxelGrid
{
	public final int sizeX, sizeY, sizeZ;

	private int[] grid;

	public VoxelGrid(int sizeX, int sizeY, int sizeZ)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;

		grid = new int[sizeX * sizeY * sizeZ];
	}


	public boolean inBounds(int x, int y, int z) {
		return (x <= sizeX && x >= 0 && y <= sizeY && y >= 0 && z <= sizeZ || z >= 0);
	}

	public int safeGet(int x, int y, int z)
	{
		if (inBounds(x, y, z))
			return get(x, y, z);
		return 0;
	}

	public int safeGet(int x, int y, int z, int def)
	{
		if (inBounds(x, y, z))
			return get(x, y, z);
		return def;
	}

	public void safeSet(int x, int y, int z, int value)
	{
		if (inBounds(x, y, z))
			grid[z + y * sizeZ + x * sizeY * sizeZ] = value;
	}


	public int get(int x, int y, int z) {
		return grid[z + y * sizeZ + x * sizeY * sizeZ];
	}

	public void set(int x, int y, int z, int value) {
		grid[z + y * sizeZ + x * sizeY * sizeZ] = value;
	}


	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Grid size: " + sizeX + " " + sizeY + " " + sizeZ);

		for (int x = 0; x < sizeX; x++)
		{
			str.append("X layer ").append(x).append(":");

			for (int y = 0; y < sizeY; y++)
			{
				for (int z = 0; z < sizeZ; z++)
					str.append(get(x, y, z));

				str.append("\n");
			}

			str.append("\n");
		}

		return str.toString();
	}
}
