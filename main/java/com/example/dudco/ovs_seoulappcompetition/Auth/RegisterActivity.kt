package com.example.dudco.ovs_seoulappcompetition.Auth

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.UserData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    var isEmail = false
    var isPass = false
    var isRepass = false
    var mIntent : Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
//        context = applicationContext
        var options = BitmapFactory.Options()
        options.inSampleSize = 5
        reg_main_contain.background = BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.background, options))

        reg_edit_email.addTextChangedListener(email_textwatcher)
        reg_edit_pass.addTextChangedListener(pass_textwatcher)
        reg_edit_repass.addTextChangedListener(repass_textwatcher)
        reg_edit_name.addTextChangedListener(name_textwatcher)
//        reg_back.setOnClickListener { finish() }
        reg_btn.setOnClickListener { reg_Asyn().execute(reg_edit_email.text.toString(), reg_edit_pass.text.toString(), reg_edit_name.text.toString())}
        reg_login.setOnClickListener { finish() }
        mIntent = intent
    }

    val email_textwatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) { }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(!Pattern.matches("^[a-zA-Z0-9]+[@][a-zA-Z0-9]+[\\.][a-zA-Z0-9]+$", s)){
                reg_textinput_email.isErrorEnabled = true
                reg_textinput_email.error = "올바른 이메일 형식이 아닙니다."
            }else{
                reg_textinput_email.isErrorEnabled = false
                isEmail = true
            }
        }
    }

    val pass_textwatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) { }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(s!!.length < 9){
                reg_textinput_pass.isErrorEnabled = true
                reg_textinput_pass.error = "PassWord는 9자 이상이여야 합니다."
            }else{
                if(s.toString().matches(Regex.fromLiteral("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*)"))){

                    reg_textinput_pass.isErrorEnabled = true
                    reg_textinput_pass.error = "PassWord에는 특수문자가 1개이상 존재해야 합니다."
                }else{
                    reg_textinput_pass.isErrorEnabled = false
                    isPass = true
                }
            }
        }
    }

    val repass_textwatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if(s.toString() != reg_edit_pass.text.toString()){
                reg_textinput_repass.isErrorEnabled = true
                reg_textinput_repass.error = "비밀번호가 같지 않습니다."
            }else{
                isRepass = true
                reg_textinput_repass.isErrorEnabled = false
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    val name_textwatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(isEmail && isPass && isRepass){
                reg_btn.isEnabled = true
                reg_btn.setBackgroundResource(R.drawable.login_btn_shape)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
    }

    internal inner class reg_Asyn : AsyncTask<String, Void, Void>(){
        var dialog : ProgressDialog = ProgressDialog(this@RegisterActivity)

        override fun onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            dialog.setMessage("회원가입 중 입니다...")
            dialog.show()
        }

        override fun doInBackground(vararg params: String?): Void? {
            Util.retrofit.create(OVSService::class.java).register(params[0]!!, params[1]!!, params[2]!!).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>?, response: Response<UserData>?) {
                    dialog.dismiss()
                    if (response!!.code() == 300) {
                        AlertDialog.Builder(this@RegisterActivity).setTitle("유저 존재").setMessage("이미 존재하는 아이디 입니다.").setPositiveButton("확인", { dialogInterface, i -> dialogInterface.dismiss() }).show()
                    } else if (response.code() == 200) {
                        mIntent!!.putExtra("token", response.body().token)
                        setResult(Activity.RESULT_OK, mIntent)
                        finish()
                    } else {
                        AlertDialog.Builder(this@RegisterActivity).setTitle("디비 에러").setMessage("디비 에러 입니다.").setPositiveButton("확인", { dialogInterface, i -> dialogInterface.dismiss() }).show()
                    }
                }

                override fun onFailure(call: Call<UserData>?, t: Throwable?) {
                    AlertDialog.Builder(this@RegisterActivity).setTitle("오류").setMessage("오류가 발생했습니다").setPositiveButton("확인", { dialogInterface, i -> dialogInterface.dismiss() }).show()
                }
            })
            return null
        }
    }
}
