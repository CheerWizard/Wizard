#version 400 core

in vec3 position;
in vec4 color;

out vec4 passColor;

uniform mat4 projection2d;

void main() {
    vec4 worldPosition = vec4(position, 1.0);
    gl_Position = projection2d * worldPosition;

    passColor = color;
}
