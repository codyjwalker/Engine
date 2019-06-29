package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

/*
 * File:	MainGameLoop.java
 * Purpose:	Contains the main() method, inside of which the "infinite"
 * 			game loop is contained.
 */
public class MainGameLoop {

	private static Loader loader;
	private static RawModel rawModel;
	private static ModelTexture texture, modelTexture;
	private static TexturedModel texturedModel;
	private static Entity entity;
	private static Light light;
	private static Camera camera;
	private static MasterRenderer renderer;
	private static Terrain terrain, terrain2;

	public static void main(String[] args) {
		init();

		// The actual game loop. Exit when user clicks 'x' button.
		while (!Display.isCloseRequested()) {
			// TODO: FIGURE OUT IF THIS LINE SHOULD BE MOVED (PROBABLY SHOULD WITH MORE
			// ENTITIES).
			entity.increaseRotation(0, 0.3f, 0);
			camera.move();

			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);

			// For each entity, for each frame, process the entity.
			// for (Entity entity : entities) {
			// renderer.processEntity(entity);
			// }

			// Or, for now, for each frame, process THE entity.
			renderer.processEntity(entity);

			// Render each frame.
			renderer.render(light, camera);

			// Update the display each frame.
			DisplayManager.updateDisplay();
		}
		terminate();
	}

	// Initializes all objects needed for rendering.
	private static void init() {
		// Open up the display.
		DisplayManager.createDisplay();

		// Create Loader.
		loader = new Loader();

		// Load up RawModel & ModelTexture, create texturedModel, & extract its texture.
		rawModel = OBJLoader.loadObjModel("DragonBlender", loader);
		texture = new ModelTexture(loader.loadTexture("white"));
		texturedModel = new TexturedModel(rawModel, texture);
		modelTexture = texturedModel.getTexture();

		// Set damper and reflectivity values.
		modelTexture.setShineDamper(10);
		modelTexture.setReflectivity(1);

		// Create Entity with TexturedModel.
		entity = new Entity(texturedModel, new Vector3f(0, -4, -25), 0, 0, 0, 1);

		// Create lightsource.
		light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));

		// Create terrain.
		terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("desert")));
		terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("desert")));

		// Create camera.
		camera = new Camera();

		// Create the MasterRenderer
		renderer = new MasterRenderer();
	}

	// Cleans up renderer & loader, then closes display.
	private static void terminate() {
		// Cleanup loader & renderer upon closing.
		renderer.cleanUp();
		loader.cleanUp();
		// Close display once loop is exited.
		DisplayManager.closeDisplay();
	}
}
