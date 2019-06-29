#version 400 core

in vec2 pass_texture_coordinates;
in vec3 surface_normal;
in vec3 to_light_vector;
in vec3 to_camera_vector;
in float visibility;

out vec4 out_color;

uniform sampler2D background_texture;
uniform sampler2D r_texture;
uniform sampler2D g_texture;
uniform sampler2D b_texture;
uniform sampler2D blend_map;

uniform vec3 light_color;
uniform float shine_damper;
uniform float reflectivity;
uniform vec3 sky_color;

void main(void) {

	vec4 blend_map_color = texture(blend_map, pass_texture_coordinates);

	float back_texture_amount = 1 - (blend_map_color.r + blend_map_color.g + blend_map_color.b);
	vec2 tiled_coordinates = pass_texture_coordinates * 40.0;
	vec4 background_texture_color = texture(background_texture, tiled_coordinates) * back_texture_amount;
	vec4 r_texture_color = texture(r_texture, tiled_coordinates) * blend_map_color.r;
	vec4 g_texture_color = texture(g_texture, tiled_coordinates) * blend_map_color.g;
	vec4 b_texture_color = texture(b_texture, tiled_coordinates) * blend_map_color.b;

	vec4 total_color = background_texture_color + r_texture_color + g_texture_color + b_texture_color;

	vec3 unit_normal = normalize(surface_normal);
	vec3 unit_light_vector = normalize(to_light_vector);
	
	float norm_dot_light = dot(unit_normal, unit_light_vector);
	float brightness = max(norm_dot_light, 0.2);
	vec3 diffuse = brightness * light_color;
	
	vec3 unit_to_camera_vector = normalize(to_camera_vector);
	vec3 light_direction = -unit_light_vector;
	vec3 reflected_light_direction = reflect(light_direction, unit_normal);
	
	float specular_factor = dot(reflected_light_direction, unit_to_camera_vector);
	specular_factor = max(specular_factor, 0.0);
	float damped_factor = pow(specular_factor, shine_damper);
	vec3 final_specular = damped_factor * reflectivity * light_color;

	out_color = vec4(diffuse, 1.0) * total_color + vec4(final_specular, 1.0);
	out_color = mix(vec4(sky_color, 1.0), out_color, visibility);
	

}
