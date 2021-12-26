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
import it.sapienza.solveit.ui.models.Constants
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
        fire3 = Fire(imageSmall, width / 4f, 3 * height / 4f)

        isFirst = false

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isFirst)
            firstDraw()

        if(fire1.active) {
            canvas.drawBitmap(
                fire1.fireImage, fire1.rect.left.toFloat(),
                fire1.rect.top.toFloat(), null
            )
        }

        if(fire2.active) {
            canvas.drawBitmap(
                fire2.fireImage, fire2.rect.left.toFloat(),
                fire2.rect.top.toFloat(), null
            )

        }

        if(fire3.active) {
            canvas.drawBitmap(
                fire3.fireImage, fire3.rect.left.toFloat(),
                fire3.rect.top.toFloat(), null
            )
        }


        // Activating the button on the fragment for the win dialog
        if(fire1.level == Constants.FIRE_LARGE || fire2.level == Constants.FIRE_LARGE ||
            fire3.level == Constants.FIRE_LARGE) {
            parentFrag!!.view?.let { parentFrag!!.activateButton(it) }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var value = super.onTouchEvent(event)

        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {

                    when {
                        checkFireTouched(event, fire1) -> {
                            // Touched fire1
                            fire1.mCircleX = event.x
                            fire1.mCircleY = event.y
                            fire1.refreshParams()

                            value = true
                        }
                        checkFireTouched(event, fire2) -> {
                            // Touched fire2
                            fire2.mCircleX = event.x
                            fire2.mCircleY = event.y
                            fire2.refreshParams()

                            value = true
                        }
                        checkFireTouched(event, fire3) -> {
                            // Touched fire3
                            fire3.mCircleX = event.x
                            fire3.mCircleY = event.y
                            fire3.refreshParams()

                            value = true
                        }
                    }

                    if(value) {
                        when {
                            fire1.intersect(fire2) -> {

                                mergeFires(fire1, fire2)

                            }
                            fire1.intersect(fire3) -> {

                                mergeFires(fire1, fire3)

                            }
                            fire2.intersect(fire3) -> {

                                mergeFires(fire1, fire2)

                            }
                        }

                        postInvalidate()
                    }

                }

                MotionEvent.ACTION_DOWN -> {

                    value = true
                }
            }
        }
        return value
    }

    private fun checkFireTouched(event: MotionEvent, fire: Fire) : Boolean{
        val x = event.x
        val y = event.y

        val dx = (x - fire.mCircleX).toDouble().pow(2.0)
        val dy = (y - fire.mCircleY).toDouble().pow(2.0)

        if (dx + dy < fire.mCircleRadius.toDouble().pow(2.0)) {
            return true
        }
        return false
    }

    private fun mergeFires(firstFire : Fire, secondFire: Fire){
        if(firstFire.level >= secondFire.level) {
            mergeFiresOrdered(firstFire, secondFire)
        } else{
            mergeFiresOrdered(secondFire, firstFire)
        }
    }

    private fun mergeFiresOrdered(biggerFire : Fire, smallerFire: Fire){
        smallerFire.active = false

        when (biggerFire.level) {
            Constants.FIRE_SMALL -> {
                biggerFire.level = Constants.FIRE_MEDIUM
                biggerFire.fireImage = imageMedium

            }
            Constants.FIRE_MEDIUM -> {
                biggerFire.level = Constants.FIRE_LARGE
                biggerFire.fireImage = imageLarge

            }

        }
        biggerFire.refreshParams()
    }



}