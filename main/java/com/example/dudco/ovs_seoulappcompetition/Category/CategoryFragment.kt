package com.example.dudco.ovs_seoulappcompetition.Category

import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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
 * Created by dudco on 2016. 10. 27..
 */
class CategoryFragment : Fragment() {
    var fragment : Fragment? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_category,container, false)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.category_container, CategoryList())
        transaction.commit()
        return view
    }
}