package com.takahiroApp.simplememo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    val _helper=DatabaseHelper(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      test_Id  ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this@MainActivity,"ca-app-pub-3940256099942544~3347511713")
        val madView=findViewById<AdView>(R.id.adView)
        val adRequest=AdRequest.Builder().build()
        madView.loadAd(adRequest)

        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        toolbar.setLogo(R.drawable.ic_mode_edit_white_24dp)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        val addButton=findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener(addListener())

//      データベース取得
        val db=_helper.writableDatabase
        val sql="SELECT * FROM simplememo ORDER BY _id DESC"
        val cursor=db.rawQuery(sql,null)

        val menuList:MutableList<MutableMap<String,Any>> =mutableListOf()

        while(cursor.moveToNext()){
            val idId=cursor.getColumnIndex("_id")
            val idTitle=cursor.getColumnIndex("title")
            val idText=cursor.getColumnIndex("text")
            val idDate=cursor.getColumnIndex("date")

            val id=cursor.getInt(idId)
            val title=cursor.getString(idTitle)
            val text=cursor.getString(idText)
            val date=cursor.getString(idDate)
            val menu=mutableMapOf("id" to id,"title" to title,"text" to text,"date" to date)
            menuList.add(menu)

        }
        cursor.close()


//      リサイクルビュー
        val lvMenu=findViewById<RecyclerView>(R.id.lvMenu)
        val layout= LinearLayoutManager(applicationContext)
        lvMenu.layoutManager=layout
        lvMenu.adapter=RecyclerListAdapter(menuList)
        val decorator= DividerItemDecoration(applicationContext,layout.orientation)
        lvMenu.addItemDecoration(decorator)


    }

    private inner class RecyclerListViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var tvId:TextView
        var tvTitle:TextView
        var tvDate:TextView
        var view=itemView
        init{
            tvTitle=itemView.findViewById(R.id.tvTitle)
            tvDate=itemView.findViewById(R.id.tvDate)
            tvId=itemView.findViewById(R.id.tvId)
        }
    }

    private inner class RecyclerListAdapter(private val _listData:MutableList<MutableMap<String,Any>>):RecyclerView.Adapter<RecyclerListViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListViewHolder {
            val inflater= LayoutInflater.from(applicationContext)
            val view=inflater.inflate(R.layout.row,parent,false)
            view.setOnClickListener(recyclerViewClick())


            val holder=RecyclerListViewHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: RecyclerListViewHolder, position: Int) {
            val item= _listData[position]
            val id=item["id"] as Int
            val title=item["title"] as String
            val text=item["text"] as String
            val date=item["date"] as String
            val textReplaced=text.replace("\n"," ")
            val textReplaced15string=textReplaced.substring(0,15)+"..."

            if(title == "" ){
                if(textReplaced.length < 15){
                    holder.tvTitle.text=textReplaced
                }else{
                    holder.tvTitle.text=textReplaced15string
                }
            }else{
                holder.tvTitle.text=title
            }

            val dateText="更新日" + date
            holder.tvDate.text=dateText
            holder.tvId.text=id.toString()
            holder.tvId.setVisibility(View.GONE)

            val imvOption=holder.view.findViewById<ImageView>(R.id.imvOption)
            imvOption.setOnClickListener(optionClick(id.toString()))


        }

        override fun getItemCount(): Int {
            return _listData.size
        }

    }

    //    クリックリスナー
    private inner class recyclerViewClick():View.OnClickListener{
        override fun onClick(view: View) {
            val tvId=view.findViewById<TextView>(R.id.tvId)
            val id=tvId.text.toString()
            val intId=id.toInt()

            val intent=Intent(applicationContext,InputActivity::class.java)
            intent.putExtra("id",intId)
            startActivity(intent)
        }

    }

    private inner class optionClick(val id:String):View.OnClickListener{
        override fun onClick(view:View){
            val dialogFragment=DeleteConfirmDialog(id,_helper)
            dialogFragment.show(supportFragmentManager,"DeleteConfirmDialog")
        }
    }



    private inner class addListener: View.OnClickListener{
        override fun onClick(veiw: View?) {
            val intent= Intent(applicationContext,InputActivity::class.java)
            startActivity(intent)

        }
    }


    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }



}