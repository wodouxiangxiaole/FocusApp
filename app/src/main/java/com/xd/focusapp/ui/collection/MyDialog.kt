package com.xd.focusapp.ui.collection

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.xd.focusapp.R

class MyDialog: DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog:Dialog

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("The plant is locked. You can go spinner wheel or set focus time to get it")
//        val input = TextView(requireActivity())
//        input.text = "The plant is locked. You can go spinner wheel or set focus time to get it"
//        builder.setView(input)

        builder.setPositiveButton("ok"){ dialog, which ->
            dismiss()
        }

        dialog = builder.create()

        return dialog
    }
}