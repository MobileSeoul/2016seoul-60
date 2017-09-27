package com.example.dudco.ovs_seoulappcompetition.Views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.dudco.ovs_seoulappcompetition.Main_Home.HomeFragment
import com.example.dudco.ovs_seoulappcompetition.Profile.ProfileFragment
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.OVSData
import com.example.dudco.ovs_seoulappcompetition.Util.OVSService
import com.example.dudco.ovs_seoulappcompetition.Util.RealUserData
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.custom_cardview.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by dudco on 2016. 10. 22..
 */

class CustomCardView : LinearLayout{

    var isfill = false
    var card_id : String? = null
    var image : Bitmap? = null
    var image_title : ImageView? = null
    var adpater : HomeFragment.MyAdapter? = null

    constructor(context: Context?) : super(context) {
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initView()
        readAttrs(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView()
        readAttrs(attrs, defStyleAttr)
    }

    private fun initView(){
        val li : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.custom_cardview, this, false)
        image_title = view.findViewById(R.id.card_image_title) as ImageView
        addView(view)
    }

    private fun readAttrs(attrs: AttributeSet?){
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomCardView)
        setTypedArray(typedArray)
    }
    private fun readAttrs(attrs: AttributeSet?, defStyleAttr: Int){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCardView, defStyleAttr, 0)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray){
        val bt_option = BitmapFactory.Options()
        bt_option.inSampleSize = 2

        //set Title Image
        val bg_resID = typedArray.getResourceId(R.styleable.CustomCardView_mycard_image, R.drawable.progress_image)
        val image = BitmapFactory.decodeResource(resources, bg_resID, bt_option)
//        val cImage = Bitmap.createScaledBitmap(image,image.width, Util.getPX(resources, 110.0f).toInt(), true)
        card_image_title.setImageBitmap(image)

        //set Title Text
        val title = typedArray.getString(R.styleable.CustomCardView_mycard_title)
        card_title.text = title

        //set SubTitle Text
        val sub_title = typedArray.getString(R.styleable.CustomCardView_mycard_sub_title)
        card_sub_title.text = sub_title

        //set Stars, fill or nonfill
        val stars = typedArray.getInt(R.styleable.CustomCardView_mycard_stars,5)
        for (i in 0..stars - 1){
            (card_star_form.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_fill)
        }

        //set Forum Text
        val forumText = typedArray.getString(R.styleable.CustomCardView_mycard_forum_text)
        card_forum_text.text = forumText

        //set Faborite Text
        val faboriteText = typedArray.getString(R.styleable.CustomCardView_mycard_favorite_text)
        card_faborite_text.text = faboriteText

        if(isfill){
            card_faborite.setImageResource(R.drawable.ic_favorite_fill)
            card_faborite_text.setTextColor(resources.getColor(R.color.faborite, context.theme))
            isfill = true
        }

        card_faborite_btn.setOnClickListener {
            if(isfill) {
                card_faborite.setImageResource(R.drawable.ic_favorite_nonfill)
                card_faborite_text.setTextColor(resources.getColor(R.color.grey, context.theme))
                card_id?.let{
                    Util.Log("$card_id, ${Util.getUserInfo()}")
//                    DislikeAsyn().execute(card_id, Util.getUserInfo())
                    Util.retrofit.create(OVSService::class.java).dislike_tour(card_id!!, Util.getUserInfo()!!).enqueue(object : Callback<Void>{
                        override fun onFailure(call: Call<Void>?, t: Throwable?) {

                        }

                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                            if(response!!.code() == 200) {
                                Toast.makeText(context, card_id + "에 좋아요취소!", Toast.LENGTH_SHORT).show()
                                ProfileFragment().onAttach(context)
                            }
                        }
                    })
                }
                isfill = false
            } else {
                card_faborite.setImageResource(R.drawable.ic_favorite_fill)
                card_faborite_text.setTextColor(resources.getColor(R.color.faborite, context.theme))
                card_id?.let{
                    Util.Log("$card_id, ${Util.getUserInfo()}")
                    Util.retrofit.create(OVSService::class.java).like_tour(card_id!!, Util.getUserInfo()!!).enqueue(object : Callback<Void>{
                            override fun onFailure(call: Call<Void>?, t: Throwable?) {

                            }

                            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                                if(response!!.code() == 200) {
                                    Toast.makeText(context, card_id + "에 좋아요!", Toast.LENGTH_SHORT).show()
                                    ProfileFragment().onAttach(context)
                                }
                            }
                        })
                }
                Toast.makeText(context,"${card_title.text}", Toast.LENGTH_SHORT).show()
                isfill = true
            }
        }

        typedArray.recycle()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {


    }

    fun setTitleImage(bitmap: Bitmap){
//        val bt_option = BitmapFactory.Options()
//        bt_option.inSampleSize = 2
//        val image = BitmapFactory.decodeResource(resources, id, bt_option)
//        Util.Log("${card_image_title.width}, ${card_image_title.height}")
        card_image_title.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val _image = Bitmap.createScaledBitmap(bitmap, right-left, bottom-top, true)
            card_image_title.setImageBitmap(_image)
        }
    }
    fun setTitleImage(drawable: Drawable){
//        val bt_option = BitmapFactory.Options()
//        bt_option.inSampleSize = 2
//        val image = BitmapFactory.decodeResource(resources, id, bt_option)

        card_image_title.setImageDrawable(drawable)
    }
    fun setTitleText(text: String){
        card_title.text = text
    }
    fun setSubTitleText(text: String){
        card_sub_title.text = text
    }
    fun setStarsNum(num: Int){
        for (i in 0..4){
            (card_star_form.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_nonfill)
        }
        for (i in 0..num - 1){
            (card_star_form.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_fill)
        }
    }
    fun setForumText(text: String){
        card_forum_text.text = text
    }
    fun setFaboriteText(text : String){
        card_faborite_text.text = text
    }

    fun setIsClick(clicked: Boolean){
        if(clicked){
            card_faborite.setImageResource(R.drawable.ic_favorite_fill)
            card_faborite_text.setTextColor(resources.getColor(R.color.faborite, context.theme))
            isfill = true
        }else{
            card_faborite.setImageResource(R.drawable.ic_favorite_nonfill)
            card_faborite_text.setTextColor(resources.getColor(R.color.grey, context.theme))
            isfill = false
        }
    }
    fun setId(id :String){
        card_id = id
    }

    fun setfill(token : String){
        Util.retrofit.create(OVSService::class.java).user(token).enqueue(object : Callback<RealUserData>{
            override fun onFailure(call: Call<RealUserData>?, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<RealUserData>?, response: Response<RealUserData>?) {

                val gson : Gson = Gson()
                val collectionType = object : TypeToken<Collection<OVSData>>() {}.type
                if(response!!.code() == 200){
                    val enums : Collection<OVSData> = gson.fromJson(response!!.body().favorit.toList().toString(), collectionType)
                    val data = enums.toTypedArray()
                    for(item in data) {
                        Util.Log("respon - ${item.id} , $card_id, ${item.id.equals(card_id)} $")
                        if (item.id.equals(card_id)){
                            setIsClick(true)
                            break
                        }
                    }
                }
            }
        })
    }
}