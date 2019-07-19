package system;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import java.util.LinkedList;

public class MousePosCallback extends GLFWCursorPosCallback
{
	private LinkedList<MousePosListenerI> listenerList = new LinkedList<>();

	@Override
	public void invoke(long window, double xpos, double ypos)
	{
		for (MousePosListenerI listener : listenerList)
		{
			listener.mouseMove(xpos, ypos);
		}
	}

	public void addListener(MousePosListenerI listener) {
		listenerList.add(listener);
	}
}
