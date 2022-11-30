package com.xd.focusapp.ui.setting

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.xd.focusapp.R

class SearchAdapter(private val context: Context, private val users:List<User> ) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_search_list, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder( holder: ViewHolder, position:Int) {
        val user:User = users[position]
        holder.userName.text = user.name
        if(user.icon!=null){
            val encodedImage = user.icon
            val stringImageToByte = Base64.decode(encodedImage, Base64.DEFAULT)
            val profileBitmap = BitmapFactory.decodeByteArray(stringImageToByte,
                0,stringImageToByte.size)
            holder.userIcon.setImageBitmap(profileBitmap)
        }
        holder.itemView.setOnClickListener{
            val im:InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(it.windowToken,0)
            val intent = Intent(context, ViewProfileActivity::class.java)
            intent.putExtra("user_name",user.name)
            intent.putExtra("user_id",user.uid)
//            intent.putExtra("user_icon",user.icon)
            intent.putExtra("user_credits",user.credits)
            context.startActivities(arrayOf(intent))
        }

    }


    override fun getItemCount(): Int {
         return users.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val userImage:ImageView = itemView.findViewById(R.id.user_image)
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val userIcon = itemView.findViewById<ImageView>(R.id.user_image)
//        val toprel = itemView.findViewById<RelativeLayout>(R.id.top_rel)

    }

}