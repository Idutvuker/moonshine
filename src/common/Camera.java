package common;

import org.joml.Matrix4f;

public class Camera extends Spatial
{
	private Matrix4f projectionMatrix;

	public Camera(float fovy, float aspect, float zNear, float zFar)
	{
		projectionMatrix = new Matrix4f();
		projectionMatrix.perspective(fovy, aspect, zNear, zFar);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
