package com.example.dudco.ovs_seoulappcompetition.Auth

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.Toast
import com.example.dudco.ovs_seoulappcompetition.MainActivity
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.UserData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    val REGISTER_ACTIVITY = 1234
    private var callbackManager: CallbackManager? = null
    var context : Context? = null
    val handler = Handler()
    var token : String? = null
    var isAnim = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //배경화면 비트맵화/리사이징
        context = applicationContext
        var options = BitmapFactory.Options()
        options.inSampleSize = 5
        container.background = BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.background, options))

        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = com.facebook.CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult) {
                FBreg_Asyn().execute(result.accessToken.token)
            }

            override fun onError(error: FacebookException) {
                Log.e("test", "Error: " + error)
            }

            override fun onCancel() {
            }
        })

        image_facebook.setOnClickListener { LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity, Arrays.asList("public_profile")) }
        image_google.setOnClickListener { AlertDialog.Builder(this@LoginActivity).setTitle("구글로 로그인").setMessage("준비 중 입니다").setPositiveButton("확인", { dialog, which -> dialog.dismiss() }).show() }
        image_twiter.setOnClickListener { AlertDialog.Builder(this@LoginActivity).setTitle("트위터로 로그인").setMessage("준비 중 입니다").setPositiveButton("확인", { dialog, which -> dialog.dismiss() }).show() }
        findpass.setOnClickListener {  }
        register.setOnClickListener { startActivityForResult(Intent(applicationContext, RegisterActivity::class.java), REGISTER_ACTIVITY) }
        login_btn.setOnClickListener { Login_Asyn().execute(edit_email.text.toString(), edit_pass.text.toString()) }

        token = Util.getSession(this)
        Util.Log(token.toString())

        container_title.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if(!isAnim){
                startAni()
                isAnim = true
            }
        }
    }

    fun startAni(){
        val statusbar = getStatusBarSizeOnCreate()

        val location = IntArray(2)
        container_title.getLocationOnScreen(location)

        var margin = Util.getPX(resources,statusbar.toFloat())
        margin += Util.getPX(resources,16.0f)
        margin += resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"))
//        Util.Log("${(container_title.height)/2}    ${dHeight/2.0f - (container_title.height)/2 - margin}")
        val trans_anim = TranslateAnimation(0f,0f,location[1].toFloat() - margin,0f)
//        val trans_anim = TranslateAnimation(Animation.ABSOLUTE,0f,Animation.ABSOLUTE ,0f,Animation.START_ON_FIRST_FRAME,0f,Animation.ABSOLUTE,0f)
        trans_anim.duration = 1000
        val show_anim = AlphaAnimation(0f,1f)
        show_anim.duration = 1000
        show_anim.fillAfter = true

        handler.postDelayed({
            if (token == null) {
                container_title.startAnimation(trans_anim)
                Thread.sleep(1000)
                login_form.apply {
                    startAnimation(show_anim)
                    visibility = View.VISIBLE
                }
                oauth_form.apply {
                    startAnimation(show_anim)
                    visibility = View.VISIBLE
                }
                etc_form.apply {
                    startAnimation(show_anim)
                    visibility = View.VISIBLE
                }
//                    Util.Log(container_title.y.toString())
            } else {
                Auto_login_Asyn().execute(token!!)
            }
        }, 3000)
    }

    private val LOW_DPI_STATUS_BAR_HEIGHT = 19
    private val MEDIUM_DPI_STATUS_BAR_HEIGHT = 25
    private val HIGH_DPI_STATUS_BAR_HEIGHT = 38

    private fun getStatusBarSizeOnCreate(): Int {
        val displayMetrics = DisplayMetrics()
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)

        val statusBarHeight: Int

        when (displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_HIGH -> statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT
            DisplayMetrics.DENSITY_MEDIUM -> statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT
            DisplayMetrics.DENSITY_LOW -> statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT
            else -> statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT
        }
        Log.i("StatusBarTest", "onCreate StatusBar Height= " + statusBarHeight)
        return statusBarHeight
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            64206 -> {
                callbackManager!!.onActivityResult(requestCode, resultCode, data)
            }
            REGISTER_ACTIVITY -> {
                if(resultCode == Activity.RESULT_OK){
                    Auto_login_Asyn().execute(data!!.getStringExtra("token"))
                }
            }
        }
    }

    override fun onDestroy() {
        recycleView(container)
//        dialog.dismiss()
        super.onDestroy()
    }

    private fun recycleView(view: View?) {
        if (view != null) {
            val bg = view.background
            if (bg != null) {
                bg.callback = null
                (bg as BitmapDrawable).bitmap.recycle()
                view.background = null
            }
        }
    }

    internal inner class FBreg_Asyn : AsyncTask<String, Void, UserData>(){
        val dialog : ProgressDialog = ProgressDialog(this@LoginActivity)
        override fun onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            dialog.setMessage("로그인 중 입니다...")
            dialog.show()
        }

        override fun doInBackground(vararg params: String?): UserData {
            return Util.retrofit.create(OVSService::class.java).fb_token(params[0]!!).execute().body()
        }

        override fun onPostExecute(result: UserData?) {
            dialog.dismiss()

            Util.setSession(this@LoginActivity, result!!.token)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.putExtra("user_name",result.nick_name)
            intent.putExtra("token",result.token)
            intent.putExtra("id",result.user_id)

            startActivity(intent)
            finish()

        }
    }
    internal inner class Auto_login_Asyn : AsyncTask<String, Void, UserData>(){
        val dialog : ProgressDialog = ProgressDialog(this@LoginActivity)
        override fun onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            dialog.setMessage("로그인 중 입니다...")
            dialog.show()
        }

        override fun doInBackground(vararg params: String?): UserData? {
            val result = Util.retrofit.create(OVSService::class.java).auth_auto(params[0]!!).execute()
            if(result.code() == 200)
                return Util.retrofit.create(OVSService::class.java).auth_auto(params[0]!!).execute().body()
            return null
        }

        override fun onPostExecute(result: UserData?) {
            dialog.dismiss()

            result?.let{
                Util.setSession(this@LoginActivity, result.token)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("user_name",result.nick_name)
                intent.putExtra("token",result.token)
                intent.putExtra("id",result.user_id)

                startActivity(intent)
                finish()
                return
            }
            AlertDialog.Builder(this@LoginActivity).setTitle("오류").setMessage("아이디나 비밀번호를 확인해주세요.").setPositiveButton("확인", {
                dialog, which ->
                dialog.dismiss()
                if(Util.getSession(this@LoginActivity) != null)
                    Util.removeSession(this@LoginActivity)
                startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
            }).show()

        }
    }
    internal inner class Login_Asyn : AsyncTask<String, Void, UserData>(){
        val dialog : ProgressDialog = ProgressDialog(this@LoginActivity)
        override fun onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            dialog.setMessage("로그인 중 입니다...")
            dialog.show()
        }

        override fun doInBackground(vararg params: String?): UserData? {
            if(Util.retrofit.create(OVSService::class.java).login(params[0]!!, params[1]!!).execute().code() == 200)
                return Util.retrofit.create(OVSService::class.java).login(params[0]!!, params[1]!!).execute().body()
            return null
        }

        override fun onPostExecute(result: UserData?) {
            dialog.dismiss()
            result?.let {
                Util.setSession(this@LoginActivity, result.token)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                Util.Log("asdfasdf ${result.nick_name} ${result.token} ${result.user_id}")
                intent.putExtra("user_name", result.nick_name)
                intent.putExtra("token", result.token)
                intent.putExtra("id", result.user_id)

                startActivity(intent)
                finish()
                return
            }
            AlertDialog.Builder(this@LoginActivity).setTitle("오류").setMessage("아이디나 비밀번호를 확인해주세요.").setPositiveButton("확인", {
                dialog, which -> dialog.dismiss()
                if(Util.getSession(this@LoginActivity) != null)
                    Util.removeSession(this@LoginActivity)
                startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
            }).show()
        }
    }
}
