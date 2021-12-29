package it.sapienza.solveit.ui.levels.multi

import android.app.Activity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.models.Constants
import it.sapienza.solveit.ui.models.MinMaxFilter

class MultiLevelTwoFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()
    private lateinit var numberET: EditText
    private lateinit var buttonMulti2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Guess the same number!"
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
            if (numberET.text != null && numberET.text.isNotEmpty()) {
                val number = numberET.text.toString()
                Log.d("number","numberEt.text=$number")
                buttonMulti2.isClickable = false
                numberET.isClickable = false
                //if (numberET.text == cloud.numberText)
                if(numberET.text.toString() == "5"){
                    nextLevel()
                } else{
                    buttonMulti2.isClickable = true
                    numberET.isClickable = true
                    Toast.makeText(context as Activity, "The other player choose 5." +
                            "Try again", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(context as Activity, "You must insert a number", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun nextLevel(){
        val bundle = Bundle()
        bundle.putInt(Constants.LEVEL, 2) // Say to the dialog that fragment 1 called it
        bundle.putBoolean(Constants.IS_SINGLE, false)
        winnerDialog.arguments = bundle
        winnerDialog.show(parentFragmentManager, Constants.NEXT_LEVEL)
    }

}