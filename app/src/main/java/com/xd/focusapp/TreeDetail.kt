package com.xd.focusapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TreeDetail: AppCompatActivity(){

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tree_detail)

        val imageview = findViewById<ImageView>(R.id.tree_details)
        val image = intent.getIntExtra("image", 0)
        imageview.setImageResource(image)

        val rarity = findViewById<TextView>(R.id.rarity)
        var rarity_text = ""
        if(intent.getIntExtra("rarity", -1) == 0){
            rarity_text = "legendary"
        }
        else if(intent.getIntExtra("rarity", -1) == 1){
            rarity_text = "rare"
        }
        else if(intent.getIntExtra("rarity", -1) == 2){
            rarity_text = "uncommon"
        }
        else if(intent.getIntExtra("rarity", -1) == 3){
            rarity_text = "common"
        }
        rarity.text = "Rarity: ${rarity_text}"

        val name = findViewById<TextView>(R.id.Name)

        name.text = "Name: ${intent.getStringExtra("name")}"

        val sourceText = findViewById<TextView>(R.id.source)

        val source = intent.getIntExtra("source", 0)
        var whereGetIt =
            if(source == 0){
                "Timer"
            }
            else{
                "Spinner"
            }
        sourceText.text = "Get it from $whereGetIt "



        findViewById<Button>(R.id.back).setOnClickListener{
            finish()
        }

    }
}