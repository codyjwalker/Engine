package toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public class MousePicker {

	private Vector3f currRay;
	private Matrix4f viewMatrix, projectionMatrix;
	private Camera camera;

	public MousePicker(Camera camera, Matrix4f projectionMatrix) {
		this.camera = camera;
		this.projectionMatrix = projectionMatrix;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}

	public Vector3f getCurrRay() {
		return this.currRay;
	}

	// Updates ViewMatrix & updates current MouseRay.
	public void update() {
		this.viewMatrix = Maths.createViewMatrix(this.camera);
		this.currRay = calculateMouseRay();
	}

	// Does conversion between 2D mouse position to 3D ray.
	private Vector3f calculateMouseRay() {
		float mouseX, mouseY;
		Vector2f normalizedCoords;
		Vector3f worldRay;
		Vector4f clipCoords, eyeCoords;
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		// Convert from Viewport Space to Normalized Device Space.
		normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		// Convert from Normalized Device Space to Homogeneous Clip Space.
		clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f,
				1f);
		// Convert from Homogeneous Clip Space to Eye Space.
		eyeCoords = toEyeCoords(clipCoords);
		// Convert from Eye Space to World Space.
		worldRay = toWorldCoords(eyeCoords);

		return worldRay;
	}

	// Converts from Homogeneous Clip Coordinates to Eye Coordinates.
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection;
		Vector4f eyeCoords;
		invertedProjection = Matrix4f.invert(this.projectionMatrix, null);
		eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);

		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	// Converts from Eye Coordinates to World Coordinates.
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView;
		Vector4f rayWorld;
		Vector3f mouseRay;
		invertedView = Matrix4f.invert(this.viewMatrix, null);
		rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();

		return mouseRay;
	}

	// Converts mouse coordinates to OpenGL coordinate system.
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x, y;
		x = (2f * mouseX) / Display.getWidth() - 1f;
		y = (2f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}

}
