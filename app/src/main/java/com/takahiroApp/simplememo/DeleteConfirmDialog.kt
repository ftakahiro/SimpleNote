package com.takahiroApp.simplememo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class DeleteConfirmDialog(val id:String ,val _helper:DatabaseHelper): DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder= AlertDialog.Builder(activity)
        builder.setTitle(R.string.dialog_title)
        builder.setMessage(R.string.dialog_text)
        builder.setPositiveButton(R.string.dialog_bt_delete,DialogButtonClickListener())
        builder.setNegativeButton(R.string.dialog_bt_cancel,DialogButtonClickListener())
        val dialog=builder.create()


        return dialog
    }

    private inner class DialogButtonClickListener(): DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val msg:String

            when(which){
                DialogInterface.BUTTON_POSITIVE->{
                    msg="削除しました。"
                    deleteData(id.toInt())
                    val intent= Intent(activity,MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show()
                }
                DialogInterface.BUTTON_NEGATIVE->{
                    msg="キャンセルしました。"
                    Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show()
                }


            }


        }

    }

    private fun deleteData(id:Int){
        val db=_helper.writableDatabase
        val sqlDelete="DELETE FROM simplememo WHERE _id= ${id}"
        val stmt=db.compileStatement(sqlDelete)
        stmt.executeUpdateDelete()

    }




}