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
				-0.5f, 0.5f, 0f,	// v0
				-0.5f, -0.5f, 0f,	// v1
				0.5f, -0.5f, 0f,	// v2
				0.5f, 0.5f, 0f,		// v3
		};
		
		// Indices for Index Buffer.
		int[] indices = {
				0,1,3,	// Top left triangle.
				3,1,2	// Bottom right triangle.
		};
		
		RawModel model = loader.loadToVAO(vertices, indices);
		
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
