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
import android.graphics.Bitmap
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.single.LevelOneFragment
import java.lang.ClassCastException
import kotlin.math.abs


class LevelOneView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), SensorEventListener {


    private var sensorManager: SensorManager
    private var mRotation: Sensor? = null
    private var image: Bitmap
    private var counter = 0
    private var completed = false
    private lateinit var fragmentManager: FragmentManager
    private var parentFrag: LevelOneFragment?

    init {
        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Round rocks? Maybe one can rotate..."
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 1"

        // Retrieve parent fragment
        try {
            fragmentManager = (context as FragmentActivity).supportFragmentManager
        } catch (e: ClassCastException) {
            Log.e("Error fragment manager", "Can't get fragment manager")
        }
        parentFrag  = fragmentManager.findFragmentById(R.id.fragmentContainerView) as LevelOneFragment?

        // Define the initial moving bitmap
        image = BitmapFactory.decodeStream((context as? Activity)?.assets?.open("rock_1920.png"))

        // SENSOR
        sensorManager =
            (context as? Activity)?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mRotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        // Need to register the listener when the fragment is on foreground, so init of the view
        mRotation.also { grav ->
            sensorManager.registerListener(this, grav, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawScene(canvas)
    }

    private fun drawScene(canvas: Canvas) {
        if(counter == 0) {
            image = Bitmap.createScaledBitmap(
                image,
                (0.5 * width).toInt(),
                (0.2 * height).toInt(),
                false
            )
        }
        var rotatingImage : Bitmap = image

        // create a matrix for the manipulation
        val matrix = Matrix()
        matrix.setRotate(-counter.toFloat())
        rotatingImage = Bitmap.createBitmap(rotatingImage, 0, 0, rotatingImage.width, rotatingImage.height, matrix, true)

        canvas.drawBitmap(rotatingImage,-counter*5 +(width-rotatingImage.width)/2f,(height-rotatingImage.height)/2f,null)

        // Creating other rocks in different positions
        canvas.drawBitmap(image,(width-image.width*2f), (height-image.height*2f),null)
        canvas.drawBitmap(image,(width-image.width)/0.9f,(height-image.height*3).toFloat(),null)
        canvas.drawBitmap(image,(width-image.width)/3f,(height-image.height)/4f,null)

    }

    // Overriding sensors function, extracting info from them
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //Log.d("Sens accuracy", "Accuracy"+p1.toString())
    }


    override fun onSensorChanged(event: SensorEvent) {
        /*  values[0]: x*sin(θ/2)   values[1]: y*sin(θ/2)
            values[2]: z*sin(θ/2)   values[3]: cos(θ/2)
            values[4]: estimated heading Accuracy (in radians) (-1 if unavailable)
         */

        val rotationMatrix  = FloatArray(16)
        val orientationValues = FloatArray(3)

        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationValues)

            val azimuth = Math.toDegrees(orientationValues[0].toDouble())
            val pitch = Math.toDegrees(orientationValues[1].toDouble())
            val roll = Math.toDegrees(orientationValues[2].toDouble())
            val zValue = event.values[2]

            //Log.d("angles", "roll: $roll , pitch: $pitch , zValue: $zValue")

            if (pitch > -50 && pitch < -3 && abs(zValue) > 0.01  && roll < 0 && !completed) {
                counter += 1
                if(counter >= 40){
                    // Activating the button on the fragment for the win dialog
                    parentFrag!!.view?.let { parentFrag!!.activateButton(it) }
                }
                if (counter >= 80) {
                    completed = true
                    // Unregister the listener when the animation is executed
                    sensorManager.unregisterListener(this)
                }


                // Redraw scene
                invalidate()
            }
        }



        /*val zValue = event?.values?.get(2)
        if (zValue != null) {
            if (Math.abs(zValue) < 0.2 && !rotating) {
                if (counter >= 80) {
                    rotating = true
                    // Unregister the listener when the animation is executed
                    sensorManager.unregisterListener(this)
                }
                counter += 1

                // Activating the button on the fragment for the win dialog
                parentFrag!!.view?.let { parentFrag!!.activateButton(it) }

                // Redraw scene
                invalidate()
            }
        }

         */
    }
}