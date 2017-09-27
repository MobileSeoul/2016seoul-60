package com.example.dudco.ovs_seoulappcompetition.Views

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dudco.ovs_seoulappcompetition.R
import com.example.dudco.ovs_seoulappcompetition.Util.Util

/**
 * Created by dudco on 2016. 10. 25..
 */
class CustomProfileCardView : CardView{
    var contain_layout : LinearLayout? = null
    var imageView : ImageView? = null
    var text_contain : LinearLayout? = null
    var text_title : CustomTextView? = null
    var text_subTitle : CustomTextView? = null
    var stars_contain : LinearLayout? = null

    constructor(context: Context?) : super(context) {
//        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
//        initView()
        readAttrs(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        initView()
        readAttrs(attrs,defStyleAttr)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
//        Util.Log(contain_layout!!.height.toString())
        val margin = Util.getPX(resources, 10.0f).toInt()
        val lp = LinearLayout.LayoutParams(contain_layout!!.height, contain_layout!!.height)
        lp.marginEnd = margin
//        lp.setMargins(margin,margin,margin,margin)
        imageView!!.layoutParams = lp
    }

    init{
        //Container
        contain_layout = LinearLayout(context).apply{
            val a = Util.getPX(resources, 10.0f).toInt()
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
            lp.setMargins(a,a,a,a)
            layoutParams = lp
            orientation = LinearLayout.HORIZONTAL
        }
        //Imageview
       imageView = ImageView(context).apply{
            val lp = LinearLayout.LayoutParams(Util.getPX(resources, 110.0f).toInt(), LinearLayout.LayoutParams.MATCH_PARENT)
            val image = BitmapFactory.decodeResource(resources, R.color.white)

            layoutParams = lp
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageBitmap(image)
       }

        //Text~
        text_contain = LinearLayout(context).apply{
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
            gravity = Gravity.CENTER_VERTICAL or Gravity.START
            layoutParams = lp
            orientation = LinearLayout.VERTICAL
        }
        //Title Text
        text_title = CustomTextView(context).apply{
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = Util.getPX(resources, 5.0f).toInt()
            setFont("nanumbarungothic_bold_0.ttf")
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f)
            text = "청계천"
            layoutParams = lp
        }

        //SubTitle Text
        text_subTitle = CustomTextView(context).apply {
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            setFont("nanumbarungothic_bold_0.ttf")
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f)
            text = "Cheong gyecheon"
            setTextColor(R.color.cardview_subTItle)
            layoutParams = lp
        }

        text_contain!!.addView(text_title)
        text_contain!!.addView(text_subTitle)

        //stars
        stars_contain = LinearLayout(context).apply{
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            lp.marginEnd = Util.getPX(resources, 5.0f).toInt()
            layoutParams = lp

            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL or Gravity.END
        }

        val starSize = Util.getPX(resources, 20.0f)
        val star1 : ImageView = ImageView(context).apply {
            val lp = LinearLayout.LayoutParams(starSize.toInt(), starSize.toInt())
            layoutParams = lp
            setImageResource(R.drawable.ic_star_nonfill)
        }
        val star2 : ImageView = ImageView(context).apply {
            val lp = LinearLayout.LayoutParams(starSize.toInt(), starSize.toInt())

            layoutParams = lp
            setImageResource(R.drawable.ic_star_nonfill)
        }
        val star3 : ImageView = ImageView(context).apply {
            val lp = LinearLayout.LayoutParams(starSize.toInt(), starSize.toInt())

            layoutParams = lp
            setImageResource(R.drawable.ic_star_nonfill)
        }
        val star4 : ImageView = ImageView(context).apply {
            val lp = LinearLayout.LayoutParams(starSize.toInt(), starSize.toInt())

            layoutParams = lp
            setImageResource(R.drawable.ic_star_nonfill)
        }
        val star5 : ImageView = ImageView(context).apply {
            val lp = LinearLayout.LayoutParams(starSize.toInt(), starSize.toInt())

            layoutParams = lp
            setImageResource(R.drawable.ic_star_nonfill)
        }

        stars_contain!!.addView(star1)
        stars_contain!!.addView(star2)
        stars_contain!!.addView(star3)
        stars_contain!!.addView(star4)
        stars_contain!!.addView(star5)


        contain_layout!!.addView(imageView)
        contain_layout!!.addView(text_contain)
        contain_layout!!.addView(stars_contain)


        this.addView(contain_layout)

    }

    private fun readAttrs(attrs: AttributeSet?){
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomProfileCardView)
        setTypedArray(typedArray)
    }
    private fun readAttrs(attrs: AttributeSet?, defStyleAttr: Int){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProfileCardView, defStyleAttr, 0)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray): Unit {
        val rank = typedArray.getInteger(R.styleable.CustomProfileCardView_rank, 0)

        var image = typedArray.getResourceId(R.styleable.CustomProfileCardView_procard_image, R.color.white)
        imageView!!.setImageBitmap(BitmapFactory.decodeResource(resources, image))

        val title = typedArray.getString(R.styleable.CustomProfileCardView_procard_title)
        text_title!!.text = title

        val sub_title = typedArray.getString(R.styleable.CustomProfileCardView_procard_subtitle)
        text_subTitle!!.text = sub_title

//        val bg_color = typedArray.getColor(R.styleable.CustomProfileCardView_procard_bgcolor, R.color.white)
//        this.setBackgroundColor(bg_color)
        when(rank){
            1 -> {
                (text_title as CustomTextView).setTextColor(resources.getColor(R.color.white,context.theme))
                (text_subTitle as CustomTextView).setTextColor(resources.getColor(R.color.gold_subTitle,context.theme))
                (this as CardView).setCardBackgroundColor(resources.getColor(R.color.gold, context.theme))
            }
            2->{
                (text_title as CustomTextView).setTextColor(resources.getColor(R.color.white,context.theme))
                (text_subTitle as CustomTextView).setTextColor(resources.getColor(R.color.silber_subTitle,context.theme))
                (this as CardView).setCardBackgroundColor(resources.getColor(R.color.silber, context.theme))

            }
            3->{
                (text_title as CustomTextView).setTextColor(resources.getColor(R.color.white,context.theme))
                (text_subTitle as CustomTextView).setTextColor(resources.getColor(R.color.bronze_subtitle,context.theme))
                (this as CardView).setCardBackgroundColor(resources.getColor(R.color.bronze, context.theme))

            }
        }
        val star_num = typedArray.getInteger(R.styleable.CustomProfileCardView_procard_starsnum, 0)

        stars_contain?.let {
            for (i in 0..star_num - 1)
                (it.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_fill)
        }
    }

    fun setImage(resid : Int){
        imageView!!.setImageBitmap(BitmapFactory.decodeResource(resources, resid))
    }
    fun setImage(bitmap: Bitmap){
        imageView!!.setImageBitmap(bitmap)
    }

    fun setTileText(string : String){
        text_title!!.text = string
    }
    fun setSubTitleText(string : String){
        text_subTitle!!.text = string
    }
    fun setStarNum(num : Int) {
        stars_contain?.let {
            for (i in 0..num - 1)
                (it.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_star_fill)
        }
    }
    fun setRank(rank : Int){
        when(rank){
            1 -> {
                (text_title as CustomTextView).setTextColor(resources.getColor(R.color.white,context.theme))
                (text_subTitle as CustomTextView).setTextColor(resources.getColor(R.color.gold_subTitle,context.theme))
                (this as CardView).setCardBackgroundColor(resources.getColor(R.color.gold, context.theme))
            }
            2->{
                (text_title as CustomTextView).setTextColor(resources.getColor(R.color.white,context.theme))
                (text_subTitle as CustomTextView).setTextColor(resources.getColor(R.color.silber_subTitle,context.theme))
                (this as CardView).setCardBackgroundColor(resources.getColor(R.color.silber, context.theme))

            }
            3->{
                (text_title as CustomTextView).setTextColor(resources.getColor(R.color.white,context.theme))
                (text_subTitle as CustomTextView).setTextColor(resources.getColor(R.color.bronze_subtitle,context.theme))
                (this as CardView).setCardBackgroundColor(resources.getColor(R.color.bronze, context.theme))

            }
        }
    }
}