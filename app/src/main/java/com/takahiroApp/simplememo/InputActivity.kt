package com.takahiroApp.simplememo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.*

class InputActivity : AppCompatActivity() {

    private val _helper=DatabaseHelper(this@InputActivity)
    private var inputTitleString=""
    private var id:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)


        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.tv_title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id=intent.getIntExtra("id",-1)

        if(id != -1){
            val db=_helper.writableDatabase
            val sql="SELECT * FROM simplememo WHERE _id=${id}"
            val cursor=db.rawQuery(sql,null)
            while(cursor.moveToNext()){
                val idTitle=cursor.getColumnIndex("title")
                val idText=cursor.getColumnIndex("text")
                val title=cursor.getString(idTitle)
                val text=cursor.getString(idText)

                val inputTitle=findViewById<EditText>(R.id.inputTitle)
                val inputText=findViewById<EditText>(R.id.inputText)
                inputTitle.setText(title)
                inputText.setText(text)
            }
            cursor.close()


        }

        val inputText= findViewById<EditText>(R.id.inputText)
        inputText.setSelection(0,0)


    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

//クリックリスナー

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onSaveClick(@Suppress("UNUSED_PARAMETER") view:View){
        val inputTitle=findViewById<EditText>(R.id.inputTitle)
        val inputText=findViewById<EditText>(R.id.inputText)


        inputTitleString = inputTitle.text.toString()
//        if(inputTitleString==""){
//            inputTitleString="No Title"
//        }

        val inputTextString=inputText.text.toString()
        val date = SimpleDateFormat("yyyy/MM/dd kk:mm:ss",Locale.getDefault()).toString()

        val db=_helper.writableDatabase

        if(id != -1){
            val sqlUpdate="UPDATE simplememo SET title = ?, text = ?, date = ? WHERE _id =?"
            val stmt=db.compileStatement(sqlUpdate)
            stmt.bindString(1,inputTitleString)
            stmt.bindString(2,inputTextString)
            stmt.bindString(3,date)
            stmt.bindLong(4,id.toLong())
            stmt.executeUpdateDelete()

        }else{

            val sqlInsert="INSERT INTO simplememo (title,text,date) VALUES (?,?,?)"
            val stmt=db.compileStatement(sqlInsert)
            stmt.bindString(1,inputTitleString)
            stmt.bindString(2,inputTextString)
            stmt.bindString(3,date)
            stmt.executeInsert()
        }



        val intent=Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)

        val duration= Toast.LENGTH_SHORT
        val toastText="保存しました。"
        Toast.makeText(applicationContext,toastText,duration).show()



        finish()

    }

    fun onCountClick(@Suppress("UNUSED_PARAMETER") view:View){

        val inputText=findViewById<EditText>(R.id.inputText)
        val length=inputText.text.toString().trim().replace("\n","").length.toString()
        val text="現在の文字数は${length}文字です。"

        val toast=Toast.makeText(applicationContext,text,Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER or Gravity.CENTER,0,0)
        toast.show()

    }


}