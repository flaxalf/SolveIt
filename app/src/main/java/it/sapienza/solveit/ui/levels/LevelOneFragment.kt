package it.sapienza.solveit.ui.levels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import it.sapienza.solveit.R
import java.lang.Math.abs

class LevelOneFragment : Fragment(), SensorEventListener {
    lateinit var paint: Paint
    lateinit var rockIV: ImageView
    lateinit var buttonIV: ImageView
    lateinit var canvas : Canvas
    val w = 3000
    val h = 4000
    var rotating: Boolean = false

    lateinit var sensorManager: SensorManager
    var mRotationVect: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_level_one, container, false)

        // DRAW ON CANVAS
        drawScene(view)

        // SENSOR
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mRotationVect = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        return view
    }

    fun drawScene(view: View) {
        rockIV = view.findViewById<View>(R.id.rockIV) as ImageView
        buttonIV = view.findViewById(R.id.buttonIV) as ImageView

        /* Creation of a 2d bitmap rock */
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        rockIV.setImageBitmap(bitmap)
        canvas = Canvas(bitmap)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = resources.getColor(R.color.black)
        paint.textSize = 100F

        // Colour the canvas
        canvas.drawRGB(255,255,255)

        var image = BitmapFactory.decodeStream(activity?.assets?.open("rock_1920.png"))

        // creating a scaled rock, the rotating one is at the center of the screen
        image=Bitmap.createScaledBitmap(image, (0.5f*w).toInt(),(0.2*h).toInt(),false)

        if(!rotating)
            canvas.drawBitmap(image,(w-image.width)/2f,(h-image.height)/2f,null)
        else {
            buttonIV.setImageResource(R.drawable.ic_red_button)
            canvas.drawText("Ora devi disegnare il cazzo di bottone!", w/2f - 1000, h/2f, paint)
        }

        // Creating other rocks in different positions
        canvas.drawBitmap(image,(w-image.width*2f), (h-image.height*2f),null)
        canvas.drawBitmap(image,(w-image.width)/0.9f,(h-image.height*3).toFloat(),null)
        canvas.drawBitmap(image,(w-image.width)/3f,(h-image.height)/4f,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* If want to check the list of sensors on the device
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        deviceSensors.forEach{
            Log.d("Sens", it.toString())
        }
         */
    }

    override fun onResume() {
        super.onResume()
        // Need to register the listener when the fragment is on foregroung
        mRotationVect.also { grav ->
            sensorManager.registerListener(this,grav,SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Need to unregister the listener when the fragment is not in focus
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("Sens accuracy", "Accuracy"+p1.toString())
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val zValue = event?.values?.get(2)
        Log.d("Sens change", "Value on z: "+zValue.toString())

        /*  values[0]: x*sin(θ/2)
            values[1]: y*sin(θ/2)
            values[2]: z*sin(θ/2)
            values[3]: cos(θ/2)
            values[4]: estimated heading Accuracy (in radians) (-1 if unavailable

         */

        if (zValue != null) {
            if (abs(zValue) < 0.15) {
                Log.d("ANIMATION", "can move")
                rotating = true
                view?.let { drawScene(it) }
            }
        }

    }
}