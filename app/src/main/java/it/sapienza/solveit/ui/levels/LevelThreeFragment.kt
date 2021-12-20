package it.sapienza.solveit.ui.levels

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import it.sapienza.solveit.R


class LevelThreeFragment : Fragment(), View.OnClickListener, SensorEventListener  {

    private lateinit var buttonIV3: ImageView
    private lateinit var glowStickGreyIV: ImageView
    private val winnerDialog = CustomDialogFragment()

    private lateinit var sensorManager: SensorManager
    private var mAccelerometer: Sensor? = null

    private var threshold : Double = 10.0 * SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "A light stick!"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 3"

        // SENSOR
        sensorManager =
            (context as? Activity)?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mAccelerometer.also { grav ->
            sensorManager.registerListener(this, grav, SensorManager.SENSOR_DELAY_UI)}


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_level_three, container, false)

        buttonIV3 = view.findViewById(R.id.buttonIV3)
        buttonIV3.isClickable = false

        glowStickGreyIV = view.findViewById(R.id.glowStickGreyIV)

        return view
    }

    private fun activateButton(v: View) {
        buttonIV3 = v.findViewById(R.id.buttonIV3)
        buttonIV3.setOnClickListener(this)
        buttonIV3.alpha = 1f
        buttonIV3.isClickable = true
    }

    override fun onClick(v: View) {
        val bundle = Bundle()
        bundle.putInt("Level", 3) // Say to the dialog that fragment 3 call it

        winnerDialog.arguments = bundle
        when (v.id) {
            R.id.buttonIV3 -> {
                winnerDialog.show(parentFragmentManager, "Next level")
            }
        }
    }


    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER) {

            var netForce = event.values[0]*event.values[0];

            netForce+=event.values[1]*event.values[1];
            netForce+=event.values[2]*event.values[2];

            if (threshold<netForce) {
                glowStickGreyIV.alpha -= 0.005f
            }
            //Log.d("Alpha", "alpha= " + glowStickGreyIV.alpha)
            if(glowStickGreyIV.alpha <= 0f){
                this.view?.let { activateButton(it) }
                sensorManager.unregisterListener(this)
            }

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //unused
    };

}