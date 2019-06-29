package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

public class TerrainRenderer {

	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	// Renders the terrains.
	public void render(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			// Prepare each terrain for rendering.
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			// Do the final render!
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}

	// Prepares all the TexturedModels.
	private void prepareTerrain(Terrain terrain) {
		// Extract RawModel out of TexturedModel.
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		// Activate the Attribute Lists in which our data is stored.
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		// Get shine variables and load them up into shader.
		ModelTexture modelTexture = terrain.getTexture();
		shader.loadShineVariables(modelTexture.getShineDamper(), modelTexture.getReflectivity());
		// Tell OpenGL which texture we would like to render.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTexture.getID());

	}

	// Unbinds the TexturedModel.
	private void unbindTexturedModel() {
		// Disable the Attribute Lists and un-bind the VAO now that we're done.
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	// Prepares the entities (instances) of each of the TexturedModels.
	private void loadModelMatrix(Terrain terrain) {
		// Load up entity's transformation to vertex shader.
		Matrix4f transformationMatrix = Maths
				.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
