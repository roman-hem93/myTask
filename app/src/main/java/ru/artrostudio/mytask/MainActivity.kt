package ru.artrostudio.mytask

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbManager : DateBaseManager = DateBaseManager()
        val tasks : ArrayList<DataTask> = dbManager.getTasks()







        val tvTitle : TextView = findViewById(R.id.title)
        val tvDate : TextView = findViewById(R.id.date)
        val tvMessage : TextView = findViewById(R.id.message)

        tvTitle.text = tasks.get(0).title
        tvDate.text = tasks.get(0).date
        tvMessage.text = tasks.get(0).message




        val bt1 : Button = findViewById(R.id.button1)
        val bt2 : Button = findViewById(R.id.button2)
        val tv : TextView = findViewById(R.id.textView)
        val textInput : EditText = findViewById(R.id.editTextText)

        bt1.setOnClickListener () {

        }

        bt2.setOnClickListener () {

        }
    }


}

