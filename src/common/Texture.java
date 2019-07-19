package common;

import org.lwjgl.stb.STBImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture
{
	private int ID;
	private int width;
	private int height;

	public Texture(String filename)
	{
		int[] w = new int[1]; //width
		int[] h = new int[1]; //height
		int[] c = new int[1]; //channels

		ByteBuffer image = STBImage.stbi_load(filename, w, h, c, 4);

		if (image == null)
		{
			System.err.println("Could not load image " + filename);
			System.err.println(STBImage.stbi_failure_reason());
		}

		width = w[0];
		height = h[0];

		ID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, ID);

		glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, ID);
	}

	public int getID() {
		return ID;
	}
}
