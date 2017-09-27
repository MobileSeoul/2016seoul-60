package com.example.dudco.ovs_seoulappcompetition

import android.content.Intent
import android.graphics.Bitmap
import com.example.dudco.ovs_seoulappcompetition.R
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.dudco.ovs_seoulappcompetition.Util.LocationData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.example.dudco.ovs_seoulappcompetition.Views.CustomProfileCardView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.custom_cardview.*
import kotlinx.android.synthetic.main.map.*

/**
 * Created by dudco on 2016. 10. 27..
 */

class MapFragment : Fragment(),GoogleMap.OnMarkerClickListener , GoogleMap.OnMapClickListener{

    var mMapView : MapView? = null
    var googleMap : GoogleMap? = null
    var selectedMarker : Marker? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.map, container, false)

        mMapView = view.findViewById(R.id.mapView) as MapView
        mMapView!!.onCreate(savedInstanceState)

        mMapView!!.onResume() // needed to get the map to display immediately

        mMapView!!.getMapAsync({ mMap ->
            googleMap = mMap
            googleMap?.let {
                it.isMyLocationEnabled = true
                setMyPostion(it)
                it.setOnMyLocationButtonClickListener {
                    setMyPostion(it)
                    true
                }
                it.setOnMarkerClickListener(this)
                it.setOnMapClickListener(this)
            }
        })
        getLoca().execute()
        return view
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

    fun setMyPostion(googleMap: GoogleMap){
        val gpsInfo = GpsInfo(context)
        val latLng = LatLng(gpsInfo.latitude, gpsInfo.longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
    }

    inner class getLoca : AsyncTask<Void, Void, JsonArray>(){
        override fun doInBackground(vararg params: Void?): JsonArray? {
            return Util.retrofit.create(OVSService::class.java).info().execute().body()
        }

        override fun onPostExecute(result: JsonArray?) {
            val gson : Gson = Gson()
            val collectionType = object : TypeToken<Collection<LocationData>>() {}.type
            val enums : Collection<LocationData> = gson.fromJson(result, collectionType)
            val LocationData = enums.toTypedArray()
//            Util.Log(LocationData[0].id.toString())
            for(i in LocationData) {
                val position = LatLng(i.gps!![0].toDouble(), i.gps!![1].toDouble())
                val id = i.id

                var markerOption : MarkerOptions = MarkerOptions()
                markerOption.position(position)
                markerOption.title(id.toString())
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.ic_small_location)))
                googleMap!!.addMarker(markerOption)
            }
        }
    }

    override fun onMarkerClick(marker : Marker?): Boolean {

        Util.Log("marker ${marker!!.title} Clicked!")

        OVSDataAsyntask().execute(marker.title)

        changeSelectedMarker(marker)

        return true
    }

    override fun onMapClick(p0: LatLng?) {
        changeSelectedMarker(null)
    }

    fun addView(marker : Marker?){
        val ani = AnimationUtils.loadAnimation(context, R.anim.show_anim)
        map_cardview.startAnimation(ani)
        map_cardview.visibility = View.VISIBLE
        map_cardview.setOnClickListener {
            val intent = Intent(context, IntroductionActivity::class.java)
            Util.Log("${marker!!.title} asdfasfdasdf")
            intent.putExtra("id", marker.title)
            startActivity(intent)
        }
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_location_big)
        marker!!.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, Util.getPX(resources, 70.0f).toInt(),Util.getPX(resources, 80.0f).toInt(),true)))

        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(marker.position))
        googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
        googleMap!!.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
    }

    fun removeView(){
        val ani = AnimationUtils.loadAnimation(context, R.anim.hide_anim)

        map_cardview.startAnimation(ani)
        map_cardview.visibility = View.GONE
    }

    private fun changeSelectedMarker(marker: Marker?) {
        // 선택했던 마커 되돌리기

        if(selectedMarker == marker){
            Util.Log("같은 마커")
            return
        }

        if (selectedMarker != null) {
            removeView()
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_small_location)
            selectedMarker!!.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
        }
        selectedMarker = marker
        // 선택한 마커 표시
        if (marker != null) {
            addView(marker)
        }

    }

    inner class OVSDataAsyntask : AsyncTask<String, Void, OVSData>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): OVSData? {
            val net = Util.retrofit.create(OVSService::class.java).tour(params[0]!!).execute()
            if(net.code() == 200){
                return net.body()
            }
            return null
        }

        override fun onPostExecute(result: OVSData?) {
            result?.let {
                map_image.addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
                    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        Picasso.with(context).load(it.img_url).resize(right - left, bottom - top).into(map_image)
                    }
                })
                map_title.text = it.name
                map_subtitle.text = it.name_eng
            }
        }
    }
}
