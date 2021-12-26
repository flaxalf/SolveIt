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
import it.sapienza.solveit.ui.levels.LevelFourFragment
import java.lang.ClassCastException

class LevelFourView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private lateinit var fragmentManager: FragmentManager
    private var parentFrag: LevelFourFragment?

    private lateinit var image: Bitmap
    private var counter = 0

    init {
        isClickable = true

        // Retrieve parent fragment
        try {
            fragmentManager = (context as FragmentActivity).supportFragmentManager
        } catch (e: ClassCastException) {
            Log.e("Error fragment manager", "Can't get fragment manager")
        }
        parentFrag  = fragmentManager.findFragmentById(R.id.fragmentContainerView) as LevelFourFragment?

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

        val oldImage = BitmapFactory.decodeStream((context as? Activity)?.assets?.open("balloon.png"))
        image = Bitmap.createScaledBitmap(oldImage, counter + (0.3f*width).toInt(), counter + (0.2*height).toInt(),false)
        if (oldImage!= image){
            oldImage.recycle()
        }

        // At least 10 clicks, true when counter <- 100
        if ( counter / 10 >= 10) {
            // Activating the button on the fragment for the win dialog
            parentFrag!!.view?.let { parentFrag!!.activateButton(it) }
        }

        canvas.drawBitmap(image,(width - image.width) / 2f ,(height - image.height) / 2f,null)
    }
}