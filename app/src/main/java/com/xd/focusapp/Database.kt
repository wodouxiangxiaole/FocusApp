package com.xd.focusapp
import java.sql.Connection
import java.sql.DriverManager

class Database {
    private var connection: Connection? = null

    // For Amazon Postgresql
    // private final String host = "ssprojectinstance.csv2nbvvgbcb.us-east-2.rds.amazonaws.com"
    // For Google Cloud Postgresql
    // private final String host = "35.44.16.169";
    // For Local PostgreSQL
    private val host = "104.198.169.251"
    private val database = "focusapp"
    private val port = 5432
    private val user = "postgres"
    private val pass = "cmpt362"
    private var url = "jdbc:postgresql://%s:%d/%s"
    private var status = false

    init {
        url = String.format(url, this.host, this.port, this.database)
        connect()
        //this.disconnect();
        println("connection status:$status")
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
}