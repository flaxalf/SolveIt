package it.sapienza.solveit.ui.levels

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import it.sapienza.solveit.R

class LevelFourFragment : Fragment() {
    private lateinit var balloonIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Fiuuu"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 4"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_level_four, container, false)

        balloonIV = view.findViewById(R.id.balloonIV)

        balloonIV.setOnClickListener(View.OnClickListener {
            increaseSize()
        })

        return view
    }

    fun increaseSize() {
        Log.d("size", "must increase size")
        // Come ingrandire una image view?
        // Invece del click dovrebbe esserci il soffio nel microfono, ma come funziona?
        // forse difficile
        // alternativa: rendere il tutto una view, e permettere di trascinare il palloncino all'ago
        // quando collide scoppia
    }

}