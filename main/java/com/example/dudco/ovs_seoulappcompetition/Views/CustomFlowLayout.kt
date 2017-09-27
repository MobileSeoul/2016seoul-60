package com.example.dudco.ovs_seoulappcompetition.Views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * Created by dudco on 2016. 10. 30..
 */
class CustomFlowLayout : ViewGroup{
    private val DEFAULT_HORIZONTAL_SPACING = 5
    private val DEFAULT_VERTICAL_SPACING = 5

    private var mVerticalSpacing: Int = 0
    private var mHorizontalSpacing: Int = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun setHorizontalSpacing(pixelSize: Int) {
        mHorizontalSpacing = pixelSize
    }

    fun setVerticalSpacing(pixelSize: Int) {
        mVerticalSpacing = pixelSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val myWidth = resolveSize(0, widthMeasureSpec)

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        var childLeft = paddingLeft
        var childTop = paddingTop

        var lineHeight = 0

        // Measure each child and put the child to the right of previous child
        // if there's enough room for it, otherwise, wrap the line and put the child to next line.
        var i = 0
        val childCount = childCount
        while (i < childCount) {
            val child = getChildAt(i)
            if (child.visibility !== View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
            } else {
                ++i
                continue
            }

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            lineHeight = Math.max(childHeight, lineHeight)

            if (childLeft + childWidth + paddingRight > myWidth) {
                childLeft = paddingLeft
                childTop += mVerticalSpacing + lineHeight
                lineHeight = childHeight
            } else {
                childLeft += childWidth + mHorizontalSpacing
            }
            ++i
        }

        val wantedHeight = childTop + lineHeight + paddingBottom

        setMeasuredDimension(myWidth, resolveSize(wantedHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val myWidth = r - l

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight

        var childLeft = paddingLeft
        var childTop = paddingTop

        var lineHeight = 0

        var i = 0
        val childCount = childCount
        while (i < childCount) {
            val childView = getChildAt(i)

            if (childView.visibility === View.GONE) {
                ++i
                continue
            }

            val childWidth = childView.measuredWidth
            val childHeight = childView.measuredHeight

            lineHeight = Math.max(childHeight, lineHeight)

            if (childLeft + childWidth + paddingRight > myWidth) {
                childLeft = paddingLeft
                childTop += mVerticalSpacing + lineHeight
                lineHeight = childHeight
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
            childLeft += childWidth + mHorizontalSpacing
            ++i
        }
    }
}