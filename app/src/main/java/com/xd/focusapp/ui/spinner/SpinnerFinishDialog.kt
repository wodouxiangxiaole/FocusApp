package com.xd.focusapp.ui.spinner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.xd.focusapp.R

class SpinnerFinishDialog: DialogFragment() {
    companion object{
        var REPEAT_KEY = 100
        var NORMAL_KEY = 200

        var COMMON = 200
        var UNCOMMON = 400
        var RARE = 800
        var LEGENDARY = 1600
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog:Dialog
        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.spinner_finish_dialog, null)
        builder.setView(view)

        val bundle = arguments
        val image = bundle?.getInt("image")

        val showImage = view.findViewById<ImageView>(R.id.image_show)
        showImage.setImageResource(image!!)

        if(bundle.getInt("key") == REPEAT_KEY){
            view.findViewById<TextView>(R.id.dialog_title).text =
                "You already have this plant, automatically converted to points"

            // add points to credit
            val points = bundle.getInt("points")

            // update credits in database
        }

        builder.setPositiveButton("ok"){ dialog, which ->
            dismiss()
        }

        dialog = builder.create()



        return dialog
    }
}