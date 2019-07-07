package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.vert";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.frag";

	private int location_transformation_matrix;
	private int location_projection_matrix;
	private int location_view_matrix;
	private int location_light_position;
	private int location_light_color;
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
		this.location_light_position = super.getUniformLocation(
				"light_position");
		this.location_light_color = super.getUniformLocation("light_color");
		this.location_shine_damper = super.getUniformLocation("shine_damper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_use_fake_lighting = super.getUniformLocation(
				"use_fake_lighting");
		this.location_sky_color = super.getUniformLocation("sky_color");
		this.location_number_of_rows = super.getUniformLocation(
				"number_of_rows");
		this.location_offset = super.getUniformLocation("offset");
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

	// Loads up light uniform variables.
	public void loadLight(Light light) {
		super.loadVector(this.location_light_position, light.getPosition());
		super.loadVector(this.location_light_color, light.getColor());
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
