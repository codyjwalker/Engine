package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/*
 * File:	Renderer.java
 * Purpose:	Renders the model from the VAO.
 */
public class Renderer {
	
	// Called once every frame to prepare OpenGL to render the game.
	public void prepare() {
		// Clear color from the last frame.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1,  0,  0,  1);
	}
	
	// Renders the raw model.
	public void render(RawModel model) {
		// First bind the VAO.
		GL30.glBindVertexArray(model.getVaoID());
		// Activate the Attribute List in which our data is stored.
		GL20.glEnableVertexAttribArray(0);
		// Render the model!
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		// Disable the Attribute List and un-bind the VAO now that we're done.
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
