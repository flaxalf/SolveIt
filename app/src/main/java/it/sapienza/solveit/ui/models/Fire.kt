package it.sapienza.solveit.ui.models

import android.graphics.Bitmap
import android.graphics.Rect

class Fire(private var image: Bitmap,
           private var initialX: Float, private var initialY: Float) {
    private var rect : Rect
    private var mCircleX : Float = initialX
    private var mCircleY : Float = initialY
    private var mCircleRadius : Float = 0f
    private var fireHeight = image.height
    private var fireWidth = image.width

    init {
        // Initialize rectangle useful for collision detection
        rect = Rect()
        rect.set((mCircleX -fireWidth/2f).toInt(),
            (mCircleY -fireHeight/2f).toInt(),
            (mCircleX -fireWidth/2f + fireWidth).toInt(),
            (mCircleY -fireHeight/2f + fireHeight).toInt()
        )

        mCircleRadius = if (fireHeight > fireWidth)
            fireHeight / 2f
        else
            fireWidth / 2f
    }

    fun intersect(f : Fire): Boolean {
        return Rect.intersects(rect, f.rect)
    }
}