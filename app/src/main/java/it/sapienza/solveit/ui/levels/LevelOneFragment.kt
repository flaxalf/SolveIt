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


class LevelOneFragment : Fragment(), View.OnClickListener {
    private lateinit var buttonIV: ImageView
    private val winnerDialog = CustomDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // The fragment contains a custom view that handle sensor and canvas drawing
        val view: View = inflater.inflate(R.layout.fragment_level_one, container, false)

        buttonIV = view.findViewById(R.id.buttonIV)
        buttonIV.setOnClickListener(this)

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
}