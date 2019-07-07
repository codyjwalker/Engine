package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import textures.TextureData;
/*
 * File: Loader.java Purpose: Loads 3D models into memory by storing
 * positional data about the model in a VAO.
 */
public class Loader {

	// Represents how strong of MipMapping to use.
	private static final float LOD_BIAS = -0.5f;

	// Lists storing IDs of VAOs, VBOs, & textures. to be used for cleanup.
	private List<Integer> VAOs = new ArrayList<Integer>();
	private List<Integer> VBOs = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	// Takes in positions of the model's vertices, loads this data into
	// a VAO, and then returns information about the VAO as a RawModel object.
	public RawModel loadToVAO(float[] positions, float[] textureCoordinates,
			float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		// Store positional data into the first (0) attribute list of the VAO.
		storeDataInAttributeList(0, 3, positions);
		// Store texture data into the second (1) attribute list of VAO.
		storeDataInAttributeList(1, 2, textureCoordinates);
		// Store normals data into third (2) attribute list of VAO.
		storeDataInAttributeList(2, 3, normals);
		// Unbind VAO when finished with it.
		unbindVAO();
		// Return the data we created about the VAO.
		return new RawModel(vaoID, indices.length);
	}

	// Loads a texture into OpenGl.
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG",
					new FileInputStream("res/" + fileName + ".png"));
			// Generate MipMap for rendering objects far away at lower
			// resolutions.
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS,
					LOD_BIAS);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		this.textures.add(textureID);

		return textureID;
	}

	// Loads a texture for GUI into OpenGL.
	public RawModel loadToVAO(float[] positions, int dimensions) {
		// Create new VAO for GUI textures.
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}

	// Called upon closing of engine to delete the VAOs, VBOs, & textures we
	// created.
	public void cleanUp() {
		for (int vao : this.VAOs) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : this.VBOs) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : this.textures) {
			GL11.glDeleteTextures(texture);
		}
	}

	// Loads up cupbemap to OpenGL, returning its ID so that we can bind it.
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		// Go thru all 6 texture & load up each one into cubemap.
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile(
					"res/" + textureFiles[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0,
					GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,
				GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,
				GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		this.textures.add(texID);
		return texID;
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
	private void storeDataInAttributeList(int attributeNumber,
			int coordinateSize, float[] data) {
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
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize,
				GL11.GL_FLOAT, false, 0, 0);
		// Unbind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	// Unbinds the VAO.
	private void unbindVAO() {
		// Put in '0' instead of vaoID to un-bind.
		GL30.glBindVertexArray(0);
	}

	// Loads up index buffer and binds it to VAO.
	private void bindIndicesBuffer(int[] indices) {
		// Create empty VBO, add to list of VBOs, & bind it.
		int vboID = GL15.glGenBuffers();
		VBOs.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		// Convert indices into IntBuffer.
		IntBuffer buffer = storeDataInIntBuffer(indices);
		// Store into VBO.
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer,
				GL15.GL_STATIC_DRAW);
	}

	// Converts float-array of data into a FloatBuffer.
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		// First create empty FloatBuffer.
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		// Put the data in the buffer.
		buffer.put(data);
		// Prepare the buffer to be read from by 'flipping' it.
		buffer.flip();
		return buffer;
	}

	// Stores indices into IntBuffer.
	private IntBuffer storeDataInIntBuffer(int[] data) {
		// Create empty IntBuffer & put data into the buffer.
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	// Loads image as byte buffer & returns at TexturaData obj.
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(
					"Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}

}
