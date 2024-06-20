package com.capstone.project.tourify.Helper

import android.content.Context
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class RecommendationHelper(private val context: Context) {

    private var interpreter: Interpreter? = null

    init {
        loadModel()
    }

    private fun loadModel() {
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("tourify_model", DownloadType.LOCAL_MODEL, conditions)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val model: CustomModel? = task.result
                    if (model != null) {
                        val file = model.file
                        if (file != null) {
                            interpreter = Interpreter(loadModelFile(file))
                        }
                    }
                } else {
                    // Handle the failure
                }
            }
    }

    private fun loadModelFile(modelFile: File): MappedByteBuffer {
        val fileInputStream = modelFile.inputStream()
        val fileChannel = fileInputStream.channel
        val declaredLength = modelFile.length()
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, declaredLength)
    }

    fun recommend(tourismInput: Float, userInput: Float): FloatArray {
        val inputBuffer1 = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        inputBuffer1.putFloat(tourismInput)
        val inputBuffer2 = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        inputBuffer2.putFloat(userInput)

        val outputBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        interpreter?.runForMultipleInputsOutputs(arrayOf(inputBuffer1, inputBuffer2), mapOf(0 to outputBuffer))

        outputBuffer.rewind()
        val recommendations = FloatArray(1)
        outputBuffer.asFloatBuffer().get(recommendations)
        return recommendations
    }
}
