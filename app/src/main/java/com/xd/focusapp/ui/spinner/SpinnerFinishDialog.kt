package com.xd.focusapp.ui.spinner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.xd.focusapp.R

class SpinnerFinishDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog:Dialog
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.spinner_finish_dialog, null)
        builder.setView(view)

        val bundle = arguments
        val image = bundle?.getInt("image")

        val showImage = view.findViewById<ImageView>(R.id.image_show)
        showImage.setImageResource(image!!)

        builder.setPositiveButton("ok"){ dialog, which ->
            dismiss()
        }

        dialog = builder.create()



        return dialog
    }
}