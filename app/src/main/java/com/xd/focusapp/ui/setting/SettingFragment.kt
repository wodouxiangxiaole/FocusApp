package com.xd.focusapp.ui.setting

import android.R
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.Database
import com.xd.focusapp.databinding.FragmentSettingBinding
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream

class SettingFragment : Fragment() {

    private lateinit var db:Database

    private lateinit var nameTextView : TextView
    private lateinit var userIDTextView : TextView
    private lateinit var profile_button: Button
    private lateinit var profile_image:de.hdodenhof.circleimageview.CircleImageView
    private lateinit var settingViewModel: SettingViewModel

    private lateinit var imgPickedUri: Uri

    private lateinit var settingListView: ListView
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var settingOptions: Array<String>
    private lateinit var user:MutableMap<String,String>
    private val uid = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = Database()

        // update profile image
        profile_image=binding.ProfileImage
        settingViewModel.userImg.observe(this.requireActivity()){
            profile_image.setImageBitmap(it)
            val baos = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val storeImageString:String = Base64.encodeToString(data, Base64.DEFAULT)
            val query = "update users set icon = '$storeImageString' where uid = ${uid};"
            db.updateProfileImage(query)
        }

        //options list
        settingOptions=settingViewModel.settingOptions
        settingListView = binding.SettingList
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(root.context,
            R.layout.simple_list_item_1, settingOptions)
        settingListView.adapter = arrayAdapter


        val query = "select * from users where uid = ${uid};"
        user = db.getUser(query)
        println("debug: ${user["email"]},${user["credits"]},${user["uid"]},${user["icon"]},${user["user_name"]}")

        userIDTextView = binding.ProfileUserID
        nameTextView = binding.ProfileName

        userIDTextView.text = user["uid"]
        nameTextView.text = user["user_name"]
        if(user["icon"]!=null){
            val encodedImage = user["icon"]
            println("debug: icon:$encodedImage")
            val stringImageToByte = Base64.decode(encodedImage, Base64.DEFAULT)
            val profileBitmap = BitmapFactory.decodeByteArray(stringImageToByte,
                0,stringImageToByte.size)
            profile_image.setImageBitmap(profileBitmap)
        }



//        val textView: TextView = binding.textSetting
//        settingViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        settingListView.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            if(settingOptions[position]=="Friends"){
                val intent = Intent(requireActivity(), FriendsMainPage::class.java)
                val bundle = Bundle()

                for(entry in user.entries){
                    bundle.putString(entry.key,entry.value)
               //     println("debug: entry.key,entry.value, ${entry.key},${entry.value}")
                }

                intent.putExtra("user",bundle)
                startActivity(intent)
            }
        }


        // change profile image button
        profile_button = binding.profileOptionBtn
        profile_button.text = "Edit Profile"
        profile_button.setOnClickListener{
            val galIntent = Intent(Intent.ACTION_PICK)
            galIntent.type="image/*"
            startActivityForResult(galIntent,1)
        }

        return root
    }

    // select image done
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1 && resultCode == Activity.RESULT_OK && data!=null){
            imgPickedUri = data.data!!
            val bitmapPick = getBitmap(this.requireActivity(), imgPickedUri)
            settingViewModel.userImg.value = bitmapPick

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getBitmap(context: Context, imgUri: Uri): Bitmap {
        var bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imgUri))
        val matrix = Matrix()
        matrix.setRotate(90f)
        var ret = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return ret
    }
}