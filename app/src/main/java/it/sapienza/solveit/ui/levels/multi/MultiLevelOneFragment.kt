package it.sapienza.solveit.ui.levels.multi

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import it.sapienza.solveit.R
import android.view.MotionEvent
import android.widget.TextView


class MultiLevelOneFragment : Fragment() {
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var button7: Button
    private lateinit var button8: Button
    private lateinit var button9: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Find the correct button and hold it"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 1"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_multi_level_one, container, false)

        val chosenButton  = chooseRandomButton(view)

        chosenButton.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Pressed
                chosenButton.setBackgroundColor(Color.GREEN)
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Released
                chosenButton.setBackgroundColor(Color.RED)
            }
            true
        }

        return view
    }

    private fun chooseRandomButton(view: View) : Button{
        val chosenButton: Button
        val rnd = (1..9).random()

        chosenButton = when (rnd) {
            1 -> { button1 = view.findViewById(R.id.button1)
                return button1 }
            2 -> { button2 = view.findViewById(R.id.button2)
                return button2 }
            3 -> { button3 = view.findViewById(R.id.button3)
                return button3 }
            4 -> { button4 = view.findViewById(R.id.button4)
                return button4 }
            5 -> { button5 = view.findViewById(R.id.button5)
                return button5 }
            6 -> { button6 = view.findViewById(R.id.button6)
                return button6 }
            7 -> { button7 = view.findViewById(R.id.button7)
                return button7 }
            8 -> { button8 = view.findViewById(R.id.button8)
                return button8 }
            9 -> { button9 = view.findViewById(R.id.button9)
                return button9 }
            else -> { button1 = view.findViewById(R.id.button1)
                button1 }
        }

        return chosenButton
    }

}