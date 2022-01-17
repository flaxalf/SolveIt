package it.sapienza.solveit.ui.levels.multi

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import it.sapienza.solveit.R
import android.widget.TextView
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.models.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MultiLevelThreeFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()
    private lateinit var counterTV: TextView
    private lateinit var buttonIV6: ImageView

    private var counter = 0
    private var goal = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Reach exactly $goal"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 5"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_multi_level_three, container, false)
        counterTV = view.findViewById(R.id.counterTV)
        buttonIV6 = view.findViewById(R.id.buttonIV6)

        buttonIV6.setOnClickListener {
            performAnimation()
        }


        return view
    }


    private fun performAnimation() {
        counter++

        if(counter == goal) {
            GlobalScope.launch {
                async {
                    delay(5000L)
                    if(counter == goal){
                        nextLevel()
                    }
                }
            }
        }
        if(counter > goal) {
            counter = 0
        }
        counterTV.text = counter.toString()
        
    }


    private fun nextLevel(){
        val bundle = Bundle()
        bundle.putInt(Constants.LEVEL, 4) // Say to the dialog that fragment 4 called it
        bundle.putBoolean(Constants.IS_SINGLE, false)
        winnerDialog.arguments = bundle
        winnerDialog.show(parentFragmentManager, Constants.NEXT_LEVEL)
    }

}