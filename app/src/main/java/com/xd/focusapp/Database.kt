package com.xd.focusapp

import android.content.Context
import android.util.MutableInt
import androidx.lifecycle.MutableLiveData
import com.xd.focusapp.ui.setting.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


class Database() {
    private var connection: Connection? = null

    // For Local postgresSQL
    // private val host = "10.0.2.2"

    // For Google Cloud Postgresql

    // private final String host = "35.44.16.169";
    // For Local PostgreSQL

    // Google cloud PostgresSQL login information
    private val host = "104.198.169.251"
    private val database = "focus_app_db"
    private val port = 5432
    private val user = "postgres"
    private val pass = "cmpt362"
    private var url = "jdbc:postgresql://%s:%d/%s"
    private var status = false


    init {
        url = String.format(url, this.host, this.port, this.database)

     //   println("debug: $url")
        connect()
        // this.disconnect();
     //   println("connection status:$status")
    }

    private fun connect() {
        val thread = Thread {
            try {
                Class.forName("org.postgresql.Driver")
                connection = DriverManager.getConnection(url, user, pass)

                status = true
                println("connected:$status")
            } catch (e: Exception) {
                status = false
                print(e.message)
                e.printStackTrace()
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
    }

    val extraConnection: Connection?
        get() {
            var c: Connection? = null
            try {
                Class.forName("org.postgresql.Driver")
                c = DriverManager.getConnection(url, user, pass)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return c
        }

    fun insert(query:String){
        try{
            val stat:Statement = connection!!.createStatement()
            stat.executeUpdate(query)
            println("Debug: database.insert(query) successful")

        }
        catch (e:Exception){
            println("Debug: database.insert(query) failed $e")
            e.printStackTrace()
        }
    }

    fun updateProfileImage(query:String) {
        CoroutineScope(IO).launch{
            try {
                val stat: Statement = connection!!.createStatement()
                stat.executeUpdate(query)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchPeople(query:String):MutableList<User>{
        val userList = mutableListOf<User>()
        val a1 = Thread  {
            try {
                val stat: Statement = connection!!.createStatement()
                val rs = stat.executeQuery(query)
                while(rs.next()) {
                    val newUser = User()
                    newUser.name = rs.getString(6)
                    if(rs.getString(4)!=null){
                        newUser.icon = rs.getString(4)
                    }
                    newUser.uid=rs.getString(3)
                    newUser.credits=rs.getString(2)
                    userList.add(newUser)
                 //   println("debug111: ${rs.getString(6)}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        a1.start()
        a1.join()
        return userList
    }

    fun getUser(query: String):MutableMap<String,String>{
        val map = mutableMapOf<String,String>()
        println("debug111:")
        val a1 = Thread  {
            try{
                val stat:Statement = connection!!.createStatement()
                val rs = stat.executeQuery(query)
                while(rs.next()) {
                    map["email"] = rs.getString(1)
                    if(rs.getString(2)!=null){
                        map["credits"] = rs.getString(2)
                    }
                    map["uid"] = rs.getString(3)
                    if(rs.getString(4)!=null){
                        map["icon"] = rs.getString(4)
                    }
                    map["pwd"] = rs.getString(5)
                    map["user_name"]=rs.getString(6)
                    map["fb_uid"]=rs.getString(7)
//                    println("debug111: ${rs.getString(1)}, ${rs.getString(2)}," +
//                            " ${rs.getString(3)}")
//                    println("debug111: ${rs.getString(7)}")
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        a1.start()
        a1.join()
        return map
    }


    fun getUnlockedId(uid:Int): ArrayList<Int>{
        var id = ArrayList<Int>()

        try{
            val stat:Statement = connection!!.createStatement()
            val rs = stat.executeQuery("SELECT * FROM users_collect_tree WHERE uid = $uid")
            while(rs.next()) {
                id.add(rs.getString(2).toInt())
            }
        }
        catch (e:Exception){
            e.printStackTrace()

        }
        return id
    }

    fun closeConnection(){
        connection!!.close()
    }

    fun insertUserCollection(uid: Int, tid: Int) {
        CoroutineScope(IO).launch {
            try {
                val stat: Statement = connection!!.createStatement()
                stat.executeUpdate("insert INTO users_collect_tree VALUES ($uid, $tid) ")

            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

//    fun getCredits(uid:Int):String {
//        val credits = MutableLiveData<String>();
//        val t = Thread {
//            try {
//                val stat: Statement = connection!!.createStatement()
//                val rs = stat.executeQuery("SELECT credits FROM users WHERE uid = $uid")
//                while (rs.next()) {
//                    credits.value = rs.getString(2)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//
//            }
//        }
//        t.start()
//        t.join()
//
//        return credits.value!!;
//    }


}