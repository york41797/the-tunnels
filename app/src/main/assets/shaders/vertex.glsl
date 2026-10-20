uniform mat4 uMVPMatrix;
uniform mat4 uModelMatrix;
uniform mat3 uNormalMatrix;

attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;
varying vec3 vLightColor;
varying float vFogFactor;

uniform vec3 uLightDirection;   // world space, normalized
uniform vec3 uLightColor;       // e.g. sickly green
uniform vec3 uAmbientColor;     // dark blue/grey
uniform float uFogStart;
uniform float uFogEnd;

void main() {
    vec4 worldPos = uModelMatrix * vec4(aPosition, 1.0);
    vec3 normal = normalize(uNormalMatrix * aNormal);

    float ndotl = max(dot(normal, -uLightDirection), 0.0);
    vec3 diffuse = uLightColor * ndotl;
    vLightColor = diffuse + uAmbientColor;

    float dist = length(worldPos.xyz);
    vFogFactor = clamp((uFogEnd - dist) / (uFogEnd - uFogStart), 0.0, 1.0);

    vTexCoord = aTexCoord;
    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
}
