package com.xd.focusapp.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.Database
import com.xd.focusapp.R
import de.hdodenhof.circleimageview.CircleImageView

class ViewProfileActivity : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var myViewModel:SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener{
            finish()
        }
        val user = User()
        user.name= intent.extras?.getString("user_name").toString()
        user.uid= intent.extras?.getString("user_id").toString()
        user.credits= intent.extras?.getString("user_credits").toString()
    //    user.icon= intent.extras?.getString("user_icon").toString()

        val btn:Button = findViewById(R.id.profile_option_btn2)
        val usernameview:TextView=findViewById(R.id.Profile_Name2)
        val useridview:TextView=findViewById(R.id.Profile_User_ID2)
        val usericon: CircleImageView =findViewById(R.id.Profile_Image2)

        usernameview.text = user.name
        useridview.text = user.uid

        val db = Database()
        val query = "select * from users where uid = ${user.uid};"
        val queryuser = db.getUser(query)
        user.icon= queryuser["icon"].toString()

        myViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        if(user.icon!=null){
            val encodedImage = user.icon
            //println("debug: $encodedImage")
            val stringImageToByte = Base64.decode(encodedImage, Base64.DEFAULT)
            val profileBitmap = BitmapFactory.decodeByteArray(stringImageToByte,
                0,stringImageToByte.size)
            usericon.setImageBitmap(profileBitmap)
        }


        val sp = this.getSharedPreferences("userSp", Context.MODE_PRIVATE)
        val curID = sp.getString("uid","-1")!!.toInt()
        val RecivedQuery = "select stat from Friend where userA = '${user.uid}' AND userB='${curID}';"
        val ReqQuery = "select stat from friend where usera = '${curID}' AND userb='${user.uid}';"
        var state = db.getFriStat(RecivedQuery)

        if (state == 0){ // user can accept new friend
            btn.text="ACCEPT"
            btn.setBackgroundColor(Color.GREEN)
            btn.setOnClickListener {
                val updateQuery= "update Friend set stat=1 where userA='${user.uid}' AND userB='${curID}';"
                db.updateFriStat(updateQuery)
                Toast.makeText(this,"Accepted Request", Toast.LENGTH_SHORT).show()
                myViewModel.datachanged.value= 1
                finish()
                startActivity(intent)
            }
        }
        else if(state == 1){ // users are already friend
            btn.text="UNFRIEND"
            btn.setBackgroundColor(Color.RED)
            btn.setOnClickListener {
                val updateQuery = "delete from Friend where userA = '${user.uid}' AND userB='${curID}';"
                db.updateFriStat(updateQuery)
                Toast.makeText(this,"Unfriended User", Toast.LENGTH_SHORT).show()
                myViewModel.datachanged.value=1
                finish()
                startActivity(intent)
            }
        }
        else{
            state = db.getFriStat(ReqQuery)
            if(state == 1){ // users are already friend
                btn.text="UNFRIEND"
                btn.setBackgroundColor(Color.RED)
                btn.setOnClickListener {
                    val updateQuery =
                        "delete from Friend where userA = '${curID}' AND userB='${user.uid}';"
                    db.updateFriStat(updateQuery)
                    Toast.makeText(this,"Unfriended User", Toast.LENGTH_SHORT).show()
                    myViewModel.datachanged.value=1
                    finish()
                    startActivity(intent)
                }
            }
            else if(state==null ){ // user can send req
                btn.text="ADD FRIEND"
                btn.setBackgroundColor(Color.GREEN)
                btn.setOnClickListener {
                    val updateQuery =
                        "insert into friend (userA, userB, stat) values ('${curID}','${user.uid}',0);"
                    db.updateFriStat(updateQuery)
                    Toast.makeText(this,"Request sent", Toast.LENGTH_SHORT).show()
                    myViewModel.datachanged.value=1
                    finish()
                    startActivity(intent)
                }
            }
            else if(state == 0){ // user can remove req while waiting
                btn.text="CANCEL REQUEST"
                btn.setBackgroundColor(Color.RED)
                btn.setOnClickListener {
                    val updateQuery =
                        "delete from Friend where userA = '${curID}' AND userB='${user.uid}';"
                    db.updateFriStat(updateQuery)
                    Toast.makeText(this,"Request canceled", Toast.LENGTH_SHORT).show()
                    myViewModel.datachanged.value=1
                    finish()
                    startActivity(intent)
                }
            }
        }
    }
}