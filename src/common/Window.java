package common;

import org.joml.Vector2d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import system.KeyCallback;
import system.MousePosCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{

	private long windowHandle;

	private int width;
	private int height;

	private KeyCallback keyCallback;
	private MousePosCallback mouseCallback;

	public KeyCallback getKeyCallback() { return keyCallback; }
	public MousePosCallback getMouseCallback() { return mouseCallback; }





	public Window(int width, int height, String title)
	{
		this.width = width;
		this.height = height;

		glfwDefaultWindowHints();

		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

		if (windowHandle == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(windowHandle, keyCallback = new KeyCallback());
		glfwSetCursorPosCallback(windowHandle, mouseCallback = new MousePosCallback());

		int[] pWidth = new int[1];
		int[] pHeight = new int[1];

		glfwGetWindowSize(windowHandle, pWidth, pHeight);

		/*GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(
				windowHandle,
				(vidmode.width() - pWidth[0]) / 2,
				(vidmode.height() - pHeight[0]) / 2
		);*/

		glfwMakeContextCurrent(windowHandle);
		glfwSwapInterval(1);
		glfwShowWindow(windowHandle);

		GL.createCapabilities();


	}

	public long getHandle() { return windowHandle; }

	public int getWidth() { return width; }

	public int getHeight() { return height; }

	public void destroy()
	{
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(windowHandle);
	}

	public void swapBuffers() {
		glfwSwapBuffers(windowHandle);
	}

	public void setInputMode(int mode, int value) {
		glfwSetInputMode(windowHandle, mode, value);
	}


	private DoubleBuffer xposBuff = BufferUtils.createDoubleBuffer(1);
	private DoubleBuffer yposBuff = BufferUtils.createDoubleBuffer(1);
	public Vector2d getMousePosition(Vector2d dest)
	{
		glfwGetCursorPos(windowHandle, xposBuff, yposBuff);
		dest.set(xposBuff.get(0), yposBuff.get(0));
		return dest;
	}


	public boolean isKeyPressed(int key)
	{
		return glfwGetKey(windowHandle, key) == GLFW_PRESS;
	}

}
