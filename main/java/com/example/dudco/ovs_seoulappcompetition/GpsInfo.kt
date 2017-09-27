package com.example.dudco.ovs_seoulappcompetition

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast

/**
 * Created by dudco on 2016. 10. 28..
 */
class GpsInfo(private val mContext: Context) : Service(), LocationListener {

    // 현재 GPS 사용유무
    internal var isGPSEnabled = false

    // 네트워크 사용유무
    internal var isNetworkEnabled = false

    // GPS 상태값
    /**
     * GPS 나 wife 정보가 켜져있는지 확인합니다.
     */
    var isGetLocation = false
        internal set

    internal var location: Location? = null
    internal var lat: Double = 0.toDouble() // 위도
    internal var lon: Double = 0.toDouble() // 경도

    protected var locationManager: LocationManager? = null

    init {
        getLocation()
    }

    fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // GPS 정보 가져오기
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
                Toast.makeText(mContext, "GPS or Network not enabled!!", Toast.LENGTH_SHORT).show()
                return null
            } else {
                this.isGetLocation = true
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)

                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            // 위도 경도 저장
                            lat = location!!.latitude
                            lon = location!!.longitude
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                        if (locationManager != null) {
                            location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                lat = location!!.latitude
                                lon = location!!.longitude
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        if(location == null){
            showSettingsAlert()
        }else{
            return location!!
        }
        return null
    }

    /**
     * GPS 종료
     */
    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GpsInfo)
        }
    }

    /**
     * 위도값을 가져옵니다.
     */
    val latitude: Double
        get() {
            if (location != null) {
                lat = location!!.latitude
            }
            return lat
        }

    /**
     * 경도값을 가져옵니다.
     */
    val longitude: Double
        get() {
            if (location != null) {
                lon = location!!.longitude
            }
            return lon
        }

    /**
     * GPS 정보를 가져오지 못했을때
     * 설정값으로 갈지 물어보는 alert 창
     */
    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)

        alertDialog.setTitle("GPS 사용유무셋팅")
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다.\n 설정창으로 가시겠습니까?")

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings"
        ) { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel"
        ) { dialog, which -> dialog.cancel() }

        alertDialog.show()
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        // TODO Auto-generated method stub

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        // TODO Auto-generated method stub

    }

    override fun onProviderEnabled(provider: String) {
        // TODO Auto-generated method stub


    }

    override fun onProviderDisabled(provider: String) {
        // TODO Auto-generated method stub

    }

    companion object {

        // 최소 GPS 정보 업데이트 거리 10미터
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10

        // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
        private val MIN_TIME_BW_UPDATES = 1000 * 60 * 1.toLong()
    }
}