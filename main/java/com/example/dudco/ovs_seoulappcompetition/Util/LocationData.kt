package com.example.dudco.ovs_seoulappcompetition.Util

import com.google.gson.annotations.SerializedName

/**
 * Created by dudco on 2016. 10. 28..
 */
class LocationData(){
    @SerializedName("id")
    val id : String? = null
    @SerializedName("gps")
    var gps : Array<String>? = null
}