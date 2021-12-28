package it.sapienza.solveit.ui.levels.multi

import android.app.Activity
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.models.MinMaxFilter

class MultiLevelTwoFragment : Fragment() {
    private lateinit var numberET: EditText
    private lateinit var buttonMulti2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Guess with your friend the same number!"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 2"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_multi_level_two, container, false)

        numberET = view.findViewById(R.id.numberET)
        buttonMulti2 = view.findViewById(R.id.buttonMulti2)

        // Assigning filters
        numberET.filters = arrayOf<InputFilter>(MinMaxFilter(1, 10))

        buttonMulti2.setOnClickListener {
            if (numberET.text != null && !numberET.text.isEmpty()) {
                //if (numberET.text == cloud.numberText)
            }
        }

        return view
    }
}