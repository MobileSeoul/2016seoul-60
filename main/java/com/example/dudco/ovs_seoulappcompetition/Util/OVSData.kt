package com.example.dudco.ovs_seoulappcompetition.Util

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.annotations.SerializedName

/**
 * Created by dudco on 2016. 10. 29..
 */
class OVSData(){
    @SerializedName("id")
    val id: String? = null
    @SerializedName("name")
    val name: String? = null
    @SerializedName("name_eng")
    val name_eng: String? = null
    @SerializedName("adress")
    val adress: String? = null
    @SerializedName("gps")
    val gps: String? = null
    @SerializedName("map")
    val map: String? = null
    @SerializedName("info")
    val info: String? = null
    @SerializedName("img_url")
    val img_url: String? = null
    @SerializedName("restaurant")
    val restaurant: String? = null
    @SerializedName("like")
    val like: Int? = null

//    @SerializedName("boards")
//    val boards: Array<>? = null
    @SerializedName("board_ids")
    val board_ids : JsonArray? = null

    @SerializedName("tag")
    val tag: Array<String>? = null
    @SerializedName("phoneNum")
    val phoneNum : String? = null
}