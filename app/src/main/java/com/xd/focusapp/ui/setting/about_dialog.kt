package com.xd.focusapp.ui.setting

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.xd.focusapp.R

class about_dialog:DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog:Dialog

        val builder = AlertDialog.Builder(requireActivity())
        val view = requireActivity().layoutInflater.inflate(R.layout.about_dialog, null)
        builder.setView(view)

        builder.setPositiveButton("ok"){ dialog, which ->
            dismiss()
        }

        dialog = builder.create()

        return dialog
    }
}