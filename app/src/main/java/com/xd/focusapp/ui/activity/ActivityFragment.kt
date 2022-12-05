package com.xd.focusapp.ui.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.Database
import com.xd.focusapp.R
import com.xd.focusapp.databinding.FragmentActivityBinding


class ActivityFragment : Fragment() {

    private var _binding: FragmentActivityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tableRow: TableRow
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val activityViewModel =
            ViewModelProvider(this).get(ActivityViewModel::class.java)

        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // create db instance
        val db = Database()

        val sp = this.activity?.getSharedPreferences("userSp", Context.MODE_PRIVATE)
        val uemail = sp?.getString("email", null)
        // LOGIN MODE
        if(uemail != null){
            // set user into db
            val query = "select * from users where email = '${uemail}';"
            db.getUser(query)

            // get user map

            val map = db.updateActivityTable()
            val focusTimeList = ArrayList<String>()
            val startTimeList = ArrayList<String>()


            for(i in map[0].indices) {
                focusTimeList.add(map[0][i])
                startTimeList.add(map[1][i])
            }

            val table = root.findViewById<TableLayout>(R.id.table)
            for(i in focusTimeList.indices){
                tableRow = TableRow(requireActivity())
                textView1 = TextView(requireActivity())
                textView2 = TextView(requireActivity())
                textView1.text = startTimeList[i]
                textView2.text = focusTimeList[i]
                textView1.setTextColor(Color.BLACK)
                textView2.setTextColor(Color.BLACK)


                textView1.background = ContextCompat.getDrawable(requireActivity(), R.drawable.border)
                textView2.background = ContextCompat.getDrawable(requireActivity(), R.drawable.border)


                tableRow.addView(textView1)
                tableRow.addView(textView2)


                table.addView(tableRow)
            }



        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}