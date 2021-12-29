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
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.models.Constants

class SingleLevelFourFragment : Fragment(), View.OnClickListener {
        private lateinit var buttonIV4: ImageView
        private val winnerDialog = CustomDialogFragment()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Dynamically change hint and level number on the activity textviews'
            val activity = context as Activity
            val hint = activity.findViewById<TextView>(R.id.hintTV)
            hint.text = "Inflate it"
            val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
            textLevel.text = "Level 4"
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
            // The fragment contains a custom view that handle sensor and canvas drawing
            val view = inflater.inflate(R.layout.fragment_single_level_four, container, false)

            buttonIV4 = view.findViewById(R.id.buttonIV4)
            buttonIV4.isClickable = false

            return view
        }

        fun activateButton(v: View) {
            buttonIV4 = v.findViewById(R.id.buttonIV4)
            buttonIV4.setOnClickListener(this)
            buttonIV4.alpha = 1f
            buttonIV4.isClickable = true
        }

        override fun onClick(v: View) {
            val bundle = Bundle()
            bundle.putInt(Constants.LEVEL, 4) // Say to the dialog that fragment 4 called it
            bundle.putBoolean(Constants.IS_SINGLE, true)
            winnerDialog.arguments = bundle
            when (v.id) {
                R.id.buttonIV4 -> {
                    winnerDialog.show(parentFragmentManager, Constants.NEXT_LEVEL)
                }
            }
        }


}