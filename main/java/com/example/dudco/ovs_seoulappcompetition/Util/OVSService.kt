package com.example.dudco.ovs_seoulappcompetition.Util

import android.telecom.Call
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.*

/**
 * Created by dudco on 2016. 10. 19..
 */
interface OVSService {
    @FormUrlEncoded
    @POST("/auth/login")
    fun login(@Field("userid") id : String, @Field("pw") pw : String ) : retrofit2.Call<UserData>

    @FormUrlEncoded
    @POST("/auth/reg")
    fun register(@Field("userid") id : String, @Field("pw") pw : String, @Field("username") username : String)  : retrofit2.Call<UserData>

    @FormUrlEncoded
    @POST("/auth/auto")
    fun auth_auto(@Field("token") token : String) : retrofit2.Call<UserData>

    @GET("/auth/fb/token")
    fun fb_token(@Query("access_token") token : String) : retrofit2.Call<UserData>

    @POST("/info")
    fun info() : retrofit2.Call<JsonArray>

    @FormUrlEncoded
    @POST("/cate")
    fun cate(@Field("cate") cate: String) : retrofit2.Call<JsonArray>

    @FormUrlEncoded
    @POST("/users/getinfo")
    fun user(@Field("token") token: String) : retrofit2.Call<RealUserData>

    @POST("/main")
    fun main() : retrofit2.Call<JsonObject>

    @FormUrlEncoded
    @POST("/tour")
    fun tour(@Field("id") tour_id: String) : retrofit2.Call<OVSData>

    @FormUrlEncoded
    @POST("/tour/like")
    fun like_tour(@Field("id") id : String,@Field("token") token: String) : retrofit2.Call<Void>


    @FormUrlEncoded
    @POST("/tour/dislike")
    fun dislike_tour(@Field("id") id : String,@Field("token") token: String) : retrofit2.Call<Void>

//    @FormUrlEncoded
//    @POST("/board/write")
//    fun write(@Field("token") token: String, @Field("contents") contents : String, @Field("tourid") tourid:String, @) : retrofit2.Call<Void>
}