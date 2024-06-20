package com.capstone.project.tourify.Helper

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import kotlin.math.absoluteValue

class RecommendationHelper(context: Context) {

    private var interpreter: Interpreter? = null

    init {
        val modelFile = File(context.filesDir, "tourify_model.tflite")
        if (modelFile.exists()) {
            interpreter = Interpreter(loadModelFile(modelFile))
        } else {
            Log.e("RecommendationHelper", "Model file not found.")
        }
    }

    private fun loadModelFile(modelFile: File): ByteBuffer {
        val fileInputStream = FileInputStream(modelFile)
        val fileChannel = fileInputStream.channel
        val declaredLength = modelFile.length()
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, declaredLength)
    }

    fun recommend(tourismInput: Float, userInput: Float): FloatArray? {
        return try {
            val inputBuffer1 = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
            inputBuffer1.putFloat(tourismInput)
            val inputBuffer2 = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
            inputBuffer2.putFloat(userInput)

            val outputBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
            interpreter?.runForMultipleInputsOutputs(arrayOf(inputBuffer1, inputBuffer2), mapOf(0 to outputBuffer))

            outputBuffer.rewind()
            val recommendations = FloatArray(1)
            outputBuffer.asFloatBuffer().get(recommendations)
            recommendations
        } catch (e: IllegalArgumentException) {
            Log.e("RecommendationHelper", "Error during model inference: ${e.message}")
            null
        }
    }
}
