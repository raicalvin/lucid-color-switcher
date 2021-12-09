package com.example.colorswitcher

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: MainLayout

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayout = MainLayout(this)
        setContentView(mainLayout.rootView)
        viewModel.viewCreated()
        observeData()
    }

    private fun observeData() {
        viewModel.colorIntData.observe(this) { colorInt ->
            if (colorInt != null) {
                mainLayout.updateBackgroundColorTo(colorInt)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        handleEvent(event)
        return false
    }

    /**
     * These coordinates are used to calculating the starting and ending positions of the user's
     * screen tap.
     */
    private var x1: Double = 0.0
    private var x2: Double = 0.0
    private var y1: Double = 0.0
    private var y2: Double = 0.0

    /**
     * This stores the minimum length that the vector from the above coordinates need to equal in
     * order to register the appropriate [SwipeDirection] event.
     */
    private val minDistanceThreshold = 150.0

    /**
     * This variable is used to determine if the user clicked on the view or swiped. If the
     * [MotionEvent.ACTION_MOVE] is never triggered, then the user only tapped their finger and
     * removed it. Else, we should set this to true to indicate that the intended gesture is a swipe.
     */
    private var actionWasMoved = false

    private fun handleEvent(event: MotionEvent?) {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                actionWasMoved = false
                x1 = event.x.toDouble()
                y1 = event.y.toDouble()
            }
            MotionEvent.ACTION_MOVE -> {
                actionWasMoved = true
                x2 = event.x.toDouble()
                y2 = event.y.toDouble()
            }
            MotionEvent.ACTION_UP -> {
                handleDirectionCoordinates()
            }
        }
    }

    private fun handleDirectionCoordinates() {
        if (!actionWasMoved) { // then this was an intended click
            handleClick()
            return
        }
        val absDeltaX = abs(x2 - x1)
        val absDeltaY = abs(y2 - y1)
        if (absDeltaX > absDeltaY) { // LEFT OR RIGHT
            if (absDeltaX < minDistanceThreshold) return
            if (x2 > x1) { // moved right
                viewModel.userSwiped(SwipeDirection.RIGHT)
            } else { // moved left
                viewModel.userSwiped(SwipeDirection.LEFT)
            }
        } else { // UP OR DOWN
            if (absDeltaY < 300) return
            if (y2 > y1) { // moved down
                viewModel.userSwiped(SwipeDirection.DOWN)
            } else { // moved up
                viewModel.userSwiped(SwipeDirection.UP)
            }
        }
    }

    /**
     * This function will extract the color code in a hex format and paste it into the devices
     * clipboard as a string value.
     */
    private fun handleClick() {
        val colorString = mainLayout.getBackgroundColorAsHexCode()
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", colorString)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "The color $colorString was copied to the clipboard!", Toast.LENGTH_SHORT).show()
    }

}