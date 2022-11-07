package com.example.focusapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.focusapp.R
import com.example.focusapp.databinding.FragmentNotificationsBinding
import java.util.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var spin_button:Button
    private lateinit var wheel:ImageView

    private val sectors = arrayOf("common", "uncommon", "rare", "legendary")
    private val sectorsDegree = arrayOf(0, 180, 288, 342)
    private var degree:Int = 0
    private var isSpinning:Boolean = false


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        spin_button = root.findViewById(R.id.spin_button)
        wheel = root.findViewById(R.id.wheel)

        spin_button.setOnClickListener{
            if(!isSpinning){
                spin()
                isSpinning = true
            }
        }
        return root
    }
    fun spin(){
        var randomDegree = Random().nextInt(360)
        val rotateAnimation = RotateAnimation(
            0F, (360*sectors.size + 360-randomDegree).toFloat(),
            RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 3600
        rotateAnimation.fillAfter = true
        rotateAnimation.setInterpolator(DecelerateInterpolator())
        rotateAnimation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationEnd(p0: Animation?) {
                isSpinning = false
                var res:String
                if(randomDegree <= 180){
                    res = "common"
                }
                else if(randomDegree <= 288){
                    res = "uncommon"
                }
                else if(randomDegree <= 342){
                    res = "rare"
                }
                else{
                    res = "legendary"
                }
                Toast.makeText(requireActivity(),"You got " + res, Toast.LENGTH_SHORT).show()
            }

            override fun onAnimationStart(p0: Animation?) {
                println(randomDegree)
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })

        wheel.startAnimation(rotateAnimation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}