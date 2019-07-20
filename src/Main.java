import common.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import system.Mouselook;
import system.Renderer;
import system.Timer;
import util.ModelLoader;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Main
{
	public static void main(String[] args)
	{
		new Main().run();
	}

	public void run()
	{
		init();
		loop();
		quit();
	}

	private Window window;
	private Camera camera;

	private int window_width = 700;
	private int window_height = 700;

	private void init()
	{
		initGLFW();

		window = new Window(window_width, window_height, "Moonshine");

		initWorld();
	}

	private Renderer renderer;

	private void initWorld()
	{
		root = new Node();

		initMeshes();
		initCamera();


		renderer = new Renderer(window, camera);
		mouselook = new Mouselook(window, camera);
	}

	private Mouselook mouselook;


	private Mesh mesh1;
	private Mesh mesh2;
	private void initMeshes()
	{
		float[] vertices1 = {
				-0.5f, 0.5f, 0f,    -0.5f, 0.5f,
				-0.5f, -0.5f, 0f,   -0.5f, -0.5f,
				0.5f, -0.5f, 0f,    0.5f, -0.5f,
		};

		int[] indices1 = {
				0, 1, 2
		};

		float[] vertices2 = {
				0.3f, 0.5f, 0f,    -0.5f, 0.5f,
				0.5f, -0.3f, 0f,   -0.5f, -0.5f,
				0.5f, 0.5f, 0f,    0.5f, -0.5f,
		};

		Material mat1 = new Material(
				"res/shaders/vs_basic.glsl",
				"res/shaders/fs_basic.glsl");

		mat1.setTexture("res/textures/tex1.jpg");

		mesh1 = new Mesh(3, vertices1, indices1, mat1);
		mesh1.setPosition(new Vector3f(0, 0, -3.f));
		root.addChild(mesh1);

		Material mat2 = new Material(
				"res/shaders/vs_basic.glsl",
				"res/shaders/fs_basic.glsl");

		mat2.setTexture("res/textures/tex2.jpg");

		mesh2 = new Mesh(3, vertices2, indices1, mat2);
		mesh2.setPosition(new Vector3f(0, 2.0f, 0.f));
		mesh1.addChild(mesh2);


		float[] verts = {1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
						-1.0f, 0.0f, -1.0f, 0.0f, 0.0f,
						-1.0f, 0.0f, 1.0f, 0.0f, 0.0f,

						1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
						1.0f, 0.0f, -1.0f, 0.0f, 0.0f,
						-1.0f, 0.0f, -1.0f, 0.0f, 0.0f};

		int[] indices = {0, 1, 2, 0, 3, 1};

		Mesh[] meshes = ModelLoader.load("res/models/monkey.obj");
		root.addChild(meshes[0]);
	}


	private void initCamera()
	{
		camera = new Camera(70.f, ((float) window_width) / window_height, 0.01f, 100.f);
		root.addChild(camera);
	}

	private Timer timer = new Timer();

	private void initGLFW()
	{
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
	}

	private ArrayList<Mesh> meshes = new ArrayList<>();

	private Node root;


	private double prevTime;

	private void loop()
	{
		while(!window.shouldClose())
		{
			double delta = timer.tick();
			glfwPollEvents();

			preprocess((float) delta);
			//System.out.println(delta);

			renderer.renderScene(root);

			process((float) delta);
		}
	}

	private void preprocess(float delta)
	{
		mouselook.update(delta);
	}

	private void process(float delta)
	{
		if (window.isKeyPressed(GLFW_KEY_SPACE))
			mesh1.getTransform().rotateZ(delta);

		if (window.isKeyPressed(GLFW_KEY_UP))
			mesh2.translate(0, delta, 0);

		if (window.isKeyPressed(GLFW_KEY_DOWN))
			mesh2.getTransform().rotateZ(delta);
	}

	private void quit()
	{
		window.destroy();
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

}
