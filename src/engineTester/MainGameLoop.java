package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

/*
 * File:	MainGameLoop.java
 * Purpose:	Contains the main() method, inside of which the "infinite"
 * 			game loop is contained.
 */
public class MainGameLoop {
	
	public static void main(String[] args) {
		
		// Open up the display.
		DisplayManager.createDisplay();
		// Create Loader & Renderer so that we can use them.
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		// Vertices of a simple rectangle.
		float[] vertices = {
				// Bottom Left
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				// Top Right
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f
		};
		
		RawModel model = loader.loadToVAO(vertices);
		
		// The actual game loop.  Exit when user clicks 'x' button.
		while(!Display.isCloseRequested()) {
			// Prepare the Renderer each frame.
			renderer.prepare();

			// GAME LOGIC
			
			// Render the model each frame.
			renderer.render(model);
			
			// Update the display each frame.
			DisplayManager.updateDisplay();
		}
		
		// Cleanup loader upon closing.
		loader.cleanUp();
		// Close display once loop is exited.
		DisplayManager.closeDisplay();
	}

}
