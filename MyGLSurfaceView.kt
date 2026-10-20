package com.horror.tunnels.opengl

import android.content.Context
import android.opengl.GLSurfaceView

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer

    init {
        // OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer(context)

        // Set renderer
        setRenderer(renderer)

        // Render continuously (for a game)
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}
