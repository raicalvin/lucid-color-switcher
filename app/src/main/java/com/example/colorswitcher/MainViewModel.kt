package com.example.colorswitcher

import androidx.core.graphics.ColorUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    /* LiveData for Color Codes */

    private var _colorIntData: MutableLiveData<Int?> = MutableLiveData(null)

    val colorIntData: LiveData<Int?>
        get() = _colorIntData

    /* HSL Data values */

    private var _hue: Float = 0f // [0, 360)
    private var _saturation: Float = 0.5f // [0, 1]
    private var _brightness: Float = 0.5f // [0, 1]

    /* View Logic & Interaction */

    fun viewCreated() {
        generateRandomColor()
    }

    fun userSwiped(swipe: SwipeDirection) {
        when (swipe) {
            SwipeDirection.UP -> increaseBrightness()
            SwipeDirection.DOWN -> decreaseBrightness()
            SwipeDirection.LEFT -> decreaseHue()
            SwipeDirection.RIGHT -> increaseHue()
        }
        updateColor()
    }

    /* Color Generation */

    /**
     * When the View is created for the first time, assign a random color the begin.
     */
    private fun generateRandomColor() {
        _hue = Math.random().toFloat()
        updateColor()
    }

    /* Color Changes */

    private fun increaseHue() {
        _hue += 0.05f
        if (_hue > 1f) _hue = 1f
    }

    private fun decreaseHue() {
        _hue -= 0.05f
        if (_hue < 0f) _hue = 0f
    }

    private fun increaseBrightness() {
        _brightness += 0.05f
        if (_brightness > 1f) _brightness = 1f
    }

    private fun decreaseBrightness() {
        _brightness -= 0.05f
        if (_brightness < 0f) _brightness = 0f
    }

    /* Color Updates */

    /**
     * This uses the values of [_hue], [_saturation], and [_brightness] to generate a Float array
     * that can be used to generate a color int value specific to those values.
     *
     * NOTE: The [_hue] property ranges from [0, 360), which is why we are multiplying by 359.
     */
    private fun updateColor() {
        val h = _hue * 359
        val colorInt = ColorUtils.HSLToColor(floatArrayOf(h, _saturation, _brightness))
        _colorIntData.postValue(colorInt)
    }

}