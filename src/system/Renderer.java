package system;

import common.*;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

public class Renderer
{
	private Window window;
	private ArrayList<Matrix4f> stack;
	private Camera camera;

	public Renderer(Window window, Camera camera)
	{
		this.camera = camera;
		this.window = window;

		initGL();
	}

	private void initGL()
	{
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.3f, 0.3f, 0.3f, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	private void dfs(Node node)
	{
		if (node instanceof Mesh)
			draw((Mesh) node);

		for (Node child : node.getChildren()) {
			dfs(child);
		}
	}

	private FloatBuffer MVPbuffer = BufferUtils.createFloatBuffer(16);

	private void draw(Mesh mesh)
	{
		Matrix4f model = mesh.renderTransform;
		viewProj.mul(model).get(MVPbuffer);

		mesh.draw(MVPbuffer);
	}

	private Matrix4f viewProj = new Matrix4f();
	private Matrix4f view = new Matrix4f();

	private static Matrix4f IDENTITY = new Matrix4f();

	public void renderScene(Node treeRoot)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		calculateWorldTransforms(treeRoot, IDENTITY);

		Matrix4f proj = camera.getProjectionMatrix();
		camera.renderTransform.invert(view);
		proj.mul(view, viewProj);

		dfs(treeRoot);

		window.swapBuffers();
	}

	private void calculateWorldTransforms(Node node, Matrix4f prev)
	{
		Matrix4f cur = prev;
		if (node instanceof Spatial)
		{
			Spatial spatial = ((Spatial) node);
			Matrix4f transform = spatial.getTransform();

			//TODO transform.mul(prev) ??
			cur = prev.mul(transform, spatial.renderTransform);
		}

		for (Node child : node.getChildren())
		{
			calculateWorldTransforms(child, cur);
		}
	}
}
