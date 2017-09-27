package com.example.dudco.ovs_seoulappcompetition.Profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.dudco.ovs_seoulappcompetition.IntroductionActivity
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.OVSData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.example.dudco.ovs_seoulappcompetition.Views.CustomCardView
import com.example.dudco.ovs_seoulappcompetition.Views.CustomFlowLayout
import com.example.dudco.ovs_seoulappcompetition.Views.CustomProfileCardView
import com.example.dudco.ovs_seoulappcompetition.Views.CustomTextView
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_introduction.*
import kotlinx.android.synthetic.main.fragment_profile_favorite.*
import java.util.*

/**
 * Created by dudco on 2016. 10. 30..
 */
class ProfileFavoriteFragment(items : Array<OVSData>) : Fragment(){
    val items = items

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_profile_favorite, container, false)
        for(i in items) {
            val lp = LinearLayout.LayoutParams(Util.getPX(resources, 200.0f).toInt(), Util.getPX(resources, 230.0f).toInt())
            val cardview = CustomCardView(context)
            cardview.layoutParams = lp
            cardview.setId(i.id!!)
            cardview.setfill(Util.getUserInfo()!!)
            cardview.setTitleText(i.name!!)
            cardview.setSubTitleText(i.name_eng!!)
            cardview.setStarsNum(5)
            cardview.setForumText(i.board_ids!!.size().toString())
            cardview.setFaboriteText(i.like.toString())
            Util.Log(i.id.toString())

            cardview.image_title!!.addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
                override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                    Picasso.with(context).load(i.img_url!!).resize(right - left, bottom - top).into(cardview.image_title)
                }
            })
            cardview.setOnClickListener {
                Util.Log("id - ${i.id}")
                val intent = Intent(context, IntroductionActivity::class.java)
                intent.putExtra("id", i.id)
                startActivity(intent)
            }

            (view.findViewById(R.id.profile_favorite_form) as CustomFlowLayout).setHorizontalSpacing(10)
            (view.findViewById(R.id.profile_favorite_form) as CustomFlowLayout).setVerticalSpacing(10)
            (view.findViewById(R.id.profile_favorite_form) as CustomFlowLayout).addView(cardview)

        }

        return view
    }



}