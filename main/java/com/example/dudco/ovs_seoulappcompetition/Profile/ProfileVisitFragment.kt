package com.example.dudco.ovs_seoulappcompetition.Profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.dudco.ovs_seoulappcompetition.IntroductionActivity
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.LocationData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSData
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.example.dudco.ovs_seoulappcompetition.Views.CustomCardView
import com.example.dudco.ovs_seoulappcompetition.Views.CustomProfileCardView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.util.*

/**
 * Created by dudco on 2016. 10. 30..
 */
class ProfileVisitFragment(items: Array<OVSData>) : Fragment(){
    val items = items
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_profile_visit,container, false)
        val recycler = view.findViewById(R.id.profile_visit_recycler) as RecyclerView

        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(view.context)

        val adapter = MyAdapter(items)
        recycler.adapter = adapter

        return view
    }

    inner class MyAdapter(items : Array<OVSData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        val items : Array<OVSData> = items
        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_profile_visit, null)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            holder?.tab?.let {
                val margin = Util.getPX(resources, 3.0f).toInt()
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.getPX(resources, 113.0f).toInt())
                lp.setMargins(margin,margin,margin,margin)
                holder.conatin.layoutParams = lp
                it.setPadding(margin,margin,margin,margin)
                it.setRank(1)
                Util.Log(items[0].toString())
                it.setTileText(items[position].name!!)
                it.setSubTitleText(items[position].name_eng!!)
                it.imageView!!.addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
                    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        Picasso.with(context).load(items[position].img_url).resize(right - left, bottom - top).into(it.imageView)
                    }
                })
                it.setRank(1)
                it.setStarNum(5)
                it.setOnClickListener {
                    Util.Log("id - ${items[position].id}")
                    val intent = Intent(context, IntroductionActivity::class.java)
                    intent.putExtra("id", items[position].id)
                    startActivity(intent)
                }
            }
        }

        inner class MyViewHolder : RecyclerView.ViewHolder{
            val tab : CustomProfileCardView
            val conatin : LinearLayout
            constructor(itemView: View?) : super(itemView){
                tab = itemView!!.findViewById(R.id.profile_visit_tab) as CustomProfileCardView
                conatin = itemView.findViewById(R.id.tab_contain) as LinearLayout
            }
        }
    }
}