package com.xd.focusapp.ui.setting

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.Database
import com.xd.focusapp.R

class FriendsMainPage : AppCompatActivity() {

    private lateinit var users:MutableList<User>
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var addFriendBtn:ImageView
    private lateinit var myviewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_main_page)

        toolbar = findViewById(R.id.toolbar)
        addFriendBtn= findViewById(R.id.friend_search_btn)

        addFriendBtn.setOnClickListener{
            val intent = Intent(this, FriendSearchActivity::class.java)
            startActivity(intent)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener{
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        users= mutableListOf()
        val db = Database()
        val sp = this.getSharedPreferences("userSp", Context.MODE_PRIVATE)
        val curID = sp.getString("uid","-1")!!.toInt()
        val query = "select * from users as u where u.uid in (select cast(usera as INT) from Friend where userb='${curID}' and stat = 1);"
        val ret = db.searchPeople(query)
        users.clear()
        users=ret
        val query2 = "select * from users as u where u.uid in (select cast(userb as INT) from Friend where usera='${curID}' and stat = 1);"
        val ret2 = db.searchPeople(query2)
        users+=ret2

        var fLAdapter = FriendListAdapter(this,users)
        val fList = findViewById<ListView>(R.id.list)
        fList.adapter = fLAdapter

        myviewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        myviewModel.datachanged.observe(this){
            fList.adapter = fLAdapter
            fLAdapter.notifyDataSetChanged()
            println("debug: ${myviewModel.datachanged.value}")
        }


        fList.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->

            val user:User = users[position]

            val intent = Intent(this, ViewProfileActivity::class.java)
            intent.putExtra("user_name",user.name)
            intent.putExtra("user_id",user.uid)
            intent.putExtra("user_credits",user.credits)
            startActivities(arrayOf(intent))

        }

        val checkReqQuery = "select * from users as u where u.uid in (select cast(usera as INT) from friend where userb='${curID}' and stat =0);"
        val reqList = db.searchPeople(checkReqQuery)

        var reqUsers: MutableList<User> = reqList

        var reqfLAdapter = FriendListAdapter(this,reqUsers)
        val reqfList = findViewById<ListView>(R.id.reqlist)
        reqfList.adapter = reqfLAdapter

        myviewModel.datachanged.observe(this){
            reqfList.adapter = reqfLAdapter
            reqfLAdapter.notifyDataSetChanged()

        }

        reqfList.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, id: Long ->

            val user:User = reqUsers[position]

            val intent = Intent(this, ViewProfileActivity::class.java)
            intent.putExtra("user_name",user.name)
            intent.putExtra("user_id",user.uid)
            intent.putExtra("user_credits",user.credits)
            startActivities(arrayOf(intent))

        }
    }


}