package com.example.dudco.ovs_seoulappcompetition.Main_Home

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.dudco.ovs_seoulappcompetition.Category.CategoryDetailItem
import com.example.dudco.ovs_seoulappcompetition.IntroductionActivity
import com.example.dudco.ovs_seoulappcompetition.MyPagerAdapter
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.SimpleFragment
import com.example.dudco.ovs_seoulappcompetition.Util.LocationData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.example.dudco.ovs_seoulappcompetition.Views.CustomCardView
import com.example.dudco.ovs_seoulappcompetition.Views.CustomDetailCardView
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.custom_cardview.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

/**
 * Created by dudco on 2016. 10. 22..
 */
class HomeFragment : Fragment() {
    var best_recycler : RecyclerView? = null
    var month_recycler : RecyclerView? = null
    var best_datas : Array<OVSData>? = null
    var month_datas : Array<OVSData>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_main, container, false)

        MainDataAsyn().execute()

        val adapter = MyPagerAdapter(fragmentManager)
        adapter.addFragment(HomeFestivalFragment("http://thefermata.net/wp-content/uploads/2012/10/IMG_1035.jpg", "경복궁 궁궐 공개행사 토요마당", "http://www.horangi.kr/festival_info.asp?fid=2433589",null))
        adapter.addFragment(HomeFestivalFragment("http://www.thehealingpost.com/news/photo/201610/1024_1244_5042.jpg","서울 사진축제 서울산아리랑","http://korean.visitkorea.or.kr/kor/bz15/where/festival/festival.jsp?cid=1142690","천리의 강물처럼 2016"))
        adapter.addFragment(HomeFestivalFragment("http://www.iusm.co.kr/news/photo/201511/625873_227016_281.jpg", "종묘 추향대제", "http://www.horangi.kr/festival_info.asp?fid=2433508",null))
        adapter.addFragment(HomeFestivalFragment("http://tong.visitkorea.or.kr/cms/resource/27/2415027_image2_1.jpg", "서울 김장축제2016", "http://www.horangi.kr/festival_info.asp?fid=1963038",null))
        (view.findViewById(R.id.main_fragment_pager) as ViewPager).offscreenPageLimit = 4
        (view.findViewById(R.id.main_fragment_pager) as ViewPager).adapter = adapter
        (view.findViewById(R.id.main_fragment_pager) as ViewPager).addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val currnetTabIndex = Math.round(position + positionOffset)

                for (i in 0..3){
                    (dot_form.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_dot_non)
                }

                (dot_form.getChildAt(currnetTabIndex) as ImageView).setImageResource(R.drawable.ic_doat)
            }

            override fun onPageSelected(position: Int) {
            }

        })
        val layoutManager1 : LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager2: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        best_recycler = view.findViewById(R.id.best_recycler) as RecyclerView
        best_recycler!!.layoutManager = layoutManager1
        best_recycler!!.setHasFixedSize(true)
        month_recycler = view.findViewById(R.id.month_recycler) as RecyclerView
        month_recycler!!.layoutManager = layoutManager2
        month_recycler!!.setHasFixedSize(true)

        return view
    }
    fun start() {
        val best_items: ArrayList<HomeItem> = ArrayList()
        val month_items: ArrayList<HomeItem> = ArrayList()
        for (data in best_datas!!) {
            best_items.add(HomeItem(data.img_url!!,data.name!!, data.name_eng!!, 3, data.like!!, data.board_ids!!.size(),data.id.toString()))
        }
        for (data in month_datas!!) {
            month_items.add(HomeItem(data.img_url!!,data.name!!, data.name_eng!!, 3, data.like!!, data.board_ids!!.size(),data.id.toString()))
        }
        val best_adapter = MyAdapter(best_items)
        val month_adapter = MyAdapter(month_items)

        best_recycler!!.adapter = best_adapter
        month_recycler!!.adapter = month_adapter
    }
    inner class MainDataAsyn : AsyncTask<Void, Void, JsonObject>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): JsonObject?{
            val net = Util.retrofit.create(OVSService::class.java).main().execute()
            if(net.code() == 200){
                return net.body()
            }
            return null
        }

        override fun onPostExecute(result: JsonObject?) {
            val _best = result!!.get("best")
            val _month = result.get("month")
            val gson : Gson = Gson()

            val collectionType = object : TypeToken<Collection<OVSData>>() {}.type
            val enum_best : Collection<OVSData> = gson.fromJson(_best.toString(), collectionType)
            val enum_month : Collection<OVSData> = gson.fromJson(_month.toString(), collectionType)

            best_datas = enum_best.toTypedArray()
            month_datas = enum_month.toTypedArray()

            start()
        }
    }
    inner class MyAdapter(items : ArrayList<HomeItem>) : RecyclerView.Adapter<MyAdapter.viewholder>() {
        val items = items
        override fun onBindViewHolder(holder: viewholder?, position: Int) {
            val item = items[position]
            holder!!.categoryItem!!.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                Picasso.with(context).load(item.url).resize(right - left, ((bottom - top) * 3/1.8).toInt()).into(holder.categoryItem!!.image_title)
            }

            (holder!!.categoryItem as CustomCardView).apply{
                val lp = LinearLayout.LayoutParams(Util.getPX(resources,210.0f).toInt(),Util.getPX(resources,260.0f).toInt())
                val margin = Util.getPX(resources, 10.0f).toInt()
                lp.setMargins(margin,margin,margin,margin)
                layoutParams = lp

                setStarsNum(item.starnum)
                setSubTitleText(item.subTitle)
                setTitleText(item.title)
                setForumText(item.board_len.toString())
                setFaboriteText(item.likeNum.toString())
                setId(item.id)
                setfill(Util.getUserInfo()!!)

                setOnClickListener{
                    val intent = Intent(context, IntroductionActivity::class.java)
                    Util.Log("${item.id} asdfasfdasdf")
                    intent.putExtra("id", item.id)
                    startActivity(intent)
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): viewholder {
            val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_home,null)
            return viewholder(v)
        }

        inner class viewholder : RecyclerView.ViewHolder {
            var categoryItem : CustomCardView? = null
            constructor(v: View?) : super(v){
                categoryItem = v!!.findViewById(R.id.home_card) as CustomCardView
            }
        }
    }
}