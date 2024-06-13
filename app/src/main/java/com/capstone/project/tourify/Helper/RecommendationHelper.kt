import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class RecommendationHelper(context: Context) {

    private var interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("recommendation_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun runInference(user_id: Int, tourism_id: Int): Float {
        val inputBuffer = ByteBuffer.allocateDirect(4 * 2).order(ByteOrder.nativeOrder())
        inputBuffer.putFloat(user_id.toFloat())
        inputBuffer.putFloat(tourism_id.toFloat())

        val outputBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        interpreter.run(inputBuffer, outputBuffer)

        outputBuffer.rewind()
        return outputBuffer.float
    }
}
