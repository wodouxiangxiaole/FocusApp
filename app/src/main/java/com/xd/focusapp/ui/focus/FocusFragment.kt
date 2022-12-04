package com.xd.focusapp.ui.focus

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.databinding.FragmentFocusBinding


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

        /**
         * Allow user to set focus timer
         */

        var setTimer: TextView = binding.selectTime
        setTimer.setOnClickListener { v->
            var timePickerDialog = TimePickerFragment()
            timePickerDialog.show(requireActivity().supportFragmentManager,"")
        }
        focusViewModel.timer_set.observe(requireActivity()){
            println("DEBUGG timer observe " + it)
            var hour = it / 60
            var hourDisplay = "00"
            if (hour != 0) {
                hourDisplay = hour.toString()
            }
            var minute = it % 60
            var minuteDisplay = "00"

            if (minute != 0) {
                minuteDisplay = minute.toString()
            }
            setTimer.text = String.format("%s:%s:00", hourDisplay, minuteDisplay)
        }



        var startTimer: Button = binding.timerStartButton
//        var timer: CountDownTimer? = null
        startTimer.setOnClickListener { v ->
            val sharedPreference: SharedPreferences? = requireActivity().getSharedPreferences("FocusApp", Context.MODE_PRIVATE)
            var timeInMin = sharedPreference?.getInt("Timer", 0)
            var timeInMillSec = (timeInMin?.times(60) ?: 0 ) * 1000

            if (timeInMillSec != 0) {
                // go to FocusTimer activity
                val intent = Intent(requireActivity(), FocusTimer::class.java)
                intent.putExtra("time",timeInMillSec)
                startActivity(intent)
            }

            // clean up old timer after its consumed
            var editor = sharedPreference?.edit()
            editor?.remove("Timer")
            editor?.commit()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}