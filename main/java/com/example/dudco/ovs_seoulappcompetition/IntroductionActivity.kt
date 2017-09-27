package com.example.dudco.ovs_seoulappcompetition

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.dudco.ovs_seoulappcompetition.Util.*
import com.example.dudco.ovs_seoulappcompetition.Views.CustomFlowLayout
import com.example.dudco.ovs_seoulappcompetition.Views.CustomTextView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_introduction.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dudco on 2016. 10. 30..
 */

class IntroductionActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        intro_back.setOnClickListener { finish() }
        floatingbtn.setOnClickListener {
            val _intent = Intent(this@IntroductionActivity, PostActivity::class.java)
            _intent.putExtra("id", intent.getStringExtra("id"))
            startActivityForResult(_intent, 5678)
        }
        Util.Log("${intent.getStringExtra("id")} asdfadfasdfadfafasdfadsfasdfadfadf")
        DataAsyn().execute(intent.getStringExtra("id").toString())
    }

    inner class DataAsyn : AsyncTask<String, Void, OVSData>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): OVSData? {
            val net = Util.retrofit.create(OVSService::class.java).tour(params[0]!!).execute()
            if(net.code() == 200){
                return net.body()
            }
            return null
        }

        override fun onPostExecute(result: OVSData?) {
            result?.let {
                Util.Log("${result.board_ids}")
                //info NULL
                start(it.img_url!!,it.name!!,it.name_eng!!, 5, it.info!!, it.tag!!, it.adress!!, it.phoneNum,it.board_ids)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 5678){
            DataAsyn().execute(intent.getStringExtra("id").toString())
        }
    }

    fun start(imge_url : String, name : String, name_en : String, starNum : Int, info : String, tags: Array<String>, location : String, callNum : String?, board : JsonArray?){
        Picasso.with(applicationContext).load(imge_url).into(object :Target{
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
//                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                intro_bg.setImageBitmap(bitmap)
            }

        })

//        intro_bg.addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
//            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
//                Picasso.with(applicationContext).load(imge_url).resize(right - left, bottom - top).into(intro_bg)
//            }
//        })
        intro_title.text = name
        intro_subtitle.text = name_en
        for(i in 0..starNum-1){
            (intro_star.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_fill)
        }
        intro_info.text = info
        for(i in tags) {
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            val tag = CustomTextView(applicationContext)
            tag.layoutParams = lp
            tag.setFont("nanumbarungothic_0.ttf")
            tag.setPadding(Util.getPX(resources, 15.0f).toInt(), Util.getPX(resources, 8.0f).toInt(), Util.getPX(resources, 15.0f).toInt(), Util.getPX(resources, 8.0f).toInt())
            tag.background = resources.getDrawable(R.drawable.visitnum_shape, applicationContext.theme)
            tag.setTextColor(resources.getColor(R.color.white, applicationContext.theme))
            tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.0f)
            tag.text = i

            tag_form.setHorizontalSpacing(10)
            tag_form.setVerticalSpacing(10)
            (tag_form as CustomFlowLayout).addView(tag)
        }
        intro_location.text = location
        if(callNum!= null){
            call.text = callNum
        }else{
            call_form.removeAllViewsInLayout()
        }
        val gson : Gson = Gson()
        val collectionType = object : TypeToken<Collection<BoardData>>() {}.type
        val enums : Collection<BoardData> = gson.fromJson(board, collectionType)
        val datas = enums.toTypedArray()
        val items = ArrayList<BoardData>()
        for(data in datas) {
            items.add(data)
        }
        val adpater = MyAdapter(items)
        review_recycler.adapter = adpater
        review_recycler.setHasFixedSize(true)
        review_recycler.layoutManager = LinearLayoutManager(applicationContext)
    }

    inner class MyAdapter(items: ArrayList<BoardData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
        val items = items
        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            val item = items[position]
            holder?.let{
                var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                it.contain!!.layoutParams = lp
                it.writer!!.text = item.board_writer
                it.mContent!!.text = item.contents
                it.mDate!!.text = SimpleDateFormat("yyyy년 MM월 dd일").format(item.date)



                it.writer_image!!.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    Picasso.with(applicationContext).load(item.writer_img).resize(right - left, bottom - top).into(it.writer_image, object : Callback{
                        override fun onSuccess() {
                            it.writer_image!!.buildDrawingCache()
                            val bmap = it.writer_image!!.drawingCache
                            val a = RoundedAvatarDrawable(bmap, resources)
                            a.setAntiAlias(true)
                            a.setColor(resources.getColor(R.color.colorPrimary, theme))
                            a.setWidth(Util.getPX(resources, 3.0f))
                            val b = a.drawableToBitmap(it.writer_image!!)
                            it.writer_image!!.setImageBitmap(b)
                        }

                        override fun onError() {
                            Toast.makeText(applicationContext, "Error!", Toast.LENGTH_SHORT).show()
                        }

                    })


                }
                Util.Log("asdfasdfasdfasdf ${item.img_url}")
                if(item.img_url.equals("1.0"))
                    it.mImage!!.visibility = LinearLayout.GONE
                else {
                    it.mImage!!.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                        Picasso.with(applicationContext).load(item.img_url).resize(right - left, bottom - top).into(it.mImage)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_review, null)
            return MyViewHolder(v)
        }

        inner class MyViewHolder : RecyclerView.ViewHolder{
            var writer_image : ImageView? = null
            var writer : CustomTextView? = null
            var mDate : CustomTextView? = null
            var mContent : CustomTextView? = null
            var mImage : ImageView? = null
            var contain : LinearLayout? = null
            constructor(itemView: View?) : super(itemView){
                itemView?.let{
                    writer_image = it.findViewById(R.id.review_writer_image) as ImageView
                    writer = it.findViewById(R.id.review_writer) as CustomTextView
                    mDate = it.findViewById(R.id.review_date) as CustomTextView
                    mContent = it.findViewById(R.id.review_content) as CustomTextView
                    mImage = it.findViewById(R.id.review_image) as ImageView
                    contain = it.findViewById(R.id.review_mcontain) as LinearLayout
                }
            }
        }
    }

}