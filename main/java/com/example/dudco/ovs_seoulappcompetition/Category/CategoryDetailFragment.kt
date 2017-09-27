package com.example.dudco.ovs_seoulappcompetition.Category

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.dudco.ovs_seoulappcompetition.IntroductionActivity
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.OVSData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.example.dudco.ovs_seoulappcompetition.Views.CategoryView
import com.example.dudco.ovs_seoulappcompetition.Views.CustomCardView
import com.example.dudco.ovs_seoulappcompetition.Views.CustomDetailCardView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.util.*

/**
 * Created by dudco on 2016. 10. 29..
 */

class CategoryDetailFragment : Fragment(){
    var datas : Array<OVSData>? = null
    var recycler : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extra : Bundle = arguments
        CategoryAsyn().execute(extra.get("id").toString()).get()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_categorydetail, container, false)

//        CategoryAsyn().execute(extra.get("id").toString())
        recycler = view.findViewById(R.id.category_detail_recycler) as RecyclerView
        recycler!!.setHasFixedSize(true)
        recycler!!.layoutManager = LinearLayoutManager(view.context)

        return view
    }

    fun start(){
        val items : ArrayList<CategoryDetailItem> =  ArrayList()
        for(data in datas!!){
            items.add(CategoryDetailItem(data.name.toString(), data.name_eng.toString(),3,data.img_url.toString(),data.id.toString()))
        }
        val adapter = MyAdapter(items)
        recycler!!.adapter = adapter
    }

    inner class MyAdapter(items : ArrayList<CategoryDetailItem>) : RecyclerView.Adapter<MyAdapter.viewholder>() {
        val items = items
        override fun onBindViewHolder(holder: viewholder?, position: Int) {
            val item = items[position]
            holder!!.categoryItem!!.imageView!!.addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
                override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                    Picasso.with(context).load(item.image_url).resize(right - left, bottom - top).into(holder.categoryItem!!.imageView!!)
                }
            })
            (holder!!.categoryItem as CustomDetailCardView).apply{
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.getPX(resources, 230.0f).toInt())
                val margin = Util.getPX(resources, 10.0f).toInt()
                lp.setMargins(margin,margin,margin,margin)
                layoutParams = lp

                setStarsNum(item.starNum)
                setSubTitleText(item.sub_title)
                setTitleText(item.title)
//                Picasso.with(context).load(item.image_url).into(object : Target{
//                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//
//                    }
//
//                    override fun onBitmapFailed(errorDrawable: Drawable?) {
//                    }
//
//                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                        setTitleImage(bitmap!!)
//                    }
//                })
                setOnClickListener{
                    val intent = Intent(context, IntroductionActivity::class.java)
                    intent.putExtra("id", item.id)
                    startActivity(intent)
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): viewholder {
            val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_category_detail, null)
            return viewholder(v)
        }

        inner class viewholder : RecyclerView.ViewHolder {
            var categoryItem : CustomDetailCardView? = null
            constructor(v: View?) : super(v){
                categoryItem = v!!.findViewById(R.id.detail_card_tab) as CustomDetailCardView
            }
        }
    }

    inner class CategoryAsyn : AsyncTask<String, Void, JsonArray>() {
        var data : Array<OVSData>? = null
            get
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): JsonArray? {
            val net = Util.retrofit.create(OVSService::class.java).cate(params[0]!!).execute()
            if(net.code() == 200){
                return net.body()
            }
            return null
        }

        override fun onPostExecute(result: JsonArray?) {
            result?.let {
                val gson : Gson = Gson()
                val collectionType = object : TypeToken<Collection<OVSData>>() {}.type
                val enums : Collection<OVSData> = gson.fromJson(result, collectionType)

                datas = enums.toTypedArray()
                start()
            }
        }
    }
}