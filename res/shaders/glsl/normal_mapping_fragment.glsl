#version 400 core

in vec2 passCubeTextures;

// sun light
in vec3 passSunLightVector;
in float passSunLightDistance;

// point light
in vec3 passPointLightVector;
in float passPointLightDistance;

in vec3 passCameraVector;

out vec4 cubeFragment;

uniform sampler2D textureSampler;
uniform sampler2D normalsSampler;
//uniform sampler2D textureSampler2;

// sun light
uniform vec3 sunLightColor;
uniform vec3 sunLightRadius;

// specular uniforms
uniform float shining;
uniform float reflectivity;
uniform float brightness;

void main(void) {

    vec4 normalsMap = 2.0 * texture(normalsSampler, passCubeTextures) - 1.0;
    vec3 cubeNormal = normalize(normalsMap.rgb);

    float sunLightStrength = sunLightRadius.x + (sunLightRadius.y * passSunLightDistance / 10.0f) + (sunLightRadius.z * passSunLightDistance * passSunLightDistance / 100.0f);
    float sunLightDot = dot(cubeNormal, passSunLightVector);
    vec3 sunLightDiffuse =  max(sunLightDot, brightness) * sunLightColor * sunLightStrength;
    vec3 reflectedSunLight = reflect(-passSunLightVector, cubeNormal);
    float sunSpecularDot = dot(reflectedSunLight, passCameraVector);
    sunSpecularDot = max(sunSpecularDot, 0.0);
    float sunDamping = pow(sunSpecularDot, shining);
    vec3 sunLightSpecular = sunDamping * reflectivity * sunLightColor * sunLightStrength;

    vec3 diffuseLight = sunLightDiffuse;
    vec3 specularLight = sunLightSpecular;

    vec4 textureColor = texture(textureSampler, passCubeTextures);
    if (textureColor.a < 0.5 || textureColor.r == 0 && textureColor.g == 0 && textureColor.b == 0) {
        discard;
    }

    cubeFragment = vec4(diffuseLight, 1.0) * textureColor + vec4(specularLight, 1.0);

}
