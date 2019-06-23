package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
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
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();

		// Vertices of a simple rectangle.
		float[] vertices = { -0.5f, 0.5f, 0f, // v0
				-0.5f, -0.5f, 0f, // v1
				0.5f, -0.5f, 0f, // v2
				0.5f, 0.5f, 0f, // v3
		};

		// Indices for Index Buffer of the simple rectangle.
		int[] indices = { 0, 1, 3, // Top left triangle.
				3, 1, 2 // Bottom right triangle.
		};
		
		// Texture uv coordinates for one sassy texture.
		float[] textureCoordinates = {
				0,0,	// v0
				0,1,	// v1
				1,1,	// v2
				1,0		// v3
		};

		RawModel model = loader.loadToVAO(vertices, textureCoordinates, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("sassy"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(-1, 0, 0), 0, 0, 0, 1);

		// The actual game loop. Exit when user clicks 'x' button.
		while (!Display.isCloseRequested()) {
			// Prepare the Renderer each frame.
			renderer.prepare();

			// Start the shader program before rendering.
			shader.start();

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
