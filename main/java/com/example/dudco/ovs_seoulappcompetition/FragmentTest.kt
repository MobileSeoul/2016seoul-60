package com.example.dudco.ovs_seoulappcompetition

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by dudco on 2016. 10. 20..
 */

class FragmentTest(val string : String) : Fragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment, container,false)
        val text : TextView= view.findViewById(R.id.fragmentText) as TextView
        text.text = string
        return view
    }




}