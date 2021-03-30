#version 400 core

layout(location = 0) in vec3 position;
layout(location = 5) in mat4 transform;
layout(location = 2) in vec2 coordinate;
layout(location = 3) in vec4 color;
layout(location = 4) in float slot;

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