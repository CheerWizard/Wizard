#version 400 core

in vec4 passColor;

in vec2 passCoordinate;
in float passSlot;

out vec4 fragment;

uniform sampler2D diffuseSampler1;
uniform sampler2D diffuseSampler2;

void main() {
    vec4 diffusePixel;
    int samplerId = int(passSlot);

    switch(samplerId) {
        case 0 :
        diffusePixel = texture(diffuseSampler1, passCoordinate);
        break;
        case 1 :
        diffusePixel = texture(diffuseSampler2, passCoordinate);
        break;
    }

    fragment = diffusePixel;
}
