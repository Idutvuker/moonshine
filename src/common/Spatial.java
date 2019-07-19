package common;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Spatial extends Node
{
	private Matrix4f transform = new Matrix4f();
	public Matrix4f renderTransform = new Matrix4f();

	public Matrix4f getTransform() {
		return transform;
	}

	public void setTransform(Matrix4f transform) {
		this.transform = transform;
	}

	public void setPosition(Vector3f position)
	{
		transform.setTranslation(position);
	}

	public Vector3f getPosition(Vector3f dest)
	{
	   return transform.getTranslation(dest);
	}

	public void translate(float x, float y, float z)
	{
		transform.translate(x, y, z);
	}
}
