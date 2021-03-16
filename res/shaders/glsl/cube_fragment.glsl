#version 400 core

in vec4 passColor;
in vec2 passCubeTextures;
in vec3 passCubeNormals;

in vec3 passLightVector;
in float passLightDistance;

in vec3 passCameraVector;

out vec4 cubeFragment;

uniform sampler2D diffuseSampler;

uniform vec3 lightColor;
uniform vec3 lightRadius;

// specular uniforms
uniform float shining;
uniform float reflectivity;
uniform float brightness;

void main(void) {

    float sunLightStrength = lightRadius.x + (lightRadius.y * passLightDistance / 10.0f) + (lightRadius.z * passLightDistance * passLightDistance / 100.0f);
    float sunLightDot = dot(passCubeNormals, passLightVector);
    vec3 sunLightDiffuse =  max(sunLightDot, brightness) * lightColor * sunLightStrength;
    vec3 reflectedSunLight = reflect(-passLightVector, passCubeNormals);
    float sunSpecularDot = dot(reflectedSunLight, passCameraVector);
    sunSpecularDot = max(sunSpecularDot, 0.0);
    float sunDamping = pow(sunSpecularDot, shining);
    vec3 sunSpecular = sunDamping * reflectivity * lightColor * sunLightStrength;

    vec3 totalDiffuse = sunLightDiffuse;
    vec3 totalSpecular = sunSpecular;

    vec4 textureColor = texture(diffuseSampler, passCubeTextures);
    vec4 mixedColor = textureColor * passColor;

    cubeFragment = vec4(totalDiffuse, 1.0) * mixedColor + vec4(totalSpecular, 1.0);

}
