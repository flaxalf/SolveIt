package it.sapienza.solveit.ui.customview

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.single.SingleLevelFourFragment
import java.lang.ClassCastException

class SingleLevelFourView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private lateinit var fragmentManager: FragmentManager
    private var parentFrag: SingleLevelFourFragment?

    private lateinit var image: Bitmap
    private lateinit var imageNeedle: Bitmap
    private var counter = 0

    private val oldImage: Bitmap
    private val oldImageNeedle: Bitmap

    init {
        isClickable = true

        // Retrieve parent fragment
        try {
            fragmentManager = (context as FragmentActivity).supportFragmentManager
        } catch (e: ClassCastException) {
            Log.e("Error fragment manager", "Can't get fragment manager")
        }
        parentFrag  = fragmentManager.findFragmentById(R.id.fragmentContainerView) as SingleLevelFourFragment?

        oldImage = BitmapFactory.decodeStream((context as? Activity)?.assets?.open("balloon_cut.png"))
        oldImageNeedle = BitmapFactory.decodeStream((context as? Activity)?.assets?.open("needle.png"))
    }

    override fun performClick(): Boolean {
        // Give default click listeners priority and perform accessibility/autofill events.
        if (super.performClick()) return true

        counter+=10
        // Redraw the view.
        invalidate()
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        image = Bitmap.createScaledBitmap(oldImage, counter + (0.2f*width).toInt(), counter + (0.2*height).toInt(),false)

        imageNeedle = Bitmap.createScaledBitmap(oldImageNeedle, (0.3f*width).toInt(), (0.2*height).toInt(),false)

        val needleX = width - width / 3f
        val needleY = (height - imageNeedle.height) / 2f

        if (((width - image.width) / 2f + image.width) >= needleX ) {
            // intersection of balloon and needle
            parentFrag!!.view?.let { parentFrag!!.activateButton(it) }
        } else {
            canvas.drawBitmap(image,(width - image.width) / 2f ,(height - image.height) / 2f,null)
        }

        canvas.drawBitmap(imageNeedle, needleX, needleY, null)
    }
}