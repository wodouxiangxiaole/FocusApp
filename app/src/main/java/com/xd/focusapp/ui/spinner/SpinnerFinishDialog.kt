package com.xd.focusapp.ui.spinner

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.xd.focusapp.MainActivity
import com.xd.focusapp.R
import com.xd.focusapp.ui.focus.FocusTimer

class SpinnerFinishDialog: DialogFragment() {

    companion object{
        var REPEAT_KEY = 100
        var NORMAL_KEY = 200

        var COMMON = 10
        var UNCOMMON = 20
        var RARE = 30
        var LEGENDARY = 50
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
            val sp = this.activity?.getSharedPreferences("userSp", Context.MODE_PRIVATE)
            var credits = sp?.getString("credits", "")!!.toInt()
            credits += points
            var editor: SharedPreferences.Editor = sp.edit().apply {
                putString("credits", credits.toString())
            }
            val show_points = view.findViewById<TextView>(R.id.points)
            show_points.text = "Transfer to ${points} credits"
            editor.commit()

        }

        builder.setPositiveButton("ok"){ dialog, which ->
            dismiss()
        }

        dialog = builder.create()

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (activity is MainActivity) {
            (activity as MainActivity?)?.updateMenuTitles()
        } else if (activity is FocusTimer) {
            (activity as FocusTimer?)?.updateDB()
        }
    }
}