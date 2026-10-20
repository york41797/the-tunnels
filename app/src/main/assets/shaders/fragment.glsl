precision mediump float;

varying vec2 vTexCoord;
varying vec3 vLightColor;
varying float vFogFactor;

uniform sampler2D uTexture;
uniform vec3 uFogColor; // deep black/blue

void main() {
    vec4 texColor = texture2D(uTexture, vTexCoord);

    // Slightly desaturate for grim look
    float grey = (texColor.r + texColor.g + texColor.b) / 3.0;
    vec3 desat = mix(texColor.rgb, vec3(grey), 0.4);

    vec3 litColor = desat * vLightColor;

    vec3 finalColor = mix(uFogColor, litColor, vFogFactor);
    gl_FragColor = vec4(finalColor, texColor.a);
}
