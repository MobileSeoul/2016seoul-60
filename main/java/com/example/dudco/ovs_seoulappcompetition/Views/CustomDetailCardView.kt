package com.example.dudco.ovs_seoulappcompetition.Views

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.Util
import kotlinx.android.synthetic.main.custom_cardview.view.*
import kotlinx.android.synthetic.main.custom_cardview_detail.view.*

/**
 * Created by dudco on 2016. 10. 22..
 */

class CustomDetailCardView : LinearLayout{

    var isfill = false
    var imageView : ImageView? = null
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
        val view = li.inflate(R.layout.custom_cardview_detail, this, false)
        imageView = view.findViewById(R.id.detail_card_image_title) as ImageView
        addView(view)
    }

    private fun readAttrs(attrs: AttributeSet?){
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomDetailCardView)
        setTypedArray(typedArray)
    }
    private fun readAttrs(attrs: AttributeSet?, defStyleAttr: Int){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomDetailCardView, defStyleAttr, 0)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray){
        val bt_option = BitmapFactory.Options()
        bt_option.inSampleSize = 2

        //set Title Image
        val bg_resID = typedArray.getResourceId(R.styleable.CustomDetailCardView_category_d_bg, R.drawable.progress_image)
        val image = BitmapFactory.decodeResource(resources, bg_resID, bt_option)
//        val cImage = Bitmap.createScaledBitmap(image,image.width, Util.getPX(resources, 110.0f).toInt(), true)
        detail_card_image_title.setImageBitmap(image)

        //set Title Text
        val title = typedArray.getString(R.styleable.CustomDetailCardView_category_d_title)
        detail_card_title.text = title

        //set SubTitle Text
        val sub_title = typedArray.getString(R.styleable.CustomDetailCardView_category_d_subtitle)
        detail_card_sub_title.text = sub_title

        //set Stars, fill or nonfill
        val stars = typedArray.getInt(R.styleable.CustomDetailCardView_category_d_stars,5)
        for (i in 0..stars - 1){
            (detail_card_star_form.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_fill)
        }

        if(isfill){
            card_faborite.setImageResource(R.drawable.ic_favorite_fill)
            card_faborite_text.setTextColor(resources.getColor(R.color.faborite, context.theme))
            isfill = true
        }

        detail_card_faborite_btn.setOnClickListener {

        }

        typedArray.recycle()
    }

    fun setTitleImage(bitmap : Bitmap){
//        val bt_option = BitmapFactory.Options()
//        bt_option.inSampleSize = 2
//        val bitmap = Bitmap(bitmap, bt_option)
//        val image = BitmapFactory.decodeResource(resources, id, bt_option)

        detail_card_image_title.setImageBitmap(bitmap)
    }

    fun setTitleText(text: String){
        detail_card_title.text = text
    }
    fun setSubTitleText(text: String){
        detail_card_sub_title.text = text
    }
    fun setStarsNum(num: Int){
        for (i in 0..4){
            (detail_card_star_form.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_nonfill)
        }
        for (i in 0..num - 1){
            (detail_card_star_form.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_fill)
        }
    }
}