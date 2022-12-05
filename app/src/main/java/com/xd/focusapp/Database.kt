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

    private var currentUid:Int ?= null


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
                    if(rs.getString(2)!=null){
                        newUser.credits=rs.getString(2)
                    }
                    userList.add(newUser)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        a1.start()
        a1.join()
        return userList
    }

    fun getFriStat(query:String): Int? {
        var rt: Int? = null
        val a1 = Thread  {
            try {
                val stat: Statement = connection!!.createStatement()
                val rs = stat.executeQuery(query)

                while(rs.next()){
                    rt= rs.getString(1).toInt()
                  //  println("debug:sss ${rs.getString(1)}")
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        a1.start()
        a1.join()
        return rt
    }

    fun updateFriStat(query:String){
        CoroutineScope(IO).launch{
            try {
                val stat: Statement = connection!!.createStatement()
                stat.executeQuery(query)
                println("debug: succeed1")
            } catch (e: Exception) {
                e.printStackTrace()
              //  println("debug: fail")
            }
        }
    }


    fun getUser(query: String):MutableMap<String,String>{
        val map = mutableMapOf<String,String>()
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
                    currentUid = rs.getString(3).toInt()

                    println("debug: uid = $currentUid")
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


    fun getUnlockedId(): ArrayList<ArrayList<Int>>{
        var id = ArrayList<Int>()
        var source = ArrayList<Int>()

        var unlockedArray = ArrayList<ArrayList<Int>>()

        try{
            val stat:Statement = connection!!.createStatement()
            val rs = stat.executeQuery("SELECT * FROM users_collect_tree WHERE uid = $currentUid")
            println("currentUid = $currentUid")
            while(rs.next()) {
                id.add(rs.getString(2).toInt())
                source.add(rs.getString(3).toInt())
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

        unlockedArray.add(id)
        unlockedArray.add(source)

        return unlockedArray
    }

    fun closeConnection(){
        connection!!.close()
    }

    fun insertUserCollection(tid: Int, source:Int) {
        CoroutineScope(IO).launch {
            try {
                val stat: Statement = connection!!.createStatement()
                stat.executeUpdate("insert INTO users_collect_tree VALUES ($currentUid, $tid, $source) ")

            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    fun updateUserCredits(credits: Int){
        CoroutineScope(IO).launch {
            try{
                val stat:Statement = connection!!.createStatement()
                stat.executeUpdate("UPDATE users SET credits = $credits WHERE uid = $currentUid")
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateActivityTable(): ArrayList<ArrayList<String>>{

        val arrayMap = ArrayList<ArrayList<String>>()
        val focusTimeList = ArrayList<String>()
        val startTimeList = ArrayList<String>()
        val a1 = Thread  {
            try{
                val stat:Statement = connection!!.createStatement()
                val query = "SELECT * FROM users_activity where uid = $currentUid"
                val rs = stat.executeQuery(query)
                while(rs.next()) {
                    focusTimeList.add(rs.getString(2))
                    startTimeList.add(rs.getString(3))
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        arrayMap.add(focusTimeList)
        arrayMap.add(startTimeList)
        a1.start()
        a1.join()

        return arrayMap
    }

    fun insertUserHistory(focusTime:Int, startTime:String){
        CoroutineScope(IO).launch {
            try{
                val stat:Statement = connection!!.createStatement()
                stat.executeUpdate("insert INTO users_activity VALUES ($currentUid, $focusTime, '$startTime')")
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }

    }


}