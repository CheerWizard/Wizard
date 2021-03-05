#version 400 core

in vec2 passCubeTextures;

in vec3 passLightVector;
in float passLightDistance;

in vec3 passCameraVector;

out vec4 cubeFragment;

uniform sampler2D diffuseSampler1;
uniform sampler2D diffuseSampler2;
uniform sampler2D normalSampler;
uniform sampler2D specularSampler;
uniform sampler2D parallaxSampler;

uniform vec3 lightColor;
uniform vec3 lightRadius;

// specular uniforms
uniform float shining;
uniform float reflectivity;
uniform float brightness;

uniform float parallaxOffset;

// texture strength;
uniform float diffuseStrength1;
uniform float diffuseStrength2;
uniform float normalStrength;
uniform float specularStrength;
uniform float parallaxStrength;

void main(void) {
    // sample parallax map
    vec3 parallaxMap = texture(parallaxSampler, passCubeTextures).rgb * parallaxStrength + parallaxOffset;
    vec2 cubeTextures = passCubeTextures + passCameraVector.xy * (parallaxMap.r + parallaxMap.g + parallaxMap.b);
    // sample diffuse map
    vec4 diffuseMap1 = texture(diffuseSampler1, cubeTextures) * diffuseStrength1;
    vec4 diffuseMap2 = texture(diffuseSampler2, cubeTextures) * diffuseStrength2;
    vec4 diffuseMap = diffuseMap1;
    // sample normal map
    vec4 normalsMap = 2.0 * texture(normalSampler, cubeTextures) * normalStrength - 1.0;
    vec3 cubeNormal = normalize(normalsMap.rgb);
    // sample specular map
    vec3 specularMap = texture(specularSampler, cubeTextures).rgb * specularStrength;

    // calculate diffuse light
    float sunLightStrength = lightRadius.x + (lightRadius.y * passLightDistance / 10.0f) + (lightRadius.z * passLightDistance * passLightDistance / 100.0f);
    float sunLightDot = dot(cubeNormal, passLightVector);
    vec3 sunLightDiffuse =  max(sunLightDot, brightness) * lightColor * sunLightStrength;
    // calculate specular light
    vec3 reflectedSunLight = reflect(-passLightVector, cubeNormal);
    float sunSpecularDot = dot(reflectedSunLight, passCameraVector);
    sunSpecularDot = max(sunSpecularDot, 0.0);
    float sunDamping = pow(sunSpecularDot, shining);
    vec3 sunLightSpecular = sunDamping * reflectivity * lightColor * sunLightStrength * specularMap;

    // pixel output
    cubeFragment = vec4(sunLightDiffuse, 1.0) * diffuseMap + vec4(sunLightSpecular, 1.0);
}
