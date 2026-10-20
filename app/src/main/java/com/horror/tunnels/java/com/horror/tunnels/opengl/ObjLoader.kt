package com.horror.tunnels.opengl.loader

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

data class Mesh(
    val vertexBuffer: FloatBuffer,
    val normalBuffer: FloatBuffer?,
    val uvBuffer: FloatBuffer?,
    val indexBuffer: IntBuffer,
    val indexCount: Int
)

object ObjLoader {

    fun loadFromAssets(context: Context, assetPath: String): Mesh {
        val positions = mutableListOf<Float>()
        val normals = mutableListOf<Float>()
        val uvs = mutableListOf<Float>()

        val positionIndices = mutableListOf<Int>()
        val normalIndices = mutableListOf<Int>()
        val uvIndices = mutableListOf<Int>()

        val inputStream = context.assets.open(assetPath)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.forEach { line ->
                val parts = line.trim().split("\\s+".toRegex())
                if (parts.isEmpty()) return@forEach

                when (parts[0]) {
                    "v" -> {
                        positions.add(parts[1].toFloat())
                        positions.add(parts[2].toFloat())
                        positions.add(parts[3].toFloat())
                    }
                    "vn" -> {
                        normals.add(parts[1].toFloat())
                        normals.add(parts[2].toFloat())
                        normals.add(parts[3].toFloat())
                    }
                    "vt" -> {
                        uvs.add(parts[1].toFloat())
                        uvs.add(parts[2].toFloat())
                    }
                    "f" -> {
                        // Assume triangles: f v/vt/vn v/vt/vn v/vt/vn
                        for (i in 1..3) {
                            val comps = parts[i].split("/")
                            val vIndex = comps[0].toInt() - 1
                            positionIndices.add(vIndex)

                            if (comps.size > 1 && comps[1].isNotEmpty()) {
                                val vtIndex = comps[1].toInt() - 1
                                uvIndices.add(vtIndex)
                            }
                            if (comps.size > 2 && comps[2].isNotEmpty()) {
                                val vnIndex = comps[2].toInt() - 1
                                normalIndices.add(vnIndex)
                            }
                        }
                    }
                }
            }
        }

        // Flatten into final arrays (simple, not optimized)
        val finalPositions = FloatArray(positionIndices.size * 3)
        val finalNormals =
            if (normals.isNotEmpty()) FloatArray(positionIndices.size * 3) else null
        val finalUvs =
            if (uvs.isNotEmpty()) FloatArray(positionIndices.size * 2) else null

        for (i in positionIndices.indices) {
            val pi = positionIndices[i] * 3
            finalPositions[i * 3] = positions[pi]
            finalPositions[i * 3 + 1] = positions[pi + 1]
            finalPositions[i * 3 + 2] = positions[pi + 2]

            if (finalNormals != null && normalIndices.size == positionIndices.size) {
                val ni = normalIndices[i] * 3
                finalNormals[i * 3] = normals[ni]
                finalNormals[i * 3 + 1] = normals[ni + 1]
                finalNormals[i * 3 + 2] = normals[ni + 2]
            }

            if (finalUvs != null && uvIndices.size == positionIndices.size) {
                val ti = uvIndices[i] * 2
                finalUvs[i * 2] = uvs[ti]
                finalUvs[i * 2 + 1] = uvs[ti + 1]
            }
        }

        val vertexBuffer = floatBufferOf(finalPositions)
        val normalBuffer = finalNormals?.let { floatBufferOf(it) }
        val uvBuffer = finalUvs?.let { floatBufferOf(it) }

        val indices = IntArray(positionIndices.size) { it }
        val indexBuffer = intBufferOf(indices)

        return Mesh(
            vertexBuffer = vertexBuffer,
            normalBuffer = normalBuffer,
            uvBuffer = uvBuffer,
            indexBuffer = indexBuffer,
            indexCount = indices.size
        )
    }

    private fun floatBufferOf(data: FloatArray): FloatBuffer =
        ByteBuffer.allocateDirect(data.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(data)
                position(0)
            }

    private fun intBufferOf(data: IntArray): IntBuffer =
        ByteBuffer.allocateDirect(data.size * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
            .apply {
                put(data)
                position(0)
            }
}
