#version 400 core

in vec3 cubeVertices;
in vec2 cubeTextures;
in vec3 cubeNormals;
in vec3 cubeTangents;

out vec2 passCubeTextures;

out vec3 passSunLightVector;
out float passSunLightDistance;

out vec3 passPointLightVector;
out float passPointLightDistance;

out vec3 passCameraVector;

uniform vec3 sunLightPosition;
uniform vec3 pointLightPosition;

uniform mat4 camera;
uniform mat4 projection3d;

uniform mat4 cubeTransform;

uniform float textureGridRowCount;
uniform vec2 textureGridOffset;

void main(void) {

    vec4 cubeWorldPosition = cubeTransform * vec4(cubeVertices, 1.0);
    gl_Position = projection3d * camera * cubeWorldPosition;

    passCubeTextures = (cubeTextures / textureGridRowCount) + textureGridOffset;

    vec3 cubeWorldNormal = normalize((cubeTransform * vec4(cubeNormals, 0.0)).xyz);
    vec3 cubeWorldTangent = normalize((cubeTransform * vec4(cubeTangents, 0.0)).xyz);
    vec3 cubeBitangent = normalize(cross(cubeWorldNormal, cubeWorldTangent));
    mat3 tangentSpace = mat3(
        cubeWorldTangent.x, cubeBitangent.x, cubeWorldNormal.x,
        cubeWorldTangent.y, cubeBitangent.y, cubeWorldNormal.y,
        cubeWorldTangent.z, cubeBitangent.z, cubeWorldNormal.z
    );

    vec3 sunLightVector = tangentSpace * (sunLightPosition - cubeWorldPosition.xyz);
    passSunLightDistance = length(sunLightVector);
    passSunLightVector = normalize(sunLightVector);

    vec3 pointLightVector = tangentSpace * (pointLightPosition - cubeWorldPosition.xyz);
    passPointLightDistance = length(pointLightVector);
    passPointLightVector = normalize(pointLightVector);

    vec4 cameraPosition = inverse(camera) * vec4(0.0, 0.0, 0.0, 1.0);
    passCameraVector = normalize(tangentSpace * (cameraPosition.xyz - cubeWorldPosition.xyz));

}