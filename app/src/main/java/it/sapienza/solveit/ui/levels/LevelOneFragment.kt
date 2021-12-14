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
import android.widget.ImageView
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.fragment.app.DialogFragment
import it.sapienza.solveit.R
import java.lang.Math.abs
import java.util.*




class LevelOneFragment : Fragment(), SensorEventListener, View.OnClickListener {
    lateinit var paint: Paint
    lateinit var rockIV: ImageView
    lateinit var buttonIV: ImageView
    lateinit var canvas : Canvas
    val w = 3000
    val h = 4000
    var rotating: Boolean = false

    lateinit var sensorManager: SensorManager
    var mRotationVect: Sensor? = null

    val levelTwoFragment = LevelTwoFragment()
    val winnerDialog = CustomDialogFragment()

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


    override fun onClick(v: View) {
        val bundle = Bundle()
        bundle.putInt("Level", 1) // Say to the dialog that fragment 1 call it

        winnerDialog.setArguments(bundle)
        when (v.id) {
            R.id.buttonIV -> {
                winnerDialog.show(parentFragmentManager, "Next level")
            }
        }
    }

    fun activeButton() {
        // Button active only after the animation of the rock
        buttonIV.alpha = 1f

        buttonIV.setOnClickListener(this)
    }

    fun animateRock(image: Bitmap) {
        // TODO: is not working, usa il tempo, ALTERNATIVA: fare una customview
        var i=0
        val max=2000
        val deltaT = 500L
        val omega = 0.00005f*360 //radial speed in degree/ms
        Log.d("anim","anim n.: "+i)
        val tt = object : TimerTask() {
            override fun run() {
                i++
                Log.d("anim","anim n.: "+i)

                /*
                activity?.runOnUiThread {
                    canvas.withRotation(omega * deltaT * i, w / 2f, h / 2f) {
                        drawBitmap(image, (w - image.width) / 2f, (h - image.height) / 2f, null)
                    }
                }
                */

                canvas.withTranslation (0f) {
                    drawBitmap(image, i + 0f, 0f, null)
                }


                if (i==max)
                    this.cancel()
                }
        }
        val timer = Timer("mover", true)
        timer.scheduleAtFixedRate(tt,0, deltaT)
    }

    fun drawScene(view: View) {
        rockIV = view.findViewById<View>(R.id.rockIV) as ImageView
        buttonIV = view.findViewById(R.id.buttonIV) as ImageView

        /* Creation of a canvas and a 2d bitmap rock */
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        rockIV.setImageBitmap(bitmap)
        canvas = Canvas(bitmap)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = resources.getColor(R.color.black)
        paint.textSize = 100F

        // Clean the canvas
        canvas.drawRGB(255,255,255)

        var image = BitmapFactory.decodeStream(activity?.assets?.open("rock_1920.png"))

        // Creating a scaled rock, the rotating one is at the center of the screen
        image=Bitmap.createScaledBitmap(image, (0.5f*w).toInt(),(0.2*h).toInt(),false)

        if(!rotating)
            canvas.drawBitmap(image,(w-image.width)/2f,(h-image.height)/2f,null)
        else {
            animateRock(image)
            // The button that was from the start hidden (with alpha = 0) is now visible
            activeButton()
        }

        // Creating other rocks in different positions
        canvas.drawBitmap(image,(w-image.width*2f), (h-image.height*2f),null)
        canvas.drawBitmap(image,(w-image.width)/0.9f,(h-image.height*3).toFloat(),null)
        canvas.drawBitmap(image,(w-image.width)/3f,(h-image.height)/4f,null)
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

    // Overriding sensors function, extracting info from them
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //Log.d("Sens accuracy", "Accuracy"+p1.toString())
    }

    override fun onSensorChanged(event: SensorEvent?) {
        /*  values[0]: x*sin(θ/2)   values[1]: y*sin(θ/2)
            values[2]: z*sin(θ/2)   values[3]: cos(θ/2)
            values[4]: estimated heading Accuracy (in radians) (-1 if unavailable)
         */
        val zValue = event?.values?.get(2)
        //Log.d("Sens change", "Value on z: "+zValue.toString())

        if (zValue != null) {
            if (abs(zValue) < 0.2 && !rotating) {
                rotating = true

                view?.let { drawScene(it) }
            }
        }
    }
}