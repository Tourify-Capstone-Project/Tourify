package com.capstone.project.tourify

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.IOException

class RecommendationHelper(context: Context) {

    private var interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("tourify_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun runInference(input: FloatArray): FloatArray {
        val inputBuffer = ByteBuffer.allocateDirect(4 * input.size).order(ByteOrder.nativeOrder())
        for (value in input) {
            inputBuffer.putFloat(value)
        }

        val output = Array(1) { FloatArray(1) } // Sesuaikan ukuran array output berdasarkan model Anda
        interpreter.run(inputBuffer, output)
        Log.d("TensorFlowLite", "Output: ${output[0][0]}")
        return output[0]
    }
}
