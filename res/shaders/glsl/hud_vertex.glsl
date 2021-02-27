#version 400 core

in vec3 hudVertices;
in vec2 hudTextures;

out vec2 passTextures;

uniform mat4 camera;
uniform mat4 projection2d;

uniform mat4 hudTransform;

void main(void)
{
    vec4 hudWorldPosition = hudTransform * vec4(hudVertices, 1.0);
    gl_Position = projection2d * camera * hudWorldPosition;
    passTextures = hudTextures;
}