import common.*;
import materials.SimpleMaterial;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import system.GridMesh;
import system.Mouselook;
import system.Renderer;
import system.Timer;
import util.ModelLoader;
import world.VoxelGrid;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Main
{
	public static void main(String[] args)
	{
		new Main().run();
	}

	private void run()
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
		initGrid();


		renderer = new Renderer(window, camera);
		mouselook = new Mouselook(window, camera);
	}

	private void initGrid()
	{
		VoxelGrid voxelGrid = new VoxelGrid(4, 4, 4);
		voxelGrid.set(1, 1, 1, 590);
		voxelGrid.set(1, 1, 2, -600);
		//voxelGrid.set(1, 1, 3, 1);

		GridMesh gridMesh = new GridMesh(voxelGrid);
		root.addChild(gridMesh);
	}

	private Mouselook mouselook;

	private void testTriangles()
	{
		float[] vertices1 = {
				-0.5f, 0.5f, 0f,	0.0f, 0.0f, 1.0f,	-0.5f, 0.5f,
				-0.5f, -0.5f, 0f,   0.0f, 0.0f, 1.0f,	-0.5f, -0.5f,
				0.5f, -0.5f, 0f,    0.0f, 0.0f, 1.0f,	0.5f, -0.5f,
		};

		int[] indices1 = {
				0, 1, 2
		};

		float[] vertices2 = {
				0.3f, 0.5f, 0f,    0.0f, 0.0f, 1.0f,	-0.5f, 0.5f,
				0.5f, -0.3f, 0f,   0.0f, 0.0f, 1.0f,	-0.5f, -0.5f,
				0.5f, 0.5f, 0f,    0.0f, 0.0f, 1.0f,	0.5f, -0.5f,
		};

		BaseMaterial mat1 = new SimpleMaterial(SimpleMaterial.Flags.TEXTURED);
		mat1.setTexture("res/textures/tex1.jpg");

		Mesh mesh1 = new Mesh(vertices1, indices1, mat1);
		mesh1.setPosition(new Vector3f(0, 0, -3.f));
		root.addChild(mesh1);

		//BaseMaterial mat2 = new SimpleMaterial(SimpleMaterial.Flags.TEXTURED);
		BaseMaterial mat2 = new BaseMaterial(mat1);
		mat2.setTexture("res/textures/tex2.jpg");

		Mesh mesh2 = new Mesh(vertices2, indices1, mat2);
		mesh2.setPosition(new Vector3f(0, 2.0f, 0.f));
		mesh1.addChild(mesh2);
	}

	private void initMeshes()
	{
		//Mesh[] meshes = ModelLoader.load("res/models/monkey.obj");

		//for (Mesh m: meshes)
		//	root.addChild(m);
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
		//if (window.isKeyPressed(GLFW_KEY_SPACE))
	}

	private void quit()
	{
		window.destroy();
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}
