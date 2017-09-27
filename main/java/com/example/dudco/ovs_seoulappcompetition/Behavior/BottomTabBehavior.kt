package com.example.dudco.ovs_seoulappcompetition.Behavior

import android.animation.Animator
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View

/**
 * Created by dudco on 2016. 10. 25..
 */
class BottomTabBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs){
    private val INTERPOLATOR = FastOutSlowInInterpolator()

    private var mDySinceDirectionChange: Int = 0
    private var mIsShowing: Boolean = false
    private var mIsHiding: Boolean = false

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout?, child: View?, directTargetChild: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout?, child: View?, target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        if(dy > 0 && mDySinceDirectionChange < 0 || dy < 0 &&  mDySinceDirectionChange > 0){
            if(child!!.animation != null)
                child.animation!!.cancel()
            mDySinceDirectionChange = 0
        }
        mDySinceDirectionChange += dy
        if (mDySinceDirectionChange > child!!.height && child.visibility == View.VISIBLE && !mIsHiding) {
            hideView(child)
        } else if (mDySinceDirectionChange < 0 && child.visibility == View.GONE && !mIsShowing) {
            showView(child)
        }
    }

    fun hideView(view: View){
        mIsHiding = true
        val animator = view.animate().translationY(view.height.toFloat()).setInterpolator(INTERPOLATOR).setDuration(200)
        animator.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mIsHiding = false
                view.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                mIsHiding = false
                if (!mIsShowing) {
                    showView(view)
                }
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        animator.start()

    }
    fun showView(view: View){
        mIsShowing = true
        val animator = view.animate().translationY(0.0f).setInterpolator(INTERPOLATOR).setDuration(200)
        animator.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mIsShowing = false
            }

            override fun onAnimationCancel(animation: Animator?) {
                mIsShowing = false
                if (!mIsHiding) {
                    hideView(view)
                }
            }

            override fun onAnimationStart(animation: Animator?) {
                view.visibility = View.VISIBLE
            }
        })
        animator.start()
    }
}