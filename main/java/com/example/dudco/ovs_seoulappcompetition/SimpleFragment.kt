package com.example.dudco.ovs_seoulappcompetition

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by dudco on 2016. 10. 27..
 */
class SimpleFragment(res : Int) : Fragment(){
    private var res : Int? = null
    init{
        this.res = res
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(res!!,container,false)
        return view
    }
}