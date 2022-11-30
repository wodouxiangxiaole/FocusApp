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

    companion object{
        const val DIALOG_KEY = "dialog"
        const val LOCK_DIALOG = 1
        const val CREDITS_NOT_ENOUGH = 2
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog:Dialog

        val builder = AlertDialog.Builder(requireActivity())

        val bundle = arguments
        val dialogId = bundle?.getInt(DIALOG_KEY)
        if(dialogId == LOCK_DIALOG){
            builder.setTitle("The plant is locked. You can go spinner wheel or set focus time to get it")
//        val input = TextView(requireActivity())
//        input.text = "The plant is locked. You can go spinner wheel or set focus time to get it"
//        builder.setView(input)

            builder.setPositiveButton("ok"){ dialog, which ->
                dismiss()
            }
        }
        else if(dialogId == CREDITS_NOT_ENOUGH){
            builder.setTitle("Your credits NOT enough")
            builder.setPositiveButton("ok"){ dialog, which ->
                dismiss()
            }
        }


        dialog = builder.create()

        return dialog
    }
}