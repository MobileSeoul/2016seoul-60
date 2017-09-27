package com.example.dudco.ovs_seoulappcompetition.Views

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.Util

/**
 * Created by dudco on 2016. 10. 23..
 */

class CategoryView : RelativeLayout{
    constructor(context: Context?) : super(context){
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initView()
        readAttrs(attrs)

    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView()
        readAttrs(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        initView()
        readAttrs(attrs)
    }
    var title: CustomTextView? = null
    var visitNum: CustomTextView? = null
    var des_form: LinearLayout? = null
    var favorite_text: CustomTextView? = null

    fun initView(){
        val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)

        val faborite = LinearLayout(context)
        faborite.layoutParams = lp
        faborite.gravity = Gravity.CENTER
        faborite.orientation = LinearLayout.HORIZONTAL

        val lp_faborite_image = LinearLayout.LayoutParams(Util.getPX(resources, 20.0f).toInt(), Util.getPX(resources, 20.0f).toInt())
        lp_faborite_image.setMargins(Util.getPX(resources, 5.0f).toInt(), Util.getPX(resources, 5.0f).toInt(), Util.getPX(resources, 5.0f).toInt(), Util.getPX(resources, 5.0f).toInt())
        val faborite_image = ImageView(context)
        faborite_image.layoutParams = lp_faborite_image
        faborite_image.setImageResource(R.drawable.ic_favorite_white)
        faborite.addView(faborite_image)

        val lp_faborite_text = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        favorite_text = CustomTextView(context).apply {
            layoutParams = lp_faborite_text
            text = "5.3k"
        }
        favorite_text!!.run {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0f)
            setTextColor(resources.getColor(R.color.white, context.theme))
        }
        faborite.addView(favorite_text)

        val lp_desform = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp_desform.addRule(RelativeLayout.CENTER_IN_PARENT)
        des_form = LinearLayout(context).apply {
            layoutParams = lp_desform
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }

        val lp_title = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp_title.bottomMargin = Util.getPX(resources, 8.0f).toInt()
        title = CustomTextView(context).apply {
            layoutParams = lp_title
            text = "랜드마크"
        }
        title!!.run {
            setFont("nanumbarungothic_bold_0.ttf")
            setTextColor(resources.getColor(R.color.white, context.theme))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 23.0f)
        }

        val lp_visitNum = LinearLayout.LayoutParams(Util.getPX(resources, 100.0f).toInt(), Util.getPX(resources, 20.0f).toInt())
        visitNum = CustomTextView(context).apply {
            layoutParams = lp_visitNum
            gravity = Gravity.CENTER
            text = "3,571VISITOR"
        }
        visitNum!!.run {
            setFont("nanumbarungothic_0.ttf")
            setTextColor(resources.getColor(R.color.white, context.theme))
            setBackgroundResource(R.drawable.visitnum_shape)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0f)
        }

        des_form!!.addView(title)
        des_form!!.addView(visitNum)

        this.addView(faborite)
        this.addView(des_form)
    }

    private fun readAttrs(attrs: AttributeSet?){
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.CategoryView)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray){
        var options = BitmapFactory.Options()
        options.inSampleSize = 4

        val bg = typedArray.getResourceId(R.styleable.CategoryView_category_bg,R.drawable.category_image1)
        this.background = BitmapDrawable(resources, BitmapFactory.decodeResource(resources, bg, options))

        val title_text = typedArray.getString(R.styleable.CategoryView_category_title)
        title!!.text = title_text
        visitNum!!.text = typedArray.getString(R.styleable.CategoryView_category_visitor_num) + " VISITOR"
        favorite_text!!.text = typedArray.getString(R.styleable.CategoryView_category_faborite_num)
    }

    fun setTitleText(text: String){
        title!!.text = text
    }
    fun setVisitNum(num: String){
        visitNum!!.text = num + " VISITOR"
    }
    fun setFaboriteNum(num: String){
        favorite_text!!.text = num
    }
    fun setBackGroundBG(res: Int){
        var options = BitmapFactory.Options()
        options.inSampleSize = 4
        this.background = BitmapDrawable(resources, BitmapFactory.decodeResource(resources, res, options))
    }
}