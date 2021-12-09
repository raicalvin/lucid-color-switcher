package com.example.colorswitcher

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * This View serves as the main layout file for [MainActivity].
 */
class MainLayout: View {

    // Mandatory Constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        initSelf()
    }

    private fun initSelf() {
        this.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    /**
     * This function handles changing the background color of this View from the original color to
     * the new color.
     *
     * In the case that the View is being initialized for the first time, we simply set the color
     * to [toColorInt] without running the fade animation.
     */
    fun updateBackgroundColorTo(toColorInt: Int) {
        val fromColorInt = (this.background as? ColorDrawable)?.color
        if (fromColorInt == null) { // color is being assigned for the first time
            this.setBackgroundColor(toColorInt)
            return
        }
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), fromColorInt, toColorInt)
        colorAnimation.duration = 250
        colorAnimation.addUpdateListener { animation -> this.setBackgroundColor(animation?.animatedValue as Int) }
        colorAnimation.start()
    }

    fun getBackgroundColorAsHexCode(): String {
        val colorInt = (this.background as ColorDrawable).color
        return java.lang.String.format("#%06X", 0xFFFFFF and colorInt)
    }

}