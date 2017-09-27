package com.example.dudco.ovs_seoulappcompetition.Util

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by dudco on 2016. 10. 31..
 */
class BoardData(){
    @SerializedName("boardid")
    var boardid : String? = null
    @SerializedName("board_writer")
    var board_writer: String? = null
    @SerializedName("writer_img")
    var writer_img: String? = null
    @SerializedName("date")
    var date: Date? = null
    @SerializedName("contents")
    var contents: String? = null
    @SerializedName("img_url")
    var img_url: String? = null
    @SerializedName("comments")
    var comments: JsonArray? = null
}