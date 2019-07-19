package system;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer
{
	private double prevTime;

	public double tick()
	{
		return -prevTime + (prevTime = glfwGetTime());
	}
}
