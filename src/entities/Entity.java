package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

/*
 * File: Entity.java Purpose: An instance of a TexturedModel. Contains
 * TexturedModel as well as position, rotation, and scale we want to render
 * model at in 3D world.
 */
public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	private int textureIndex; // Index in the texture atlas.

	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.textureIndex = 0;
	}

	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, int textureIndex) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.textureIndex = textureIndex;
	}

	// Move the entity in the world.
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	// Rotate the entity in the world.
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	// Returns the texture's X-offset in the texture atlas.
	public float getTextureXOffset() {
		int col = this.textureIndex % this.model.getTexture().getNumberOfRows();
		return (float) col / (float) this.model.getTexture().getNumberOfRows();
	}

	// Returns the texture's Y-offset in the texture atlas.
	public float getTextureYOffset() {
		int row = this.textureIndex / this.model.getTexture().getNumberOfRows();
		return (float) row / (float) this.model.getTexture().getNumberOfRows();

	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
