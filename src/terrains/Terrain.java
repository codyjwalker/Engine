package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

/*
 * File: Terrain.java Purpose: Represents a terrain object.
 */
public class Terrain {

	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private BufferedImage image;
	private float[][] heights;

	public Terrain(int gridX, int gridZ, Loader loader,
			TerrainTexturePack texturePack, TerrainTexture blendMap,
			String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
		this.image = null;
	}

	// Temporary Terrain Generator (WILL BE CHANGED SOON!)
	private RawModel generateTerrain(Loader loader, String heightMap) {
		// Create BufferedImage of terrain map.
		try {
			image = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int vertexCount = image.getHeight();
		heights = new float[vertexCount][vertexCount];

		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				vertices[vertexPointer * 3] = (float) j
						/ ((float) vertexCount - 1) * SIZE;
				// Get height from heightMap.
				float height = getHeight(j, i, image);
				vertices[vertexPointer * 3 + 1] = height;
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 2] = (float) i
						/ ((float) vertexCount - 1) * SIZE;
				// Calculate the normals.
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j
						/ ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i
						/ ((float) vertexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < vertexCount - 1; gz++) {
			for (int gx = 0; gx < vertexCount - 1; gx++) {
				int topLeft = (gz * vertexCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	// Returns the height at the x,y position of the heightMap.
	private float getHeight(int x, int y, BufferedImage heightMap) {
		if ((x < 0) || (x >= image.getHeight()) || (y < 0)
				|| (y >= image.getHeight())) {
			return 0;
		}
		// Get pixel color of height map & make value workable.
		float height = image.getRGB(x, y);
		height += (MAX_PIXEL_COLOR / 2f);
		height /= MAX_PIXEL_COLOR;
		height *= MAX_HEIGHT;

		return height;
	}
	// Returns the normal at the x,y position of the heightMapl
	private Vector3f calculateNormal(int x, int y, BufferedImage heightMap) {
		float heightLeft = getHeight(x - 1, y, heightMap);
		float heightRight = getHeight(x + 1, y, heightMap);
		float heightDown = getHeight(x, y - 1, heightMap);
		float heightUp = getHeight(x, y + 1, heightMap);

		Vector3f normal = new Vector3f(heightLeft - heightRight, 2f,
				heightDown - heightUp);
		normal.normalise();
		return normal;
	}

	// Gets the height of the terrain of the x,z world coordinate.
	public float getHeightOfTerrain(float worldX, float worldZ) {
		// Get coordinates in terrain.
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		// Get which gridSquare this x,z coordinate player is in.
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if ((gridX >= heights.length - 1) || (gridZ >= heights.length - 1)
				|| (gridX < 0) || (gridZ < 0)) {
			return 0;
		}
		// Find where on the gridsquare the player is.
		float xCoord = (terrainX % gridSquareSize);
		float zCoord = (terrainZ % gridSquareSize);
		float result;

		if (xCoord <= (1 - zCoord)) {
			result = Maths.barryCentric(
					new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			result = Maths.barryCentric(
					new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
					new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return result;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

}
