package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.vert";
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.frag";

	private int location_transformation_matrix;
	private int location_projection_matrix;
	private int location_view_matrix;
	private int location_light_position;
	private int location_light_color;
	private int location_shine_damper;
	private int location_reflectivity;
	private int location_sky_color;
	private int location_background_texture;
	private int location_r_texture;
	private int location_g_texture;
	private int location_b_texture;
	private int location_blend_map;

	public TerrainShader() {
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
		location_transformation_matrix = super.getUniformLocation("transformation_matrix");
		location_projection_matrix = super.getUniformLocation("projection_matrix");
		location_view_matrix = super.getUniformLocation("view_matrix");
		location_light_position = super.getUniformLocation("light_position");
		location_light_color = super.getUniformLocation("light_color");
		location_shine_damper = super.getUniformLocation("shine_damper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_sky_color = super.getUniformLocation("sky_color");
		location_background_texture = super.getUniformLocation("background_texture");
		location_r_texture = super.getUniformLocation("r_texture");
		location_g_texture = super.getUniformLocation("g_texture");
		location_b_texture = super.getUniformLocation("b_texture");
		location_blend_map = super.getUniformLocation("blend_map");
	}

	// Loads transformation matrix to uniform variable.
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformation_matrix, matrix);
	}

	// Loads projection matrix to uniform variable.
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projection_matrix, projection);
	}

	// Loads view matrix to uniform variable.
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_view_matrix, viewMatrix);
	}

	// Loads up light uniform variables.
	public void loadLight(Light light) {
		super.loadVector(location_light_position, light.getPosition());
		super.loadVector(location_light_color, light.getColor());
	}

	// Loads up shine uniform variables.
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shine_damper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	// Loads up value to sky color uniform variable.
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(location_sky_color, new Vector3f(r, g, b));
	}

	// Loads up an Integer to each of Sampler2D's to indicate which texture units
	// they should be referencing.
	public void connectTextureUnits() {
		super.loadInt(location_background_texture, 0);
		super.loadInt(location_r_texture, 1);
		super.loadInt(location_g_texture, 2);
		super.loadInt(location_b_texture, 3);
		super.loadInt(location_blend_map, 4);
	}

}
