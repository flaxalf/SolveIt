package it.sapienza.solveit.ui.levels.multi

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.models.Constants

class MultiLevelThreeFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()

    private lateinit var timerTV: TextView
    private lateinit var bombIV: ImageView
    private lateinit var clockIV: ImageView


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
        bombIV = view.findViewById(R.id.bombIV)
        clockIV = view.findViewById(R.id.clockIV)

        bombIV.setImageDrawable(Drawable.createFromStream(activity?.assets?.open("bomb.png"), null))
        clockIV.setImageDrawable(Drawable.createFromStream(activity?.assets?.open("clock.png"), null))

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

    fun startTimer() {
        var counter = 60
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTV.text = counter.toString()
                counter--
            }
            override fun onFinish() {
                timerTV.text = "BOOM"
            }
        }.start()
    }
}