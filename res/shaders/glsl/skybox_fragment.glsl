#version 400 core

in vec3 skyboxTextures;
out vec4 skyboxFragment;

uniform samplerCube skyboxCubeSampler;

void main(void) {
    skyboxFragment = texture(skyboxCubeSampler, skyboxTextures);
}