package com.example.dudco.ovs_seoulappcompetition

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.dudco.ovs_seoulappcompetition.Category.CategoryFragment
import com.example.dudco.ovs_seoulappcompetition.Main_Home.HomeFragment
import com.example.dudco.ovs_seoulappcompetition.Profile.ProfileFragment
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.w3c.dom.Text
import java.security.Permission
import java.util.jar.Manifest

/**
 * Created by dudco on 2016. 10. 20..
 */
class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar_main)

        if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),123)
        }else{
            startApp()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 123){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                startApp()
            }else{
                Toast.makeText(this@MainActivity, "권한허용이 안됬습니다.", Toast.LENGTH_LONG).show()
                finish()
            }
            return
        }
    }

    fun startApp(){
        val user_name = intent.getStringExtra("user_name")
        val user_id = intent.getStringExtra("id")
        val token = intent.getStringExtra("token")
        Util.setUserInfo(token)
        Util.Log("user_name : $user_name   user_id : $user_id   token : $token")

        val tabs = arrayOf(main_tab_home, main_tab_category, main_tab_location, main_tab_user)
        val tab_images = mapOf(R.id.main_tab_home to R.drawable.ic_tab_home, R.id.main_tab_home+10 to R.drawable.ic_tab_home_se,
                R.id.main_tab_category to R.drawable.ic_tab_category, R.id.main_tab_category+10 to R.drawable.ic_tab_category_se,
                R.id.main_tab_location to R.drawable.ic_tab_loaction, R.id.main_tab_location+10 to R.drawable.ic_tab_loaction_se,
                R.id.main_tab_user to R.drawable.ic_tab_user, R.id.main_tab_user+10 to R.drawable.ic_tab_user_se)

        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(CategoryFragment())
        adapter.addFragment(MapFragment())
        adapter.addFragment(ProfileFragment())
        pager_main.offscreenPageLimit = 4
        pager_main.adapter = adapter
        pager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                val param = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, position + positionOffset)
                main_tab_margin.layoutParams = param

                val currnetTabIndex = Math.round(position + positionOffset)
                val currentTab = tabs[currnetTabIndex]

                for (tab in tabs){
                    (tab.getChildAt(0) as ImageView).setImageBitmap(BitmapFactory.decodeResource(resources,tab_images[tab.id]!!))
                    (tab.getChildAt(1) as TextView).setTextColor(resources.getColor(R.color.grey, theme))
                }

                (currentTab.getChildAt(0) as ImageView).setImageBitmap(BitmapFactory.decodeResource(resources, tab_images[currentTab.id+10]!!))
                (currentTab.getChildAt(1) as TextView).setTextColor(resources.getColor(R.color.colorPrimary, theme))


                when(currnetTabIndex){
                    0 -> bar_text_title.setText(R.string.oh_visit_seoul)
                    1 -> bar_text_title.text = "Category"
                    2 -> bar_text_title.text = "Map"
                    3 -> bar_text_title.text = "Profile"
                }
            }
            override fun onPageScrollStateChanged(state: Int) { }
        })

        main_tab_home.setOnClickListener { pager_main.setCurrentItem(0, true) }
        main_tab_category.setOnClickListener { pager_main.setCurrentItem(1, true) }
        main_tab_location.setOnClickListener { pager_main.setCurrentItem(2,true) }
        main_tab_user.setOnClickListener { pager_main.setCurrentItem(3, true) }
    }
}
