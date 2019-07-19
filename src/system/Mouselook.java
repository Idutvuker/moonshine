package system;

import common.Camera;
import common.Window;
import org.joml.Math;
import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;

public class Mouselook implements MousePosListenerI
{
	public float sensitivity = 0.001f;
	public float flySpeed = 3.f;

	private Camera camera;
	private Window window;
	private Matrix4f mat;

	public Mouselook(Window window, Camera camera)
	{
		this.camera = camera;
		this.window = window;
		mat = camera.getTransform();
		//parMat = ((Spatial) camera.getParent()).getTransform();

		window.getMouseCallback().addListener(this);

		window.setInputMode(GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		if (glfwRawMouseMotionSupported())
			window.setInputMode(GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);

		Vector2d pos = new Vector2d();
		window.getMousePosition(pos);
		prevx = pos.x;
		prevy = pos.y;
	}

	private double prevx, prevy;

	private final float XLIMIT = (float) (Math.PI / 2.0) - 0.01f;

	private Matrix3f rotate = new Matrix3f();
	private Matrix3f matYaw = new Matrix3f();
	private Matrix3f matPitch = new Matrix3f();


	private float pitch;

	@Override
	public void mouseMove(double xpos, double ypos)
	{
		float mx = (float) (-xpos + prevx) * sensitivity;
		float my = (float) (-ypos + prevy) * sensitivity;
		prevx = xpos;
		prevy = ypos;

		matYaw.rotateY(mx);

		//float rx = (float) Math.atan2(matPitch.m12(), matPitch.m22()); //euler x-axis angle

		if (pitch + my > XLIMIT)
			my = XLIMIT - pitch;
		else if (pitch + my < -XLIMIT)
			my = -XLIMIT - pitch;

		matPitch.rotateX(my);
		pitch += my;

		matYaw.mul(matPitch, rotate);


		//mat.setTranslation()
		//mat.set3x3(rotate);
	}


	private Vector3f position = new Vector3f();
	private Vector3f move_vec = new Vector3f();
	public void update(float delta)
	{
		move_vec.set(0.f, 0.f, 0.f);
		if (window.isKeyPressed(GLFW_KEY_W)) {
			move_vec.z -= 1;
		}
		if (window.isKeyPressed(GLFW_KEY_S)) {
			move_vec.z += 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			move_vec.x -= 1;
		}
		if (window.isKeyPressed(GLFW_KEY_D)) {
			move_vec.x += 1;
		}

		if (!move_vec.equals(0.f, 0.f, 0.f))
			move_vec.normalize();

		move_vec.mul(rotate);
		move_vec.mul(delta * flySpeed);

		mat.set3x3(rotate);
		mat.setTranslation(mat.getTranslation(position).add(move_vec));

		//mat.mul(translate);
	}
}
