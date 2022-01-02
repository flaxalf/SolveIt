package it.sapienza.solveit.ui.levels.multi

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.models.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class MultiLevelThreeFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()

    private lateinit var timerTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Defuse the bomb!"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = "Level 3"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_multi_level_three, container, false)
        timerTV = view.findViewById(R.id.timerTV)

        startTimer()

        return view
    }


    private fun nextLevel(){
        val bundle = Bundle()
        bundle.putInt(Constants.LEVEL, 3) // Say to the dialog that fragment 1 called it
        bundle.putBoolean(Constants.IS_SINGLE, false)
        winnerDialog.arguments = bundle
        winnerDialog.show(parentFragmentManager, Constants.NEXT_LEVEL)
    }


    private fun startTimer() {
        val animator : ValueAnimator = ValueAnimator.ofInt(60, 0)
        animator.duration = 60000 //60 secs in ms
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            timerTV.text = animation.animatedValue.toString()
            if(timerTV.text == "0"){
                //TODO: restart level
                Log.d("timer", "restart level")
                animator.pause()
                sleep(1000)
                animator.start()

            }
        }
        animator.start()
    }
}