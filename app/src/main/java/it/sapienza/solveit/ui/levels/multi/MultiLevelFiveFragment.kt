package it.sapienza.solveit.ui.levels.multi

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import it.sapienza.solveit.R
import android.widget.TextView
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.models.Constants


class MultiLevelFiveFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()
    private lateinit var counterTV: TextView



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
        startAnimation(counterTV)

        return view
    }


    private fun startAnimation(textView: TextView) {
        val animator = ValueAnimator.ofInt(0, 600)
        animator.duration = 5000 // 5 seconds
        animator.addUpdateListener { animation ->
            textView.text = animation.animatedValue.toString()
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