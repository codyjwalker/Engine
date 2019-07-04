package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

/*
 * File: Player.java Purpose: Object that represents the player.
 */
public class Player extends Entity {

	private static final float MOVE_SPEED = 120.0f;
	private static final float TURN_SPEED = 160.0f;
	private static final float GRAVITY = -150.0f;
	private static final float JUMP_POWER = 130.0f;
	private static final float TERRAIN_HEIGHT = 0.0f;

	private float currMoveSpeed, currTurnSpeed, upwardsSpeed;
	private boolean isAirborne;

	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.currMoveSpeed = 0.0f;
		this.currTurnSpeed = 0.0f;
		this.upwardsSpeed = 0.0f;
		this.isAirborne = false;
	}

	// Moves the player around.
	public void move() {
		checkInputs();
		super.increaseRotation(0,
				currTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currMoveSpeed * DisplayManager.getFrameTimeSeconds();
		// Trigonometry to figure out how far player moved in each direction.
		float dx = (float) (distance
				* (Math.sin(Math.toRadians(super.getRotY()))));
		float dz = (float) (distance
				* (Math.cos(Math.toRadians(super.getRotY()))));
		// Move the player by each amount.
		super.increasePosition(dx, 0, dz);
		// Work out players vertical position during jumps.
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0,
				upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		if (super.getPosition().y < TERRAIN_HEIGHT) {
			upwardsSpeed = 0;
			this.isAirborne = false;
			super.getPosition().y = TERRAIN_HEIGHT;
		}
	}

	// Makes the player jump into the air.
	private void jump() {
		if (!this.isAirborne) {
			this.upwardsSpeed = JUMP_POWER;
			// this.isAirborne = true;
		}
	}

	// Checks keyboard input.
	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currMoveSpeed = MOVE_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currMoveSpeed = -MOVE_SPEED;
		} else {
			this.currMoveSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currTurnSpeed = TURN_SPEED;
		} else {
			this.currTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

}
