#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 coordinate;
layout(location = 2) in vec4 color;
layout(location = 3) in float slot;
layout(location = 4) in mat4 transform;

out vec2 passCoordinate;
out vec4 passColor;
out float passSlot;

uniform mat4 projection;
uniform mat4 camera;

void main() {
    vec4 worldPosition = vec4(position, 1.0);
    gl_Position = projection * camera * worldPosition;

    passColor = color;
    passCoordinate = coordinate;
    passSlot = slot;
}