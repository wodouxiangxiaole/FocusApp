package com.xd.focusapp.ui.focus

import android.R
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.databinding.FragmentFocusBinding
import org.w3c.dom.Text


class FocusFragment : Fragment() {
    private lateinit var focusViewModel: FocusViewModel

    private var _binding: FragmentFocusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var factory = FocusViewModelFactory()

        focusViewModel =
            ViewModelProvider(requireActivity(), factory).get(FocusViewModel::class.java)

        _binding = FragmentFocusBinding.inflate(inflater, container, false)
        val root: View = binding.root



//        val textView: TextView = binding.textFocus
//        focusViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        var setTimer: TextView = binding.selectTime
        setTimer.setOnClickListener { it->
            var timePickerDialog = TimePickerFragment()
            timePickerDialog.show(requireActivity().supportFragmentManager,"")
        }
        focusViewModel.timer.observe(requireActivity()){
            println("DEBUGG timer observe " + it)
            var hour = it / 60
            var minute = it % 60

            setTimer.text = String.format("%s:%s", hour, minute)
        }

        return root
    }

//    fun setTimer() {
//
//        TimePickerFragment().show(supportFragmentManager, "timePicker")
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}