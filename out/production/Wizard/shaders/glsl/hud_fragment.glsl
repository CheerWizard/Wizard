#version 400 core

in vec2 passTextures;

out vec4 hudFragment;

uniform sampler2D hudTextureSampler;
uniform vec4 hudColor;

void main(void)
{
    hudFragment = hudColor * texture(hudTextureSampler, passTextures);
}