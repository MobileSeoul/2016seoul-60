package com.example.dudco.ovs_seoulappcompetition.Main_Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Views.CustomTextView
import com.squareup.picasso.Picasso

/**
 * Created by dudco on 2016. 10. 31..
 */
class HomeFestivalFragment(_image : String, _title : String, _page : String, _subtitle : String?) : Fragment(){
    var image : String? = null
    var title : String? = null
    var page : String? = null
    var sub : String? = null
    init {
        image = _image
        title = _title
        page = _page
        sub = _subtitle
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_image, container, false)
        val title_text = view.findViewById(R.id.main_title_text) as CustomTextView
        title_text.text = title
        val title_image = view.findViewById(R.id.main_title_image) as ImageView
        title_image.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Picasso.with(context).load(image).resize(right - left, bottom - top).into(title_image)
        }
        view.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(page)))
        }
        if(sub != null){
            (view.findViewById(R.id.main_title_subtext) as CustomTextView).text = sub
        }
        return view
    }
}