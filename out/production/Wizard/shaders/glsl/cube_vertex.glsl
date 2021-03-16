#version 400 core
// mesh buffer
in vec3 position;
in vec3 normal;
// material buffer
in vec2 coordinate;
in vec4 color;

out vec2 passCubeTextures;
out vec3 passCubeNormals;

out vec3 passLightVector;
out float passLightDistance;

out vec3 passCameraVector;

out vec4 passColor;

uniform mat4 camera;
uniform mat4 projection3d;

uniform float textureGridRowCount;
uniform vec2 textureGridOffset;

uniform vec3 lightPosition;

void main(void) {

	vec4 cubeWorldPosition = vec4(position, 1.0);
	gl_Position = projection3d * camera * cubeWorldPosition;
	passColor = color;

	passCubeTextures = coordinate;

	passCubeNormals = normalize(normal);

	vec3 sunLightVector = lightPosition - cubeWorldPosition.xyz;
	passLightDistance = length(sunLightVector);
	passLightVector = normalize(sunLightVector);

	vec4 cameraPosition = (inverse(camera) * vec4(0.0, 0.0, 0.0, 1.0));
	passCameraVector = normalize(cameraPosition.xyz - cubeWorldPosition.xyz);

}