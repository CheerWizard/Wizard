#version 400 core

in vec3 position;
in vec4 color;
in mat4 transformation;

out vec4 passColor;

uniform mat4 projection;

void main() {
    vec4 worldPosition = transformation * vec4(position, 1.0);
    gl_Position = projection * worldPosition;

    passColor = color;
}
