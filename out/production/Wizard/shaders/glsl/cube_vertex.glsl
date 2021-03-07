#version 400 core
// mesh buffer
in vec3 position;
in vec3 normal;
// material buffer
in vec2 coordinate;

out vec2 passCubeTextures;
out vec3 passCubeNormals;

out vec3 passSunLightVector;
out float passSunLightDistance;

out vec3 passPointLightVector;
out float passPointLightDistance;

out vec3 passCameraVector;

uniform vec3 sunLightPosition;

uniform vec3 pointLightPosition;

uniform mat4 camera;
uniform mat4 projection3d;

uniform float textureGridRowCount;
uniform vec2 textureGridOffset;

void main(void) {

	gl_Position = projection3d * camera * vec4(position, 1.0);

	passCubeTextures = coordinate;

	passCubeNormals = normalize(normal);

	vec3 sunLightVector = sunLightPosition - cubeWorldPosition.xyz;
	passSunLightDistance = length(sunLightVector);
	passSunLightVector = normalize(sunLightVector);

	vec3 pointLightVector = pointLightPosition - cubeWorldPosition.xyz;
	passPointLightDistance = length(pointLightVector);
	passPointLightVector = normalize(pointLightVector);

	vec4 cameraPosition = (inverse(camera) * vec4(0.0, 0.0, 0.0, 1.0));
	passCameraVector = normalize(cameraPosition.xyz - cubeWorldPosition.xyz);

}