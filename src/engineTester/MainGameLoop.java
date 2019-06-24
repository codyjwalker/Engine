package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
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

		// Create Loader, Renderer, & Shader so that we can use them.
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);

		// Load up RawModel & ModelTexture.
		RawModel model = OBJLoader.loadObjModel("Ball", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("sassy"));
		// Make TexturedModel out of model & texture.
		TexturedModel texturedModel = new TexturedModel(model, texture);
		// Make Entity with TexturedModel.
		Entity entity = new Entity(texturedModel, new Vector3f(0, -0.2f, -10), 0, 0, 0, 1);
		// Create camera.
		Camera camera = new Camera();

		// The actual game loop. Exit when user clicks 'x' button.
		while (!Display.isCloseRequested()) {
			entity.increaseRotation(0, 0.3f, 0);
			camera.move();
			// Prepare the Renderer each frame.
			renderer.prepare();

			// Start the shader program before rendering.
			shader.start();
			
			// Load camera into shader.
			shader.loadViewMatrix(camera);

			// Render the model each frame.
			renderer.render(entity, shader);

			// Stop shader after render finished.
			shader.stop();

			// Update the display each frame.
			DisplayManager.updateDisplay();
		}

		// Cleanup shader & loader upon closing.
		shader.cleanUp();
		loader.cleanUp();
		// Close display once loop is exited.
		DisplayManager.closeDisplay();
	}

}
