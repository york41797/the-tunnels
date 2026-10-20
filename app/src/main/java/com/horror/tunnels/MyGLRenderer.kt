package com.horror.tunnels.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(private val context: Context) : GLSurfaceView.Renderer {

    // Projection and view matrices
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val vpMatrix = FloatArray(16)

    // Simple camera
    private val eye = floatArrayOf(0f, 1.5f, 5f)
    private val center = floatArrayOf(0f, 1.0f, 0f)
    private val up = floatArrayOf(0f, 1f, 0f)

    // Example: a placeholder “room” object you’ll replace with real meshes
    private lateinit var tunnelScene: TunnelScene

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
    // Dark, atmospheric clear color
    GLES20.glClearColor(0.02f, 0.02f, 0.05f, 1.0f)

    // Enable depth testing for 3D
    GLES20.glEnable(GLES20.GL_DEPTH_TEST)

    // 1. Load shader text from assets
    val vertexShaderCode =
        ShaderUtils.loadShaderFromAssets(context, "shaders/vertex.glsl")

    val fragmentShaderCode =
        ShaderUtils.loadShaderFromAssets(context, "shaders/fragment.glsl")

    // 2. Compile + link shaders using your ShaderProgram class
    val shaderProgram = ShaderProgram(vertexShaderCode, fragmentShaderCode)

    // 3. Pass shader program into your scene
    tunnelScene = TunnelScene(context, shaderProgram)
}


    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(
            projectionMatrix,
            0,
            -ratio,
            ratio,
            -1f,
            1f,
            1.5f,
            100f
        )
    }

    override fun onDrawFrame(unused: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Basic camera
        Matrix.setLookAtM(
            viewMatrix,
            0,
            eye[0], eye[1], eye[2],
            center[0], center[1], center[2],
            up[0], up[1], up[2]
        )

        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Update and draw your horror scene
        tunnelScene.update()
        tunnelScene.draw(vpMatrix)
    }
}
