#version 400 core

in vec2 passCubeTextures;
in vec3 passCubeNormals;

// sun light
in vec3 passSunLightVector;
in float passSunLightDistance;

// point light
in vec3 passPointLightVector;
in float passPointLightDistance;

in vec3 passCameraVector;

out vec4 cubeFragment;

uniform sampler2D diffuseSampler;

// sun light
uniform vec3 sunLightColor;
uniform vec3 sunLightRadius;

// specular uniforms
uniform float shining;
uniform float reflectivity;
uniform float brightness;

void main(void) {

    float sunLightStrength = sunLightRadius.x + (sunLightRadius.y * passSunLightDistance / 10.0f) + (sunLightRadius.z * passSunLightDistance * passSunLightDistance / 100.0f);
    float sunLightDot = dot(passCubeNormals, passSunLightVector);
    vec3 sunLightDiffuse =  max(sunLightDot, brightness) * sunLightColor * sunLightStrength;
    vec3 reflectedSunLight = reflect(-passSunLightVector, passCubeNormals);
    float sunSpecularDot = dot(reflectedSunLight, passCameraVector);
    sunSpecularDot = max(sunSpecularDot, 0.0);
    float sunDamping = pow(sunSpecularDot, shining);
    vec3 sunSpecular = sunDamping * reflectivity * sunLightColor * sunLightStrength;

    vec3 totalDiffuse = sunLightDiffuse;
    vec3 totalSpecular = sunSpecular;

    vec4 textureColor = texture(diffuseSampler, passCubeTextures);

    cubeFragment = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);

}
