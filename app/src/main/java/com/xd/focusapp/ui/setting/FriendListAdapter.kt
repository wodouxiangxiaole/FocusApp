package com.xd.focusapp.ui.setting

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.xd.focusapp.R

class FriendListAdapter(private val context: Context, private val users:List<User> ) : BaseAdapter(){
    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.friends_adapter_list,null)
        val profile_imgs = view. findViewById<ImageView>(R.id.user_image2)
        val profile_names = view. findViewById<TextView>(R.id.user_name2)

        profile_names.text=users.get(position).name
        val temp_img = users.get(position).icon

        if(temp_img!=null){
            val encodedImage = temp_img
            //println("debug: $encodedImage")
            val stringImageToByte = Base64.decode(encodedImage, Base64.DEFAULT)
            val profileBitmap = BitmapFactory.decodeByteArray(stringImageToByte,
                0,stringImageToByte.size)
            profile_imgs.setImageBitmap(profileBitmap)
        }
        return view
    }



}