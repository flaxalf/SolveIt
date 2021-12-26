package it.sapienza.solveit.ui.customview

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.LevelFiveFragment
import it.sapienza.solveit.ui.models.Fire
import java.lang.ClassCastException
import kotlin.math.pow

class LevelFiveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private lateinit var fragmentManager: FragmentManager
    private var parentFrag: LevelFiveFragment?

    private lateinit var imageSmall: Bitmap
    private lateinit var imageMedium: Bitmap
    private lateinit var imageLarge: Bitmap

    private var mCircleX = 500f
    private var mCircleY = 500f
    private var mCircleRadius = 250f
    private var mPaint: Paint

    private lateinit var fire1: Fire
    private lateinit var fire2: Fire
    private lateinit var fire3: Fire

    private var isFirst = true

    init {
        isClickable = true

        // Retrieve parent fragment
        try {
            fragmentManager = (context as FragmentActivity).supportFragmentManager
        } catch (e: ClassCastException) {
            Log.e("Error fragment manager", "Can't get fragment manager")
        }
        parentFrag  = fragmentManager.findFragmentById(R.id.fragmentContainerView) as LevelFiveFragment?

        // Initialize a PAINT for the circle
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = Color.parseColor("#00ccff")
    }

    private fun firstDraw() {
        // Create a bitmap of a fire, handle the touch event approximating the image as a circle
        val oldImage = BitmapFactory.decodeStream((context as? Activity)?.assets?.open("fire_removebg.png"))
        imageSmall = Bitmap.createScaledBitmap(oldImage, (0.3f*width).toInt(), (0.2*height).toInt(),false)
        imageMedium = Bitmap.createScaledBitmap(oldImage, (0.4f*width).toInt(), (0.3*height).toInt(),false)
        imageLarge = Bitmap.createScaledBitmap(oldImage, (0.5f*width).toInt(), (0.4*height).toInt(),false)

        if (oldImage!= imageSmall){
            oldImage.recycle()
        }

        // Three fires
        fire1 = Fire(imageSmall, width / 2f, height / 2f)
        fire2 = Fire(imageSmall, 3 * width / 4f, height / 4f)
        fire1 = Fire(imageSmall, width / 4f, 3 * height / 4f)

        isFirst = false

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isFirst)
            firstDraw()

        canvas.drawBitmap(imageSmall, mCircleX -imageSmall.width/2f, mCircleY -imageSmall.height/2f,null)


        // Activating the button on the fragment for the win dialog
        //    parentFrag!!.view?.let { parentFrag!!.activateButton(it) }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value = super.onTouchEvent(event)

        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val x = event.x
                    val y = event.y

                    val dx = (x - mCircleX).toDouble().pow(2.0)
                    val dy = (y - mCircleY).toDouble().pow(2.0)

                    if (dx + dy < mCircleRadius.toDouble().pow(2.0)) {
                        // Touched
                        mCircleX = x
                        mCircleY = y

                        postInvalidate()
                        return true
                    }
                }

                MotionEvent.ACTION_DOWN -> {

                    return true
                }
            }
        }
        return value
    }

}