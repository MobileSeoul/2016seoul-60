package com.example.dudco.ovs_seoulappcompetition

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.RealUserData
import com.example.dudco.ovs_seoulappcompetition.Util.RoundedAvatarDrawable
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dudco on 2016. 10. 31..
 */
class PostActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        Util.Log("${intent.getStringExtra("id")}")
        getUserInfoAsyn().execute(Util.getUserInfo())
        post_floating.setOnClickListener {
            reqPermission()
        }
        post_back.setOnClickListener {
            finish()
        }
        post_upload.setOnClickListener {
            upload()
        }
    }
    fun reqPermission(){
        if(applicationContext.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED &&
                applicationContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),112)
        }else{
            showDialog()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 112){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                showDialog()
            }
        }
    }

    fun showDialog(){
        val ab = AlertDialog.Builder(this@PostActivity)
        val _intent = Intent()
        ab.setTitle("업로드")
        ab.setItems(arrayOf("카메라", "앨범"), { dialogInterface, which ->
            Toast.makeText(this@PostActivity, which.toString(), Toast.LENGTH_SHORT).show()
            if(which == 0){
                _intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                startActivityForResult(_intent, 5432)
            }else if(which == 1){
                _intent.action = Intent.ACTION_PICK
                _intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(_intent, 4321)
            }
        })
        ab.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            5432 -> {
                Util.Log(data!!.extras.get("data").toString())
                val _image : Bitmap = data.extras.get("data") as Bitmap
                var r_image : Bitmap?
                post_image.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    r_image = Bitmap.createScaledBitmap(_image, right - left, bottom - top, true)
                    post_image!!.setImageBitmap(r_image)
                }

            }
            4321 -> {
                val _image : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data!!.data)
                var r_image : Bitmap?
                post_image.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    r_image = Bitmap.createScaledBitmap(_image, right - left, bottom - top, true)
                    post_image!!.setImageBitmap(r_image)
                }
            }
        }
    }

    fun upload(){
        val dialog : ProgressDialog = ProgressDialog(this@PostActivity)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setMessage("업로드 중...")
        dialog.show()

        val aq = AQuery(applicationContext)
        post_image.buildDrawingCache()
        val bmap = post_image.drawingCache
        val file = Util.saveBitmapToFileCach(bmap, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath+"/"+ SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()))

        val params = HashMap<String, Any>()
        params.put("file", file)
        params.put("token", Util.getUserInfo()!!)
        params.put("tourid", intent.getStringExtra("id"))
        params.put("contents", post_content.text)

        aq.ajax("http://iwin247.net:3000/board/write", params, JSONObject::class.java, object : AjaxCallback<JSONObject>() {
            override fun callback(url: String?, `object`: JSONObject?, status: AjaxStatus?) {
                dialog.dismiss()
                Log.d("dudco", url!!.toString())
                Log.d("dudco", status!!.code.toString())
                Log.d("dudco", `object`!!.toString())
                finish()
            }
        })
    }
    inner class getUserInfoAsyn : AsyncTask<String, Void, RealUserData>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): RealUserData? {
            val net = Util.retrofit.create(OVSService::class.java).user(params[0]!!).execute()
            if(net.code() == 200){
                return net.body()
            }
            return null
        }

        override fun onPostExecute(result: RealUserData?) {
            result?.let {
                Util.Log("user - ${result.profile_image}, ${result.nick_name}")
                post_writer_image.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    Picasso.with(applicationContext).load(result.profile_image).resize(right - left, bottom - top).into(post_writer_image,object : Callback{
                        override fun onSuccess() {
                            post_writer_image.buildDrawingCache()
                            val bmap = post_writer_image.drawingCache
                            val a = RoundedAvatarDrawable(bmap, resources)
                            a.setAntiAlias(true)
                            a.setColor(resources.getColor(R.color.colorPrimary, theme))
                            a.setWidth(Util.getPX(resources, 3.0f))
                            val b = a.drawableToBitmap(post_writer_image)
                            post_writer_image.setImageBitmap(b)
                        }
                        override fun onError() {
                            Log.e("dudco","ERROR!")
                        }
                    })
                }

                val time = SimpleDateFormat("yyyy년 MM월 dd일").format(Date(System.currentTimeMillis()))
                post_writer.text = result.nick_name
                post_date.text = time
            }
        }
    }
}