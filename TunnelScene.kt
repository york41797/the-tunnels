package com.horror.tunnels.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix

class TunnelScene(private val context: Context) {

    // Example: a single low‑poly corridor mesh
    private val corridorMesh = ObjLoader.loadFromAssets(context, "models/corridor.obj")

    private val modelMatrix = FloatArray(16)

    init {
        Matrix.setIdentityM(modelMatrix, 0)
    }

    fun update() {
        // TODO: animate flickering lights, subtle camera sway, distant sounds, etc.
    }

    fun draw(vpMatrix: FloatArray) {
    // Compute MVP, model, normal matrices
    val mvp = FloatArray(16)
    Matrix.multiplyMM(mvp, 0, vpMatrix, 0, modelMatrix, 0)

    val normalMatrix = FloatArray(9)
    // Extract upper-left 3x3 from modelMatrix and invert/transpose for normals
    // (you can simplify if you don’t scale non‑uniformly)

    GLES20.glUseProgram(program)

    // Bind buffers (positions, normals, uvs) from Mesh
    // Set uniforms: uMVPMatrix, uModelMatrix, uNormalMatrix, lights, fog, texture
    // Then draw:
    GLES20.glDrawElements(
        GLES20.GL_TRIANGLES,
        mesh.indexCount,
        GLES20.GL_UNSIGNED_INT,
        mesh.indexBuffer
    )
}
