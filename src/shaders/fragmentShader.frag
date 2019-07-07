#version 400 core


in vec2 pass_texture_coordinates;
in vec3 surface_normal;
in vec3 to_light_vector[4];
in vec3 to_camera_vector;
in float visibility;

out vec4 out_color;

uniform sampler2D texture_sampler;
uniform vec3 light_color[4];
uniform vec3 attenuation[4];
uniform float shine_damper;
uniform float reflectivity;
uniform vec3 sky_color;

void main(void) {

	vec3 unit_normal = normalize(surface_normal);
	// Camera calculations.
	vec3 unit_to_camera_vector = normalize(to_camera_vector);
	// Get sums of diffuse and secular lighting from each of the light sources.
	vec3 total_diffuse = vec3(0.0);
	vec3 total_specular = vec3(0.0);

	// Lighting calculations.
	for (int i = 0; i < 4; i++) {
		float distance = length(to_light_vector[i]);
		float att_factor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unit_light_vector = normalize(to_light_vector[i]);
		float norm_dot_light = dot(unit_normal, unit_light_vector);
		float brightness = max(norm_dot_light, 0.0);
		vec3 light_direction = -unit_light_vector;
		vec3 reflected_light_direction = reflect(light_direction, unit_normal);
		float specular_factor = dot(reflected_light_direction, unit_to_camera_vector);
		specular_factor = max(specular_factor, 0.0);
		float damped_factor = pow(specular_factor, shine_damper);
		// Add this light's diffuse & specular to total diffuse.
		total_diffuse = total_diffuse + (brightness * light_color[i]) / att_factor;
		total_specular = total_specular + (damped_factor * reflectivity * light_color[i]) / att_factor;

	}
	// Ambient lighting calculation outside loop.
	total_diffuse = max(total_diffuse, 0.2);

	vec4 texture_color = texture(texture_sampler, pass_texture_coordinates);

	if (texture_color.a < 0.5) {
		discard;
	}

	out_color = vec4(total_diffuse, 1.0) * texture_color + vec4(total_specular, 1.0);
	out_color = mix(vec4(sky_color, 1.0), out_color, visibility);

}
