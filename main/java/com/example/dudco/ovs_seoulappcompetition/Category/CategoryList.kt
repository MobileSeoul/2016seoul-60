package com.example.dudco.ovs_seoulappcompetition.Category

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.OVSData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.example.dudco.ovs_seoulappcompetition.Views.CategoryView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by dudco on 2016. 10. 30..
 */
class CategoryList : Fragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_categorylist,container, false)
        val recycler = view.findViewById(R.id.category_recycler) as RecyclerView
        recycler.setHasFixedSize(false)

        val items : ArrayList<CategoryItem> = ArrayList()
        items.add(CategoryItem("lm","랜드마크","3,571","5.3k",R.drawable.category_image1))
        items.add(CategoryItem("at","관광명소","4,864","4.3k",R.drawable.category_image2))
        items.add(CategoryItem("tr","둘레길","2,897","5.0k",R.drawable.category_image3))
        items.add(CategoryItem("mt","산악","3,851","5.7k",R.drawable.category_image4))

        val adapter = MyAdapter(items)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(view.context)
        return view

    }

    inner class MyAdapter(items : ArrayList<CategoryItem>) : RecyclerView.Adapter<MyAdapter.viewholder>() {
        val items = items
        override fun onBindViewHolder(holder: viewholder?, position: Int) {
            val item = items[position]
            (holder!!.categoryItem as CategoryView).apply{
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.getPX(resources, 190.0f).toInt())
                layoutParams = lp
                setBackGroundBG(item.bg_res)
                setFaboriteNum(item.favoriteNum)
                setTitleText(item.titleText)
                setVisitNum(item.visitNum)
                setOnClickListener{
                    Util.Log("touch! ${item.id}")

                    val fragment = CategoryDetailFragment()
                    val bundle : Bundle = Bundle()
                    bundle.putString("id", item.id)
                    fragment.arguments = bundle
                    fragmentManager.beginTransaction()
                            .replace(R.id.category_container, fragment)
                            .addToBackStack(null)
                            .setCustomAnimations(R.anim.layout_bottomin,R.anim.layout_bottomout)
                            .commit()

//                    CategoryAsyn().execute(item.id)
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): viewholder {
            val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_category, null)
            return viewholder(v)
        }

        inner class viewholder : RecyclerView.ViewHolder {
            var categoryItem : CategoryView? = null
            constructor(v: View?) : super(v){
                categoryItem = v!!.findViewById(R.id.category_tab) as CategoryView
            }
        }
    }


}