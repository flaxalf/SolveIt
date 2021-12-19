package it.sapienza.solveit.ui.customview

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.levels.LevelOneFragment
import it.sapienza.solveit.ui.levels.LevelTwoFragment
import java.lang.ClassCastException

class LevelTwoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), SensorEventListener {
    private var isMorning: Boolean = true
    private var sensorManager: SensorManager
    private var mLight: Sensor? = null

    private lateinit var fragmentManager: FragmentManager
    private var parentFrag: LevelTwoFragment?


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
        isClickable = false

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.setText("It's time to sleep")
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.setText("Level 2")

        // Retrieve parent fragment
        try {
            fragmentManager = (context as FragmentActivity).supportFragmentManager
        } catch (e: ClassCastException) {
            Log.e("Error fragment manager", "Can't get fragment manager")
        }
        parentFrag  = fragmentManager.findFragmentById(R.id.fragmentContainerView) as LevelTwoFragment?

        // SENSOR
        sensorManager =
            (context as? Activity)?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        mLight.also { grav ->
            sensorManager.registerListener(this, grav, SensorManager.SENSOR_DELAY_UI)}
    }

    /*
    override fun performClick(): Boolean {
        // Give default click listeners priority and perform accessibility/autofill events.
        if (super.performClick()) return true

        counter++
        // Redraw the view.
        invalidate()
        return true
    }
    */

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isMorning) {
            var oldImage = BitmapFactory.decodeStream((context as? Activity)?.assets?.open("sun_removebg.png"))
            image = Bitmap.createScaledBitmap(oldImage, (0.7f*width).toInt(),(0.5*height).toInt(),false)
            if (oldImage!= image){
                oldImage.recycle();
            }
        } else {
            var oldImage = BitmapFactory.decodeStream((context as? Activity)?.assets?.open("moon_removebg.png"))
            image = Bitmap.createScaledBitmap(oldImage, (0.7f*width).toInt(),(0.5*height).toInt(),false)
            if (oldImage!= image){
                oldImage.recycle();
            }
            // Activating the button on the fragment for the win dialog
            parentFrag!!.view?.let { parentFrag!!.activateButton(it) }
        }

        Log.d("canvas", "redrawing")
        canvas.drawBitmap(image,counter*10+0f,0f,null)

    }

    override fun onSensorChanged(event: SensorEvent) {
        // Just look when light is on or off
        if(event.sensor.type == Sensor.TYPE_LIGHT)
            if (event.values[0] <= 3.0f) {
                // Light is off, sun sleeps
                isMorning = false
                invalidate()
            }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // nothing
    }
}