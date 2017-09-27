package com.example.dudco.ovs_seoulappcompetition.Util

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * Created by dudco on 2016. 10. 30..
 */
data class RealUserData(val nick_name : String, val profile_image : String, val favorit : Array<JsonObject>, val visit : Array<JsonObject>)