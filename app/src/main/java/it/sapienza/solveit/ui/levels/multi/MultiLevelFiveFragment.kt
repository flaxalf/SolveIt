package it.sapienza.solveit.ui.levels.multi

import android.animation.ValueAnimator
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
import kotlinx.coroutines.*


class MultiLevelFiveFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()
    private lateinit var counterTV: TextView
    private lateinit var buttonIV6: ImageView

    private var counter = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Reach exactly 100"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 5"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_multi_level_five, container, false)
        counterTV = view.findViewById(R.id.counterTV)
        buttonIV6 = view.findViewById(R.id.buttonIV6)


        buttonIV6.setOnClickListener {
            performAnimation()
        }


        return view
    }


    private fun performAnimation() {
        counter++

        if(counter == 100) {
            GlobalScope.launch {
                async {
                    delay(5000L)
                    if(counter == 100){
                        nextLevel()
                    }
                }
            }
        }
        val animator : ValueAnimator

        if(counter <= 100) {
             animator = ValueAnimator.ofInt(counter - 1, counter)
        } else{
            animator = ValueAnimator.ofInt(counter - 1, 0)
            counter = 0
        }
        animator.duration = 1
        animator.repeatCount = 0
        animator.addUpdateListener { animation ->
            counterTV.text = animation.animatedValue.toString()
        }
        animator.start()
    }




    private fun nextLevel(){
        val bundle = Bundle()
        bundle.putInt(Constants.LEVEL, 5) // Say to the dialog that fragment 5 called it
        bundle.putBoolean(Constants.IS_SINGLE, false)
        winnerDialog.arguments = bundle
        winnerDialog.show(parentFragmentManager, Constants.NEXT_LEVEL)
    }

}