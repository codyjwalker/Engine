package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.vert";
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.frag";
	private static final int MAX_LIGHTS = 4;

	private int location_transformation_matrix;
	private int location_projection_matrix;
	private int location_view_matrix;
	private int location_light_position[];
	private int location_light_color[];
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
		this.location_transformation_matrix = super.getUniformLocation(
				"transformation_matrix");
		this.location_projection_matrix = super.getUniformLocation(
				"projection_matrix");
		this.location_view_matrix = super.getUniformLocation("view_matrix");
		this.location_shine_damper = super.getUniformLocation("shine_damper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_sky_color = super.getUniformLocation("sky_color");
		this.location_background_texture = super.getUniformLocation(
				"background_texture");
		this.location_r_texture = super.getUniformLocation("r_texture");
		this.location_g_texture = super.getUniformLocation("g_texture");
		this.location_b_texture = super.getUniformLocation("b_texture");
		this.location_blend_map = super.getUniformLocation("blend_map");

		this.location_light_position = new int[MAX_LIGHTS];
		this.location_light_color = new int[MAX_LIGHTS];
		// Get location of each of elements in uniform arrays & store in int
		// arrays.
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_light_position[i] = super.getUniformLocation(
					"light_position[" + i + "]");
			location_light_color[i] = super.getUniformLocation(
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
	public void loadLight(List<Light> lights) {
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

	// Loads up value to sky color uniform variable.
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(this.location_sky_color, new Vector3f(r, g, b));
	}

	// Loads up an Integer to each of Sampler2D's to indicate which texture
	// units
	// they should be referencing.
	public void connectTextureUnits() {
		super.loadInt(this.location_background_texture, 0);
		super.loadInt(this.location_r_texture, 1);
		super.loadInt(this.location_g_texture, 2);
		super.loadInt(this.location_b_texture, 3);
		super.loadInt(this.location_blend_map, 4);
	}

}
