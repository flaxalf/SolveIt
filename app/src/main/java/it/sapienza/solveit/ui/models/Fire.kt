package it.sapienza.solveit.ui.models

import android.graphics.Bitmap
import android.graphics.Rect

class Fire(private var image: Bitmap,
           private var initialX: Float, private var initialY: Float) {

    var active = true
    var isTouched = false
    var level : Int = Constants.FIRE_SMALL

    var fireImage = image
    private var fireHeight = fireImage.height
    private var fireWidth = fireImage.width

    var rect : Rect = Rect()
    var mCircleX : Float = initialX
    var mCircleY : Float = initialY
    var mCircleRadius : Float = 0f


    init {
        refreshParams()
    }

    fun refreshParams(){
        //refresh image dimension, useful when image changes
        fireHeight = fireImage.height
        fireWidth = fireImage.width

        //refresh rectangle, useful for collision detection
        rect.set((mCircleX -fireWidth/2f).toInt(),
            (mCircleY -fireHeight/2f).toInt(),
            (mCircleX -fireWidth/2f + fireWidth).toInt(),
            (mCircleY -fireHeight/2f + fireHeight).toInt()
        )

        //refresh radius, useful for collision detection
        mCircleRadius = if (fireHeight > fireWidth)
            fireHeight / 2f
        else
            fireWidth / 2f
    }

    fun intersect(f : Fire): Boolean {
        if(!active || !f.active){
            return false
        }
        return Rect.intersects(rect, f.rect)
    }
}