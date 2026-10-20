package com.horror.tunnels.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix

class TunnelScene(
    private val context: Context,
    private val shaderProgram: ShaderProgram
) {

    // Load your corridor OBJ mesh
    private val corridorMesh = ObjLoader.loadFromAssets(context, "models/corridor.obj")

    private val modelMatrix = FloatArray(16)

    init {
        Matrix.setIdentityM(modelMatrix, 0)
    }

    fun update() {
        // TODO: flickering lights, subtle camera sway, distant sounds, etc.
    }

    fun draw(vpMatrix: FloatArray) {

        // --- Compute matrices ---
        val mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix, 0)

        // Normal matrix (upper-left 3x3 of modelMatrix)
        val normalMatrix = FloatArray(9)
        normalMatrix[0] = modelMatrix[0]
        normalMatrix[1] = modelMatrix[1]
        normalMatrix[2] = modelMatrix[2]
        normalMatrix[3] = modelMatrix[4]
        normalMatrix[4] = modelMatrix[5]
        normalMatrix[5] = modelMatrix[6]
        normalMatrix[6] = modelMatrix[8]
        normalMatrix[7] = modelMatrix[9]
        normalMatrix[8] = modelMatrix[10]

        // --- Use shader program ---
        GLES20.glUseProgram(shaderProgram.program)

        // --- Bind vertex positions ---
        val posHandle = GLES20.glGetAttribLocation(shaderProgram.program, "aPosition")
        GLES20.glEnableVertexAttribArray(posHandle)
        GLES20.glVertexAttribPointer(
            posHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            3 * 4,
            corridorMesh.vertexBuffer
        )

        // --- Bind normals (if present) ---
        corridorMesh.normalBuffer?.let {
            val normHandle = GLES20.glGetAttribLocation(shaderProgram.program, "aNormal")
            GLES20.glEnableVertexAttribArray(normHandle)
            GLES20.glVertexAttribPointer(
                normHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                it
            )
        }

        // --- Bind UVs (if present) ---
        corridorMesh.uvBuffer?.let {
            val uvHandle = GLES20.glGetAttribLocation(shaderProgram.program, "aTexCoord")
            GLES20.glEnableVertexAttribArray(uvHandle)
            GLES20.glVertexAttribPointer(
                uvHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                2 * 4,
                it
            )
        }

        // --- Set uniforms ---
        val mvpHandle = GLES20.glGetUniformLocation(shaderProgram.program, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0)

        val modelHandle = GLES20.glGetUniformLocation(shaderProgram.program, "uModelMatrix")
        GLES20.glUniformMatrix4fv(modelHandle, 1, false, modelMatrix, 0)

        val normalHandle = GLES20.glGetUniformLocation(shaderProgram.program, "uNormalMatrix")
        GLES20.glUniformMatrix3fv(normalHandle, 1, false, normalMatrix, 0)

        // TODO: set fog uniforms, light uniforms, texture sampler, etc.

        // --- Draw the mesh ---
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            corridorMesh.indexCount,
            GLES20.GL_UNSIGNED_INT,
            corridorMesh.indexBuffer
        )

        // --- Disable attributes ---
        GLES20.glDisableVertexAttribArray(posHandle)
        corridorMesh.normalBuffer?.let {
            val normHandle = GLES20.glGetAttribLocation(shaderProgram.program, "aNormal")
            GLES20.glDisableVertexAttribArray(normHandle)
        }
        corridorMesh.uvBuffer?.let {
            val uvHandle = GLES20.glGetAttribLocation(shaderProgram.program, "aTexCoord")
            GLES20.glDisableVertexAttribArray(uvHandle)
        }
    }
}
