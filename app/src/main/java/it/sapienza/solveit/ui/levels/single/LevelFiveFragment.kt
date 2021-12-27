package it.sapienza.solveit.ui.levels.single

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import it.sapienza.solveit.R

class LevelFiveFragment : Fragment(), View.OnClickListener {
    private lateinit var buttonIV5: ImageView
    private val winnerDialog = CustomDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Fire"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 5"
    }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // The fragment contains a custom view that handle sensor and canvas drawing
        val view = inflater.inflate(R.layout.fragment_level_five, container, false)

        buttonIV5 = view.findViewById(R.id.buttonIV5)
        buttonIV5.isClickable = false

        return view
    }

    fun activateButton(v: View) {
        buttonIV5 = v.findViewById(R.id.buttonIV5)
        buttonIV5.setOnClickListener(this)
        buttonIV5.alpha = 1f
        buttonIV5.isClickable = true
    }

    override fun onClick(v: View) {
        val bundle = Bundle()
        bundle.putInt("Level", 5) // Say to the dialog that fragment 5 call it

        winnerDialog.arguments = bundle
        when (v.id) {
            R.id.buttonIV5 -> {
                winnerDialog.show(parentFragmentManager, "Next level")
            }
        }
    }


}