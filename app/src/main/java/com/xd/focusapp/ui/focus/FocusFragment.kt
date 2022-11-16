package com.xd.focusapp.ui.focus

import android.R
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.databinding.FragmentFocusBinding
import org.w3c.dom.Text
import kotlin.math.min


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
        var timer: CountDownTimer? = null
        startTimer.setOnClickListener { v ->
            val sharedPreference: SharedPreferences? = requireActivity().getSharedPreferences("FocusApp", Context.MODE_PRIVATE)
            var timeInMin = sharedPreference?.getInt("Timer", 0)
            var timeInMillSec = (timeInMin?.times(60) ?: 0 ) * 1000

            var countDown = binding.countDown
            if (timer != null) {
                timer?.cancel()
            }

            timer = object: CountDownTimer(timeInMillSec.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var totalTimeInSec = Math.ceil((millisUntilFinished / 1000).toDouble())
                    var secDisplay = "00"
                    var minDisplay = "00"
                    var hourDisplay = "00"
                    if (totalTimeInSec >= 60) {
                        secDisplay = ((totalTimeInSec % 60).toInt()).toString()
                        minDisplay = ((totalTimeInSec / 60).toInt()).toString()
                        var timeInMin = totalTimeInSec / 60

                        if (timeInMin >= 60) {
                            minDisplay = ((timeInMin % 60).toInt()).toString()
                            hourDisplay = ((timeInMin / 60).toInt()).toString()
                        }
                    } else {
                        secDisplay = (totalTimeInSec.toInt()).toString()
                    }

                    countDown.text = String.format("%s:%s:%s", hourDisplay, minDisplay, secDisplay)
                }

                override fun onFinish() {
                    countDown.text = "DONE"
                }
            }
            timer?.start()
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