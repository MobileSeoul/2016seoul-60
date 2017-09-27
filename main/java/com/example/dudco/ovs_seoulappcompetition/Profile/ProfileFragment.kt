package com.example.dudco.ovs_seoulappcompetition.Profile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.androidquery.AQuery
import com.androidquery.callback.AjaxCallback
import com.androidquery.callback.AjaxStatus
import com.example.dudco.ovs_seoulappcompetition.Manifest
import com.example.dudco.ovs_seoulappcompetition.MyPagerAdapter
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.SimpleFragment
import com.example.dudco.ovs_seoulappcompetition.Util.*
import com.example.dudco.ovs_seoulappcompetition.Views.CustomTextView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_header.*
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dudco on 2016. 10. 25..
 */
class ProfileFragment : Fragment() {
    private var image: ImageView? = null
    private var bg : ImageView? = null
    var pager : ViewPager? = null
    var width : Int? =null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_profile, container, false)
        val token = Util.getUserInfo()

        Util.Log("profile token : $token")

        if(token is String)
            UserInfo().execute(token.toString())

        image = view.findViewById(R.id.profile_header_simage) as ImageView
        bg = view.findViewById(R.id.profile_header_image) as ImageView
//        url = "https://scontent-icn1-1.xx.fbcdn.net/v/t1.0-9/14642120_901300086666791_9119321321216082450_n.jpg?oh=b7dc3da4897ff0ea64ac3ff0ddd940ca&oe=58AB4A7D"
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        pager = view.findViewById(R.id.profile_pager) as ViewPager
        pager!!.layoutParams = lp

        val tab = view.findViewById(R.id.profile_tab_text) as LinearLayout
        tab.getChildAt(0).setOnClickListener { pager!!.setCurrentItem(0, true) }
        tab.getChildAt(1).setOnClickListener { pager!!.setCurrentItem(1, true) }

        pager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val currnetTabIndex = Math.round(position + positionOffset)
                val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, positionOffset + position)
                profile_tab_margin.layoutParams = lp
                for(i in 0..1){
                    (profile_tab_text.getChildAt(i) as CustomTextView).setTextColor(resources.getColor(R.color.profile_tab_color, context.theme))
                }
                (profile_tab_text.getChildAt(currnetTabIndex) as CustomTextView).setTextColor(resources.getColor(R.color.colorPrimary, context.theme))
            }

            override fun onPageSelected(position: Int) {
            }
        })
        pager!!.offscreenPageLimit = 2
        view.findViewById(R.id.profile_header_simage).setOnClickListener {
            reqPermission()
        }
        return view
    }
    fun reqPermission(){
        if(context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED &&
                context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
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
        val ab = AlertDialog.Builder(context)
        val intent = Intent()
        ab.setTitle("프로필 변경")
        ab.setItems(arrayOf("카메라", "앨범"), { dialogInterface, which ->
            Toast.makeText(context, which.toString(), Toast.LENGTH_SHORT).show()
            if(which == 0){
                intent.action = MediaStore.ACTION_IMAGE_CAPTURE
                startActivityForResult(intent, 1234)
            }else if(which == 1){
                intent.action = Intent.ACTION_PICK
                intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(intent, 2345)
            }
        })
        ab.show()
    }

    fun start(favorite: Array<OVSData>, visit : Array<OVSData>){
        val adapter = MyPagerAdapter(fragmentManager)
        adapter.addFragment(ProfileFavoriteFragment(favorite))
        adapter.addFragment(ProfileVisitFragment(visit))
        pager!!.adapter = adapter
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view!!.post {
            val lp = LinearLayout.LayoutParams(profile_scroll.width, profile_scroll.height - profile_tab.height)
            val maxheight = profile_header.height
            val headerparam = profile_header.layoutParams
            profile_pager.layoutParams = lp
            profile_scroll.setOnScrollChangeListener { nestedScrollView: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                if(headerparam.height < 0)
                    headerparam.height = 0
                headerparam.height = Math.round(maxheight - (scrollY/2.5)).toInt()
//                headerparam.height / m
//                Util.Log("$scrollY, ${headerparam.height}")
                profile_header.layoutParams = headerparam
            }
        }
    }

    inner class UserInfo : AsyncTask<String, Void, RealUserData>(){
        override fun doInBackground(vararg params: String?): RealUserData? {
            val net = Util.retrofit.create(OVSService::class.java).user(params[0]!!).execute()
            if(net.code() == 200){
                return net.body()
            }
            return null
        }

        override fun onPostExecute(result: RealUserData?) {
            Util.Log("${result!!.nick_name}${result.profile_image},${result.favorit},${result.visit},")
            profile_name.text = result.nick_name
            GetImageAsyn().execute(result.profile_image)
            val gson : Gson = Gson()
            val collectionType = object : TypeToken<Collection<OVSData>>() {}.type
            val enums_visit : Collection<OVSData> = gson.fromJson(result.visit.toList().toString(), collectionType)
            val enums_favorite : Collection<OVSData> = gson.fromJson(result.favorit.toList().toString(), collectionType)
            val favorite = enums_favorite.toTypedArray()
            val visit = enums_visit.toTypedArray()

//            Util.Log("Location Data : ${favorite[1].id}")

            start(favorite, visit)
        }
    }

    inner class GetImageAsyn : AsyncTask<String, Void, Bitmap>(){

        override fun doInBackground(vararg params: String?): Bitmap {
            val dm = context.getResources().getDisplayMetrics()
            width = dm.widthPixels
            return Picasso.with(context).load(params[0]).centerCrop().resize(width!!,Util.getPX(resources, 200.0f).toInt()).get()
        }

        override fun onPreExecute() {
        }

        override fun onPostExecute(result: Bitmap?) {
            bg!!.setImageBitmap(result)
            val a = RoundedAvatarDrawable(result!!, resources)
            a.setAntiAlias(true)
            val b = a.drawableToBitmap(image!!)

            image!!.setImageBitmap(b)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            1234 -> {
                Util.Log(data!!.extras.get("data").toString())
                val _image : Bitmap = data.extras.get("data") as Bitmap
                val r_image = Bitmap.createScaledBitmap(_image, width!!, Util.getPX(resources, 200.0f).toInt(),true)

                bg!!.setImageBitmap(r_image)
                val a = RoundedAvatarDrawable(r_image, resources)
                a.setAntiAlias(true)
                val b = a.drawableToBitmap(image!!)

                image!!.setImageBitmap(b)

                val aq = AQuery(context)
//                val file : File = File(data.data.toString())
                val file = Util.saveBitmapToFileCach(r_image, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath+"/"+ SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()))

                val params = HashMap<String, Any>()
                params.put("file", file)
                params.put("token", Util.getUserInfo()!!)

                aq.ajax("http://iwin247.net:3000/users/setProfile", params, JSONObject::class.java, object : AjaxCallback<JSONObject>() {
                    override fun callback(url: String?, `object`: JSONObject?,
                                          status: AjaxStatus?) {
                        Log.d("dudco", url!!.toString())
                        Log.d("dudco", status!!.toString())
                        Log.d("dudco", `object`!!.toString())
                    }
                })
            }
            2345 -> {
//                val uri : Uri = data!!.data
//                Util.Log(uri.path.toString())
                val _image : Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, data!!.data)
                val r_image = Bitmap.createScaledBitmap(_image, width!!, Util.getPX(resources, 200.0f).toInt(),true)

                bg!!.setImageBitmap(r_image)
                val a = RoundedAvatarDrawable(r_image, resources)
                a.setAntiAlias(true)
                val b = a.drawableToBitmap(image!!)

                image!!.setImageBitmap(b)

                val aq = AQuery(context)
//                val file : File = File(data.data.toString())
                val file = Util.saveBitmapToFileCach(r_image, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath+"/"+ SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()))

                val params = HashMap<String, Any>()
                params.put("file", file)
                params.put("token", Util.getUserInfo()!!)

                aq.ajax("http://iwin247.net:3000/users/setProfile", params, JSONObject::class.java, object : AjaxCallback<JSONObject>() {
                    override fun callback(url: String?, `object`: JSONObject?, status: AjaxStatus?) {
                        Log.d("dudco", url!!.toString())
                        Log.d("dudco", status!!.code.toString())
                        Log.d("dudco", `object`!!.toString())
                    }
                })
            }
        }
    }
}

