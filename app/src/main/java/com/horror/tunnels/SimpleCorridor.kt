package com.horror.tunnels.opengl

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class SimpleCorridor {

    // Very rough quad strip as a placeholder
    private val vertices = floatArrayOf(
        // floor
        -2f, 0f, -10f,
         2f, 0f, -10f,
        -2f, 0f,  0f,
         2f, 0f,  0f,

        // ceiling
        -2f, 2f, -10f,
         2f, 2f, -10f,
        -2f, 2f,  0f,
         2f, 2f,  0f
    )

    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(vertices)
                position(0)
            }

    // Simple flat color shader (you’ll replace with textured, lit shaders)
    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 aPosition;" +
        "void main() {" +
        "  gl_Position = uMVPMatrix * aPosition;" +
        "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 uColor;" +
        "void main() {" +
        "  gl_FragColor = uColor;" +
        "}"

    private val program: Int

    init {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        val positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        val colorHandle = GLES20.glGetUniformLocation(program, "uColor")
        val mvpHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            3 * 4,
            vertexBuffer
        )

        // Damp, sickly greenish light
        GLES20.glUniform4f(colorHandle, 0.3f, 0.5f, 0.3f, 1.0f)
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0)

        // Draw as triangle strip (you’ll refine topology later)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertices.size / 3)

        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}
