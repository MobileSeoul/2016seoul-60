package com.example.dudco.ovs_seoulappcompetition.Util

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.example.dudco.ovs_seoulappcompetition.R

/**
 * Created by dudco on 2016. 10. 30..
 */
class RoundedAvatarDrawable(val bitmap: Bitmap, val resources : Resources) : Drawable() {

    private val mPaint: Paint
    private val mRectF: RectF
    private val mPath : Path
    private val mPPaint : Paint
    val intrinsicWidth: Int?
    val intrinsicHeight: Int?

    init {
        mRectF = RectF(0.0f, 0.0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        mPaint = Paint()
        mPath = Path()
        mPPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mPaint.shader = shader

        mPPaint.strokeWidth = Util.getPX(resources,5.0f)
        mPPaint.color = Color.WHITE
        mPPaint.style = Paint.Style.STROKE

        intrinsicWidth = this.bitmap.width
        intrinsicHeight = this.bitmap.height
//            Util.Log("${bitmap.width.toFloat()} + ${Util.getPX(resources, 120.0f)} + $intrinsicHeight")
    }
    fun setColor(color: Int){
        mPPaint.color = color
        invalidateSelf()
    }
    fun setWidth(width: Float){
        mPPaint.strokeWidth = width
        invalidateSelf()
    }
    override fun draw(canvas: Canvas) {
        canvas.drawCircle(intrinsicWidth!!/2.0f, intrinsicHeight!!/2.0f, intrinsicHeight /2.0f - 25, mPaint)
        mPath.addCircle(intrinsicWidth/2.0f, intrinsicHeight/2.0f, intrinsicHeight/2.0f - 25, Path.Direction.CW)
        canvas.drawPath(mPath, mPPaint)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mRectF.set(bounds)
    }

    override fun setAlpha(alpha: Int) {
        if (mPaint.alpha !== alpha) {
            mPaint.alpha = alpha
            invalidateSelf()
        }
    }

    fun setAntiAlias(aa: Boolean) {
        mPaint.isAntiAlias = aa
        invalidateSelf()
    }
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(cf: ColorFilter) {
        mPaint.colorFilter = cf
    }

    override fun setFilterBitmap(filter: Boolean) {
        mPaint.isFilterBitmap = filter
        invalidateSelf()
    }

    override fun setDither(dither: Boolean) {
        mPaint.isDither = dither
        invalidateSelf()
    }

    fun drawableToBitmap(image : ImageView): Bitmap {

        val bitmap = Bitmap.createBitmap(this.intrinsicWidth!!, this.intrinsicHeight!!, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)

//            Util.Log("${canvas.width} : ${canvas.height}")
        val asdf : Float = canvas.width / canvas.height.toFloat()
        Util.Log(asdf.toString())
        return Bitmap.createScaledBitmap(bitmap,(image.height * asdf).toInt(),image.height,true)
    }
}