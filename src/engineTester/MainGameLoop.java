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
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

/*
 * File:	MainGameLoop.java
 * Purpose:	Contains the main() method, inside of which the "infinite"
 * 			game loop is contained.
 */
public class MainGameLoop {

	public static void main(String[] args) {

		// Open up the display.
		DisplayManager.createDisplay();

		// Create Loader.
		Loader loader = new Loader();

		// Load up RawModel & ModelTexture, make texturedModel, & extract its texture.
		RawModel rawModel = OBJLoader.loadObjModel("DragonBlender", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		TexturedModel texturedModel = new TexturedModel(rawModel, texture);
		ModelTexture modelTexture = texturedModel.getTexture();
		// Set damper and reflectivity values.
		modelTexture.setShineDamper(10);
		modelTexture.setReflectivity(1);
		// Make Entity with TexturedModel.
		Entity entity = new Entity(texturedModel, new Vector3f(0, -4, -25), 0, 0, 0, 1);
		// Make lightsource.
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		// Create camera.
		Camera camera = new Camera();

		// Create the MasterRenderer
		MasterRenderer renderer = new MasterRenderer();

		// The actual game loop. Exit when user clicks 'x' button.
		while (!Display.isCloseRequested()) {
			entity.increaseRotation(0, 0.3f, 0);
			camera.move();

			/*
			 * // For each entity, for each frame, process the entity. for (Entity entity :
			 * entities) { renderer.processEntity(entity); }
			 */
			renderer.processEntity(entity);

			// Render each frame.
			renderer.render(light, camera);

			// Update the display each frame.
			DisplayManager.updateDisplay();
		}

		// Cleanup loader & renderer upon closing.
		renderer.cleanUp();
		loader.cleanUp();
		// Close display once loop is exited.
		DisplayManager.closeDisplay();
	}

}
