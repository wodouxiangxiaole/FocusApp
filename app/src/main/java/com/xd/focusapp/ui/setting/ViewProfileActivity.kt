package com.xd.focusapp.ui.setting

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import com.xd.focusapp.Database
import com.xd.focusapp.R
import de.hdodenhof.circleimageview.CircleImageView

class ViewProfileActivity : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        val user = User()
        user.name= intent.extras?.getString("user_name").toString()
        user.uid= intent.extras?.getString("user_id").toString()
        user.credits= intent.extras?.getString("user_credits").toString()
    //    user.icon= intent.extras?.getString("user_icon").toString()


        val usernameview:TextView=findViewById(R.id.Profile_Name2)
        val useridview:TextView=findViewById(R.id.Profile_User_ID2)
        val usericon: CircleImageView =findViewById(R.id.Profile_Image2)

        usernameview.text = user.name
        useridview.text = user.uid

        val db = Database()
        val query = "select * from users where uid = ${user.uid};"
        val queryuser = db.getUser(query)
        user.icon= queryuser["icon"].toString()


        if(user.icon!=null){
            val encodedImage = user.icon
            //println("debug: $encodedImage")
            val stringImageToByte = Base64.decode(encodedImage, Base64.DEFAULT)
            val profileBitmap = BitmapFactory.decodeByteArray(stringImageToByte,
                0,stringImageToByte.size)
            usericon.setImageBitmap(profileBitmap)
        }


    }
}