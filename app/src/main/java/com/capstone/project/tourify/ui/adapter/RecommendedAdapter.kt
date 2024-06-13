package com.capstone.project.tourify.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RecommendedAdapter(
    private val data: List<RecommendedItem>,
    private val tflite: Interpreter,
    private val onItemClickListener: (RecommendedItem) -> Unit
) : RecyclerView.Adapter<RecommendedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.tvTitle)
        val imageTitle: ImageView = view.findViewById(R.id.imagePoster)

        fun bind(recommendedItem: RecommendedItem) {
            textViewTitle.text = recommendedItem.title
            imageTitle.setImageResource(recommendedItem.imageResId)

            // Contoh penggunaan model TensorFlow Lite untuk prediksi
            val inputBuffer = createInputBuffer(recommendedItem)
            val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)

            try {
                tflite.run(inputBuffer, outputBuffer.buffer)
                // Gunakan outputBuffer untuk mendapatkan hasil prediksi dan melakukan sesuatu dengan hasil tersebut
                val prediction = outputBuffer.floatArray[0]
                Log.d("TensorFlowLite", "Prediction: $prediction")

                itemView.setOnClickListener {
                    onItemClickListener(recommendedItem)
                    // Misalnya, tampilkan prediksi sebagai Toast
                    Toast.makeText(itemView.context, "Prediction: $prediction", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("TensorFlowLite", "Error running model", e)
            }
        }

        private fun createInputBuffer(recommendedItem: RecommendedItem): ByteBuffer {
            // Contoh membuat input buffer, sesuaikan dengan model Anda
            val inputBuffer = ByteBuffer.allocateDirect(4 * 1) // misalnya untuk 1 input float
            inputBuffer.order(ByteOrder.nativeOrder())
            val inputValue = 0.0f // ganti dengan nilai input yang relevan
            inputBuffer.putFloat(inputValue)
            Log.d("TensorFlowLite", "Input value: $inputValue")
            return inputBuffer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommended, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}

data class RecommendedItem(val title: String, val imageResId: Int)