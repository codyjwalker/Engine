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
import skybox.SkyboxRenderer;
import terrains.Terrain;

/*
 * File: MasterRenderer.java Purpose: Abstractifies the rendering process so
 * that entities sharing the same texture do not cause the renderer to do
 * extra computations by loading up the same TexturedModel for each entity
 * sharing it.
 */
public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	private static final float RED = 0.54444f;
	private static final float GREEN = 0.62f;
	private static final float BLUE = 0.69f;

	private Matrix4f projectionMatrix;
	private StaticShader shader;
	private EntityRenderer renderer;
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader;
	private SkyboxRenderer skyboxRenderer;

	// Hashmap of all TexturedModels & their respective entities.
	private Map<TexturedModel, List<Entity>> entities;
	private List<Terrain> terrains;

	public MasterRenderer(Loader loader) {
		enableCulling();

		// Initialize globals.
		this.entities = new HashMap<TexturedModel, List<Entity>>();
		this.terrains = new ArrayList<Terrain>();

		this.shader = new StaticShader();
		this.terrainShader = new TerrainShader();

		createProjectionMatrix();

		this.renderer = new EntityRenderer(this.shader, this.projectionMatrix);
		this.terrainRenderer = new TerrainRenderer(this.terrainShader,
				this.projectionMatrix);
		this.skyboxRenderer = new SkyboxRenderer(loader, this.projectionMatrix);
	}

	// Cull faces inside objects that we wouldn't see anyways to reduce
	// computations.
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	// Disable Culling on transparent objects so that we see all parts of
	// them.
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(List<Light> lights, Camera camera) {
		prepare();
		// Render the entities.
		this.shader.start();
		this.shader.loadSkyColor(RED, GREEN, BLUE);
		this.shader.loadLights(lights);
		this.shader.loadViewMatrix(camera);
		this.renderer.render(entities);
		this.shader.stop();
		// Render the terrains.
		this.terrainShader.start();
		this.terrainShader.loadSkyColor(RED, GREEN, BLUE);
		this.terrainShader.loadLight(lights);
		this.terrainShader.loadViewMatrix(camera);
		this.terrainRenderer.render(this.terrains);
		this.terrainShader.stop();
		// Render skybox.
		this.skyboxRenderer.render(camera, RED, GREEN, BLUE);
		// Clear entities and terrains each frame.
		this.entities.clear();
		this.terrains.clear();
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
		float aspectRatio = (float) Display.getWidth()
				/ (float) Display.getHeight();
		float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f)))
				* aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;

		this.projectionMatrix = new Matrix4f();
		this.projectionMatrix.m00 = xScale;
		this.projectionMatrix.m11 = yScale;
		this.projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
		this.projectionMatrix.m23 = -1;
		this.projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE)
				/ frustumLength);
		this.projectionMatrix.m33 = 0;
	}

	// Sorts all entities ready to be rendered into the correct list each
	// frame.
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = this.entities.get(entityModel);
		// If batch for curr TexturedModel already exists, add entity straight
		// to that
		// batch.
		if (batch != null) {
			batch.add(entity);
		} // Else, create new batch for curr TexturedModel.
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			// Add the entity to the new batch & add batch to HashMap.
			newBatch.add(entity);
			this.entities.put(entityModel, newBatch);
		}
	}

	// Adds terrain to list of Terrains.
	public void processTerrain(Terrain terrain) {
		this.terrains.add(terrain);
	}

	public void cleanUp() {
		this.shader.cleanUp();
		this.terrainShader.cleanUp();
	}
}
