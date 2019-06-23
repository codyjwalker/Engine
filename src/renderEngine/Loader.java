package renderEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/*
 * File:	Loader.java
 * Purpose:	Loads 3D models into memory by storing positional data about
 * 			the model in a VAO.
 */
public class Loader {
	
	// Lists storing IDs of VAOs and VBOs to be used for cleanup.
	private List<Integer> VAOs = new ArrayList<Integer>();
	private List<Integer> VBOs = new ArrayList<Integer>();
	
	// Takes in positions of the model's vertices, loads this data into
	// a VAO, and then returns information about the VAO as a RawModel object.
	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		// Store positional data into the first (0) attribute list of the VAO.
		storeDataInAttributeList(0, positions);
		// Unbind VAO when finished with it.
		unbindVAO();
		// Return the data we created about the VAO.
		return new RawModel(vaoID, positions.length / 3);
	}
	
	// Called upon closing of engine to delete the VAOs & VBOs we created.
	public void cleanUp() {
		for (int vao:VAOs) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:VBOs) {
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	// Creates a new, empty VAO, returning its ID.
	private int createVAO() {
		// Create the empty VAO & store its ID.
		int vaoID = GL30.glGenVertexArrays();
		// Add it to our list so we can delete it later.
		VAOs.add(vaoID);
		// Activate the VAO by binding it.
		GL30.glBindVertexArray(vaoID);
		// Return the VAOs ID.
		return vaoID;
	}
	
	// Stores the data into one of the attribute lists of the VAO.
	private void storeDataInAttributeList(int attributeNumber, float[] data) {
		// Must store data in Attribute List as VBO, so create an empty one.
		int vboID = GL15.glGenBuffers();
		// Add it to our list so we can delete it later.
		VBOs.add(vboID);
		// Same with VAO's, must bind before doing anything with it.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// Convert data into a FloatBuffer.
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		// Store FloatBuffer with our data into VBO.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		// Put VBO into one of the VAO's Attribute Lists.
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
		// Unbind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	// Unbinds the VAO.
	private void unbindVAO() {
		// Put in '0' instead of vaoID to un-bind.
		GL30.glBindVertexArray(0);
	}
	
	// Converts float-array of data into a FloatBuffer.
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		// First create empty FloatBuffer.
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		// Put the data in the buffer.
		buffer.put(data);
		// Prepare the buffer to be read from by 'flipping' it.
		buffer.flip();
		// Return the buffer so that we can use it to store into VBO.
		return buffer;
	}

}
