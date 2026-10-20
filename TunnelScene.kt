package com.horror.tunnels.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix

class TunnelScene(private val context: Context) {

    // Example: a single low‑poly corridor mesh
    private val corridor = SimpleCorridor()

    private val modelMatrix = FloatArray(16)

    init {
        Matrix.setIdentityM(modelMatrix, 0)
    }

    fun update() {
        // TODO: animate flickering lights, subtle camera sway, distant sounds, etc.
    }

    fun draw(vpMatrix: FloatArray) {
        val mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix, 0)

        corridor.draw(mvpMatrix)
    }
}
