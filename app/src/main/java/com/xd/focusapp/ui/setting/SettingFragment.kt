package com.xd.focusapp.ui.setting

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.Database
import com.xd.focusapp.databinding.FragmentSettingBinding
import org.w3c.dom.Text

class SettingFragment : Fragment() {

    private lateinit var nameTextView : TextView
    private lateinit var userIDTextView : TextView


    private lateinit var settingListView: ListView
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var settingOptions: Array<String>
    private lateinit var user:MutableMap<String,String>
    private val uid = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settingOptions=settingViewModel.settingOptions

        settingListView = binding.SettingList
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(root.context,
            R.layout.simple_list_item_1, settingOptions)
        settingListView.adapter = arrayAdapter

        val db = Database()
        val query = "select * from users where uid = ${uid};"
        user = db.getUser(query)
        //println("debug: name ${user["name"]}")

        val userIDTextView = binding.ProfileUserID
        val nameTextView = binding.ProfileName

        userIDTextView.text = user["uid"]
        nameTextView.text = user["name"]

//        val textView: TextView = binding.textSetting
//        settingViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        settingListView.setOnItemClickListener(){ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            if(settingOptions[position]=="Friends"){
                val intent = Intent(requireActivity(), FriendsMainPage::class.java)
                val bundle = Bundle()

                for(entry in user.entries){
                    bundle.putString(entry.key,entry.value)
                    println("debug: entry.key,entry.value, ${entry.key},${entry.value}")
                }

                intent.putExtra("user",bundle)
                startActivity(intent)
            }

        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}