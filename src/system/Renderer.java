package system;

import common.*;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.lwjgl.opengl.GL33.*;

public class Renderer
{
	private Window window;
	private Camera camera;

	public Renderer(Window window, Camera camera)
	{
		this.camera = camera;
		this.window = window;

		this.meshQueue = new ArrayList<>(1024);

		initGL();
	}

	private void initGL()
	{
		//System.out.println(glGetString(GL_RENDERER));

		glEnable(GL_DEPTH_TEST);
		glClearColor(0.0f, 0.0f, 0.0f, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	private FloatBuffer MVPBuffer = BufferUtils.createFloatBuffer(16);

	private void renderMesh(Mesh mesh)
	{
		Matrix4f model = mesh.renderTransform;
		viewProj.mul(model, model).get(MVPBuffer);

		mesh.draw(MVPBuffer);
	}

	private Matrix4f viewProj = new Matrix4f();
	private Matrix4f view = new Matrix4f();

	private static final Matrix4f IDENTITY = new Matrix4f();

	public void renderScene(Node treeRoot)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		calculateWorldTransforms(treeRoot, IDENTITY);

		//System.out.println(camera.renderTransform);

		Matrix4f proj = camera.getProjectionMatrix();
		camera.renderTransform.invert(view);
		proj.mul(view, viewProj);

		drawQueue();

		window.swapBuffers();
	}

	private List<Mesh> meshQueue;

	private void drawQueue()
	{
		for (Mesh mesh: meshQueue)
			renderMesh(mesh);

		meshQueue.clear();
	}


	private void calculateWorldTransforms(Node node, Matrix4f prev)
	{

		Matrix4f cur = prev;
		if (node instanceof Spatial)
		{
			Spatial spatial = ((Spatial) node);
			Matrix4f localTransform = spatial.getTransform();

			cur = prev.mul(localTransform, spatial.renderTransform);

			if (node instanceof Mesh)
				meshQueue.add((Mesh) node);

		}

		for (Node child : node.getChildren())
		{
			calculateWorldTransforms(child, cur);
		}
	}
}
