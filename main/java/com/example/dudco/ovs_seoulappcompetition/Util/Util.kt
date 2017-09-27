package com.example.dudco.ovs_seoulappcompetition.Util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Created by dudco on 2016. 10. 19..
 */
object Util {
    val TAG = "Dudco"
    val retrofit = Retrofit.Builder()
        .baseUrl("http://iwin247.net:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    final var user:String?= null

    fun getBtOption(size : Int): BitmapFactory.Options {
        val bt_option = BitmapFactory.Options()
        bt_option.inSampleSize = size

        return bt_option
    }

    fun Log(string: String) {
        android.util.Log.d("dudco", string)
    }
    fun removeSession(context: Context){
        val sh = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        sh.edit().remove("token").commit()
        Log("Removed!")
    }
    fun setSession(context: Context, token: String?){
        val sh = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        sh.edit().putString("token", token).commit()
        Log("Saved!")
    }
    fun getSession(context: Context) : String?{
        val sh : SharedPreferences? = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        if(sh != null)
            return sh.getString("token", null)
        else
            return null
    }
    fun getPX(resources : Resources, dp : Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
    fun getSP2Px(resources: Resources, sp : Float): Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
    }
    fun setUserInfo(token: String){
        user = token
    }
    fun getUserInfo() = user

//    fun getRealPathFromURI(contentUri: Uri, context: Context): String {
//        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
//        val cursor = .managedQuery(contentUri, proj, null, null, null)
//        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        return cursor.getString(column_index)
//
//    }


    fun saveBitmapToFileCach(bitmap :Bitmap, strFilePath : String) : File{
        val fileCachItem : File = File(strFilePath)
        var out : OutputStream? = null

        fileCachItem.createNewFile()
        out = FileOutputStream(fileCachItem)

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        return fileCachItem
    }

}
