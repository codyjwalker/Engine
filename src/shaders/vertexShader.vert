#version 400 core

const float density = 0.003;
const float gradient = 5;

in vec3 position;
in vec2 texture_coordinates;
in vec3 normal;

out vec2 pass_texture_coordinates;
out vec3 surface_normal;
out vec3 to_light_vector[4]; // Array for all lights.
out vec3 to_camera_vector;
out float visibility;

uniform mat4 transformation_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;
uniform vec3 light_position[4]; // Array for positions of all lights.
uniform float use_fake_lighting;

// For texture atlas.
uniform int number_of_rows;
uniform vec2 offset;

void main(void) {

	vec4 world_position = transformation_matrix * vec4(position, 1.0);
	vec4 position_relative_to_cam = view_matrix * world_position;
	gl_Position = projection_matrix * position_relative_to_cam;
	pass_texture_coordinates = (texture_coordinates / number_of_rows) + offset;
	
	vec3 actual_normal = normal;
	if (use_fake_lighting > 0.5) {
		actual_normal = vec3(0.0, 1.0, 0.0);
	}
	// Calculate surface normals.
	surface_normal = (transformation_matrix * vec4(actual_normal, 0.0)).xyz;
	// Calculate vector to lights.
	for (int i = 0; i < 4; i++) {
		to_light_vector[i] = light_position[i] - world_position.xyz;
	}
	to_camera_vector = (inverse(view_matrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - world_position.xyz;

	float distance = length(position_relative_to_cam.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);

}
