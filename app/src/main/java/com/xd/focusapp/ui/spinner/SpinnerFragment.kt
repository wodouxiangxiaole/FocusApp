package com.xd.focusapp.ui.spinner

import android.content.Context
import android.content.SharedPreferences
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
import com.xd.focusapp.MainActivity
import com.xd.focusapp.R
import com.xd.focusapp.databinding.FragmentSpinnerBinding
import com.xd.focusapp.ui.collection.CollectionViewModel
import com.xd.focusapp.ui.collection.CollectionViewModelFactory
import com.xd.focusapp.ui.collection.MyDialog
import java.util.*


class SpinnerFragment : Fragment() {

    private var _binding: FragmentSpinnerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var spin_button: Button
    private lateinit var wheel: ImageView

    private val sectors = arrayOf("common", "uncommon", "rare", "legendary")
    private var isSpinning:Boolean = false

    private lateinit var spinnerViewModel: SpinnerViewModel

    private lateinit var collectionViewModel: CollectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        spinnerViewModel =
            ViewModelProvider(this).get(SpinnerViewModel::class.java)

        val sp = this.activity?.getSharedPreferences("userSp", Context.MODE_PRIVATE)
        val viewModelFactory = CollectionViewModelFactory(sp!!.getString("email", ""))
        collectionViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(CollectionViewModel::class.java)

        _binding = FragmentSpinnerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        spin_button = root.findViewById(R.id.spin_button)
        wheel = root.findViewById(R.id.wheel)

        spin_button.setOnClickListener {
            val sp = this.activity?.getSharedPreferences("userSp", Context.MODE_PRIVATE)

            // test user login or not ==> if guest mode ==> can not spin it
            if(sp?.getString("email", null) == null){
                val bundle = Bundle()
                bundle.putInt(MyDialog.DIALOG_KEY, MyDialog.USER_NOT_LOGIN)

                val dialog = MyDialog()
                dialog.arguments = bundle

                dialog.show(parentFragmentManager, "dialog")
            }
            else {
                // if credits > 120 ==> can spin
                var credits = sp?.getString("credits", "")!!.toInt()
                if (credits >= 120) {
                    credits -= 120

                    var editor: SharedPreferences.Editor = sp.edit().apply {
                        putString("credits", credits.toString())
                    }
                    editor.commit()

                    if (!isSpinning) {
                        spin()
                        isSpinning = true
                    }
                    (activity as MainActivity?)?.updateMenuTitles()

                } else {
                    val bundle = Bundle()
                    bundle.putInt(MyDialog.DIALOG_KEY, MyDialog.CREDITS_NOT_ENOUGH)

                    val dialog = MyDialog()
                    dialog.arguments = bundle

                    dialog.show(parentFragmentManager, "dialog")

                }
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
                var res = if(randomDegree <= 180){
                    3
                } else if(randomDegree <= 288){
                    2
                } else if(randomDegree <= 342){
                    1
                } else{
                    0
                }
                // Get tree object
                // random unlock 1 tree in current rank level
                // use shared preference to pass data

                Toast.makeText(activity,"You got ${sectors[3-res]}",Toast.LENGTH_SHORT).show()

                // GET PLANT FROM SPINNER
                val plant = collectionViewModel.unlock(res, 1)


                val dialog = SpinnerFinishDialog()
                val bundle = Bundle()
                bundle.putInt("image", plant.image!!)


                if(plant.toPoints){
                    bundle.putInt("key", SpinnerFinishDialog.REPEAT_KEY)
                    when(res){
                        0 ->{
                            bundle.putInt("points", SpinnerFinishDialog.LEGENDARY)
                        }
                        1 -> {
                            bundle.putInt("points", SpinnerFinishDialog.RARE)
                        }
                        2 -> {
                            bundle.putInt("points", SpinnerFinishDialog.UNCOMMON)
                        }
                        else -> {
                            bundle.putInt("points", SpinnerFinishDialog.COMMON)
                        }
                    }

                }
                else{
                    bundle.putInt("key", SpinnerFinishDialog.NORMAL_KEY)
                }

                dialog.arguments = bundle

                dialog.show(parentFragmentManager, "dialog")



            }

            override fun onAnimationStart(p0: Animation?) {
                // println(randomDegree)
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