package com.xd.focusapp

import android.util.MutableInt
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


class Database {
    private var connection: Connection? = null

    // For Local postgresSQL
    // private val host = "10.0.2.2"

    // For Google Cloud Postgresql
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

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun getUser(query: String):MutableMap<String,String>{
        val map = mutableMapOf<String,String>()
        val a1 = Thread  {
            try{
                val stat:Statement = connection!!.createStatement()
                val rs = stat.executeQuery(query)
                while(rs.next()) {
                    map["name"] = rs.getString(2)
                    map["email"] = rs.getString(3)
                    map["credits"] = rs.getString(4)
                    map["uid"] = rs.getString(5)
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

    fun getAllUser(query: String){
        CoroutineScope(IO).launch {
            try{
                val stat:Statement = connection!!.createStatement()
                val rs = stat.executeQuery(query)
                while(rs.next()) {
                    println("debug111: ${rs.getString(1)}, ${rs.getString(2)}")
                }
            }
            catch (e:Exception){
                e.printStackTrace()

            }
        }
     }

    fun getUnlockedId(uid: Int): ArrayList<Int>{
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