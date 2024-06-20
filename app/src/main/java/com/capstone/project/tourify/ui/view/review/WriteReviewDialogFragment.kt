package com.capstone.project.tourify.ui.view.review

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.capstone.project.tourify.R

class WriteReviewDialogFragment : DialogFragment() {

    interface WriteReviewDialogListener {
        fun onReviewSubmitted(review: String)
    }

    private var listener: WriteReviewDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_write_review, null)

            builder.setView(view)
                .setTitle("Write a Review")
                .setPositiveButton("Submit") { _, _ ->
                    val editTextReview = view.findViewById<EditText>(R.id.editTextReview)
                    val review = editTextReview.text.toString().trim()
                    listener?.onReviewSubmitted(review)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            val dialog = builder.create()

            // Mendapatkan referensi tombol positif dan negatif
            dialog.setOnShowListener {
                val submitButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                // Mengubah warna teks tombol menjadi hitam
                submitButton.setTextColor(resources.getColor(R.color.black)) // Ganti R.color.black dengan warna yang sesuai
                cancelButton.setTextColor(resources.getColor(R.color.black)) // Ganti R.color.black dengan warna yang sesuai
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setWriteReviewDialogListener(listener: WriteReviewDialogListener) {
        this.listener = listener
    }
}
