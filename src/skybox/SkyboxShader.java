package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.vert";
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.frag";
	private static final float ROTATION_SPEED = 1f;

	private int location_projectionMatrix, location_viewMatrix,
			location_fog_color, location_cube_map, location_cube_map2,
			location_blend_factor;
	private float rotation;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		this.rotation = 0;
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(this.location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		this.rotation += ROTATION_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(this.rotation),
				new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(this.location_viewMatrix, matrix);
	}

	public void loadFogColor(float r, float g, float b) {
		super.loadVector(this.location_fog_color, new Vector3f(r, g, b));
	}

	public void connectTextureUnits() {
		super.loadInt(this.location_cube_map, 0);
		super.loadInt(this.location_cube_map2, 1);
	}

	public void loadBlendFactor(float blend) {
		super.loadFloat(this.location_blend_factor, blend);
	}

	@Override
	protected void getAllUniformLocations() {
		this.location_projectionMatrix = super.getUniformLocation(
				"projection_matrix");
		this.location_viewMatrix = super.getUniformLocation("view_matrix");
		this.location_fog_color = super.getUniformLocation("fog_color");
		this.location_cube_map = super.getUniformLocation("cube_map");
		this.location_cube_map2 = super.getUniformLocation("cube_map2");
		this.location_blend_factor = super.getUniformLocation("blend_factor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}