#version 400 core

in vec3 cubeVertices;
in vec2 cubeTextures;
in vec3 cubeNormals;
in vec3 cubeTangents;

out vec2 passCubeTextures;

out vec3 passCameraVector;

out vec3 passLightVector;
out float passLightDistance;

uniform vec3 lightPosition;

uniform mat4 camera;
uniform mat4 projection3d;

uniform mat4 cubeTransform;

uniform float textureGridRowCount;
uniform vec2 textureGridOffset;

uniform vec4 clipping;

void main(void) {

    vec4 cubeWorldPosition = cubeTransform * vec4(cubeVertices, 1.0);

    gl_ClipDistance[0] = dot(cubeWorldPosition, clipping);
    gl_Position = projection3d * camera * cubeWorldPosition;

    vec3 cubeWorldNormal = normalize((cubeTransform * vec4(cubeNormals, 0.0)).xyz);
    vec3 cubeWorldTangent = normalize((cubeTransform * vec4(cubeTangents, 0.0)).xyz);
    cubeWorldTangent = normalize(cubeWorldTangent - dot(cubeWorldTangent, cubeWorldNormal) * cubeWorldNormal);
    vec3 cubeWorldBitangent = cross(cubeWorldNormal, cubeWorldTangent);
    mat3 tangentSpace = mat3(cubeWorldTangent, cubeWorldBitangent, cubeWorldNormal);

    vec3 sunLightVector = (lightPosition - cubeWorldPosition.xyz) * tangentSpace;
    passLightDistance = length(sunLightVector);
    passLightVector = normalize(sunLightVector);

    vec4 cameraPosition = inverse(camera) * vec4(0.0, 0.0, 0.0, 1.0);
    passCameraVector = normalize(cameraPosition.xyz - cubeWorldPosition.xyz) * tangentSpace;

    passCubeTextures = (cubeTextures / textureGridRowCount) + textureGridOffset;

}