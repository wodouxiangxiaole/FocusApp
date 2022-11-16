package com.xd.focusapp.ui.focus

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class TimePickerFragment : DialogFragment(), DialogInterface.OnClickListener  {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog: Dialog

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Duration")
        // Set up the input
        val input = EditText(requireActivity())

        // Specify the type of input
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(input)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
//            editor?.putString(DURATION_DIALOG, input.text.toString())
//            editor?.commit()
            dialog.dismiss()
        }
        builder.setPositiveButton("ok", DialogInterface.OnClickListener(function = positiveButtonClick))

        builder.setNegativeButton("cancel", this)
        dialog = builder.create()

        return dialog
    }

    override fun onClick(dialog: DialogInterface?, which: Int) { //which is the index of the button
        if(which == DialogInterface.BUTTON_POSITIVE) {
            Toast.makeText(requireActivity(),"time is set", Toast.LENGTH_SHORT).show()
            // use sharedPreference to store entries
        }
        else if(which == DialogInterface.BUTTON_NEGATIVE)
            Toast.makeText(requireActivity(),"cancelled", Toast.LENGTH_SHORT).show()
    }
}