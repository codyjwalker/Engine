package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.vert";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.frag";
	private static final int MAX_LIGHTS = 4;

	private int location_transformation_matrix;
	private int location_projection_matrix;
	private int location_view_matrix;
	private int location_light_position[];
	private int location_light_color[];
	private int location_shine_damper;
	private int location_reflectivity;
	private int location_use_fake_lighting;
	private int location_sky_color;
	private int location_number_of_rows;
	private int location_offset;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	// Bind attributes of VAO to variables.
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texture_coordinates");
		super.bindAttribute(2, "normal");
	}

	// Gets the locations of all uniform variables.
	@Override
	protected void getAllUniformLocations() {
		this.location_transformation_matrix = super.getUniformLocation(
				"transformation_matrix");
		this.location_projection_matrix = super.getUniformLocation(
				"projection_matrix");
		this.location_view_matrix = super.getUniformLocation("view_matrix");
		this.location_shine_damper = super.getUniformLocation("shine_damper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_use_fake_lighting = super.getUniformLocation(
				"use_fake_lighting");
		this.location_sky_color = super.getUniformLocation("sky_color");
		this.location_number_of_rows = super.getUniformLocation(
				"number_of_rows");
		this.location_offset = super.getUniformLocation("offset");

		this.location_light_position = new int[MAX_LIGHTS];
		this.location_light_color = new int[MAX_LIGHTS];
		// Get location of each of elements in arrays & store in int arrays.
		for (int i = 0; i < MAX_LIGHTS; i++) {
			this.location_light_position[i] = super.getUniformLocation(
					"light_position[" + i + "]");
			this.location_light_color[i] = super.getUniformLocation(
					"light_color[" + i + "]");
		}
	}

	// Loads transformation matrix to uniform variable.
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(this.location_transformation_matrix, matrix);
	}

	// Loads projection matrix to uniform variable.
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(this.location_projection_matrix, projection);
	}

	// Loads view matrix to uniform variable.
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(this.location_view_matrix, viewMatrix);
	}

	// Loads up lights uniform variables.
	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				// Load up position & color of light i
				super.loadVector(this.location_light_position[i],
						lights.get(i).getPosition());
				super.loadVector(this.location_light_color[i],
						lights.get(i).getColor());
			} else {
				// If no light, load up empty vectors.
				super.loadVector(this.location_light_position[i],
						new Vector3f(0, 0, 0));
				super.loadVector(this.location_light_color[i],
						new Vector3f(0, 0, 0));
			}
		}
	}

	// Loads up shine uniform variables.
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(this.location_shine_damper, damper);
		super.loadFloat(this.location_reflectivity, reflectivity);
	}

	// Loads up fake lighting uniform variable.
	public void loadFakeLightingVariable(boolean useFakeLighting) {
		super.loadBoolean(this.location_use_fake_lighting, useFakeLighting);
	}

	// Loads up sky color to uniform variable.
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(this.location_sky_color, new Vector3f(r, g, b));
	}

	// Loads up the number of rows to uniform variable.
	public void loadNumberOfRows(int numberOfRows) {
		super.loadInt(this.location_number_of_rows, numberOfRows);
	}

	// Loads up the offset to uniform variable.
	public void loadOffset(float f, float g) {
		super.loadVector2D(this.location_offset, new Vector2f(f, g));
	}

}
