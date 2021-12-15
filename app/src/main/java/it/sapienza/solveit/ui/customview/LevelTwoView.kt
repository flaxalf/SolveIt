package it.sapienza.solveit.ui.customview

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class LevelTwoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var image: Bitmap
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // Paint styles used for rendering are initialized here.
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }
    private var counter = 0

    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        // Give default click listeners priority and perform accessibility/autofill events.
        if (super.performClick()) return true

        counter++
        // Redraw the view.
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var oldImage = BitmapFactory.decodeStream((context as? Activity)?.assets?.open("sun_removebg.png"))
        image = Bitmap.createScaledBitmap(oldImage, (0.7f*width).toInt(),(0.5*height).toInt(),false)
        if (oldImage!= image){
            oldImage.recycle();
        }

        Log.d("canvas", "redrawing")
        //canvas.drawCircle(0f, 0f, 300f, paint)
        canvas.drawBitmap(image,counter*10+0f,0f,null)

    }
}