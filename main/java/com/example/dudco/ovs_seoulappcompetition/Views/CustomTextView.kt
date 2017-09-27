package com.example.dudco.ovs_seoulappcompetition.Views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

import com.example.dudco.ovs_seoulappcompetition.R

/**
 * Created by dudco on 2016. 10. 19..
 */
class CustomTextView : TextView {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
//        applyTypeface(context, attrs)
        readAttributes(context, attrs)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        applyTypeface(context, attrs)
        readAttributes(context, attrs)
    }

    constructor(context: Context) : super(context) {
    }
    fun setFont(font: String){
        val fileName = "fonts/" + font
        this.typeface = Typeface.createFromAsset(context.assets, fileName)
    }
    private fun readAttributes(context: Context, attrs: AttributeSet){
        val arr = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
        val N = arr.getIndexCount()
        var fileName: String? = null
        for (i in 0..N - 1) {
            val attr = arr.getIndex(i)
            if (R.styleable.CustomTextView_font === attr) {
                fileName = "fonts/" + arr.getString(attr)
            }
        }

        arr.recycle()
        if (fileName != null) {
            this.typeface = Typeface.createFromAsset(context.assets, fileName)
        }
    }
}