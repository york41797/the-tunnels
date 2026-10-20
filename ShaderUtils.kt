package com.horror.tunnels.opengl

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object ShaderUtils {

    fun loadShaderFromAssets(context: Context, path: String): String {
        val inputStream = context.assets.open(path)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val builder = StringBuilder()

        reader.useLines { lines ->
            lines.forEach { builder.append(it).append("\n") }
        }

        return builder.toString()
    }
}
