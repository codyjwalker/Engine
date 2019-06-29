package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

/*
 * File:	MasterRenderer.java
 * Purpose:	Abstractifies the rendering process so that entities sharing the same texture 
 * 			do not cause the renderer to do extra computations by loading up the same 
 * 			TexturedModel for each entity sharing it.
 */
public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	private static final float RED = 0.2f;
	private static final float GREEN = 1;
	private static final float BLUE = 1;

	private Matrix4f projectionMatrix;
	private StaticShader shader;
	private EntityRenderer renderer;
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader;

	// Hashmap of all TexturedModels & their respective entities.
	private Map<TexturedModel, List<Entity>> entities;
	private List<Terrain> terrains;

	public MasterRenderer() {
		enableCulling();

		// Initialize globals.
		entities = new HashMap<TexturedModel, List<Entity>>();
		terrains = new ArrayList<Terrain>();

		shader = new StaticShader();
		terrainShader = new TerrainShader();

		createProjectionMatrix();

		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}

	// Cull faces inside objects that we wouldn't see anyways to reduce
	// computations.
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	// Disable Culling on transparent objects so that we see all parts of them.
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(Light light, Camera camera) {
		prepare();
		// Render the entities.
		shader.start();
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		// Render the terrains.
		terrainShader.start();
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		// Clear entities and terrains each frame.
		entities.clear();
		terrains.clear();
	}

	// Called once every frame to prepare OpenGL to render the game.
	public void prepare() {
		// Tell OpenGL to test which triangles are in front of each other.
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// Clear color from the last frame.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// Set color of background.
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}

	// Creates the projection matrix.
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projectionMatrix.m33 = 0;
	}

	// Sorts all entities ready to be rendered into the correct list each frame.
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		// If batch for curr TexturedModel already exists, add entity straight to that
		// batch.
		if (batch != null) {
			batch.add(entity);
		} // Else, create new batch for curr TexturedModel.
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			// Add the entity to the new batch & add batch to HashMap.
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	// Adds terrain to list of Terrains.
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
}
