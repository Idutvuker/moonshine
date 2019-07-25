package system;

import common.*;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.*;

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

	private FloatBuffer MatBuffer = BufferUtils.createFloatBuffer(16);
	private Matrix4f tMat = new Matrix4f();

	private void updateMaterial(Mesh mesh)
	{
		BaseMaterial mat = mesh.getMaterial();
		mat.use();

		Matrix4f model = mesh.renderTransform;

		if (mat.usesUniform(BaseMaterial.Uniform.MODEL))
			mat.setMatrix4f("uf_ModelMat", model.get(MatBuffer));

		if (mat.usesUniform(BaseMaterial.Uniform.VIEW))
			mat.setMatrix4f("uf_ViewMat", view.get(MatBuffer));

		if (mat.usesUniform(BaseMaterial.Uniform.PROJECTION))
			mat.setMatrix4f("uf_ProjectionMat", proj.get(MatBuffer));

		if (mat.usesUniform(BaseMaterial.Uniform.MODELVIEW))
		{
			view.mul(model, tMat);
			mat.setMatrix4f("uf_ModelViewMat", tMat.get(MatBuffer));
		}

		if (mat.usesUniform(BaseMaterial.Uniform.MODELVIEWPROJECTION))
		{
			viewProj.mul(model, tMat);
			mat.setMVP(tMat.get(MatBuffer));
		}
	}

	private void renderMesh(Mesh mesh)
	{
		updateMaterial(mesh);
		mesh.draw();
	}

	private Matrix4f proj;

	private Matrix4f viewProj = new Matrix4f();
	private Matrix4f view = new Matrix4f();

	private static final Matrix4f IDENTITY = new Matrix4f();

	public void renderScene(Node treeRoot)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		calculateWorldTransforms(treeRoot, IDENTITY);

		//System.out.println(camera.renderTransform);

		proj = camera.getProjectionMatrix();
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
