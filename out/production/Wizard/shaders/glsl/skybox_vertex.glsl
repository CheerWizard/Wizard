#version 400 core

in vec3 skyboxVertices;
out vec3 skyboxTextures;

uniform mat4 projection3d;
uniform mat4 camera;

uniform mat4 skyboxTransform;

uniform vec4 clipping;

void main(void) {
    vec4 skyboxWorldPosition = skyboxTransform * vec4(skyboxVertices, 1.0);
    gl_ClipDistance[0] = dot(skyboxWorldPosition, clipping);
    gl_Position = projection3d * camera * skyboxWorldPosition;
    skyboxTextures = skyboxVertices;
}