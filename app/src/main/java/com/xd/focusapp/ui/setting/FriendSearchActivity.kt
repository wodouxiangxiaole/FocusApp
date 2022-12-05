package com.xd.focusapp.ui.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xd.focusapp.Database
import com.xd.focusapp.R
import java.util.*

class FriendSearchActivity : AppCompatActivity() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var searchBtn: ImageView
    private lateinit var searchRec: RecyclerView

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var users:MutableList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_search)

        toolbar = findViewById(R.id.toolbar)
   //     searchBtn= findViewById(R.id.friend_search_btn)
        searchRec= findViewById(R.id.search_Recy)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener{
            val intent = Intent(this, FriendsMainPage::class.java)
            startActivity(intent)
        }

        users= mutableListOf()

        searchAdapter=SearchAdapter(this,users)
        val layoutManager :RecyclerView.LayoutManager = LinearLayoutManager(this)
        searchRec.layoutManager = layoutManager
        searchRec.adapter=searchAdapter


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        val searchView: SearchView = menu.findItem(R.id.search1).actionView as SearchView
        searchView.isIconified=false
        searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            .setTextColor(resources.getColor(R.color.hint_color))
        searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            .setHintTextColor(resources.getColor(R.color.hint_color))
        searchView.queryHint="Search People"
        searchView.maxWidth= Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(qString: String): Boolean {
                if(qString.length >2){
                    searchPeopleFromDb(qString,false)
                }
                else{
                    users.clear()
                    searchAdapter.notifyDataSetChanged()
                }

                return true
            }
            override fun onQueryTextSubmit(qString: String): Boolean {
                searchPeopleFromDb(qString,true)

                return true
            }
        })
        return true
    }


    private fun searchPeopleFromDb(qString:String, b:Boolean){
        val db = Database()
        val sp = this.getSharedPreferences("userSp", Context.MODE_PRIVATE)
        val curID = sp.getString("uid","-1")!!.toInt()
        val query = "select * from users where user_name like '$qString%' and uid!=${curID}  limit 10;"
        val ret = db.searchPeople(query)
        users.clear()
        users=ret
//        println("debug: users ${users[0].name}")
        searchAdapter=SearchAdapter(this,users)
        searchRec.adapter=searchAdapter
        searchAdapter.notifyDataSetChanged()
    }




}