package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private static final float MOVEMENT_SCALER = 0.3f;

	private Vector3f position;
	private float pitch, yaw, roll, distanceFromPlayer, angleAroundPlayer;
	private Player player;

	public Camera(Player player) {
		this.player = player;
		this.position = new Vector3f(0, 0, 0);
		this.pitch = 20;
		this.yaw = 0;
//		this.roll = 0;
		this.distanceFromPlayer = 50;
		this.angleAroundPlayer = 0;
	}

	// Moves the camera around.
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + this.angleAroundPlayer);
	}

	// Calculates how zoomed in we are using mouse scroll-wheel as input.
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		this.distanceFromPlayer -= zoomLevel;
	}

	// Calculates the pitch of camera based on user input with mouse.
	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChage = Mouse.getDY() * 0.1f;
			this.pitch -= pitchChage;
		}
	}

	// Calculates how much camera should move around player.
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.3f;
			this.angleAroundPlayer -= angleChange;
		}
	}

	// Calculates horizontal distance from player.
	private float calculateHorizontalDistance() {
		return (float) (this.distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	// Calculates vertical distance from player.
	private float calculateVerticalDistance() {
		return (float) (this.distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	// Calculates actual (x,y,z) position of the camera.
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + this.angleAroundPlayer;
		float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		this.position.x = player.getPosition().x - xOffset;
		this.position.z = player.getPosition().z - zOffset;
		this.position.y = player.getPosition().y + verticalDistance;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}
