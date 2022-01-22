package it.sapienza.solveit.ui.levels.multi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import it.sapienza.solveit.R
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.models.Constants
import it.sapienza.solveit.ui.proxy.LevelOneProxy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.*


class MultiLevelOneFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()
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
        hint.text = getString(R.string.hint_multi_level_one)
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = getString(R.string.text_level_one)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_multi_level_one, container, false)
        val sharedPref = activity?.getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPref?.getString(Constants.USERNAME, "Unrecognized_username")
        val id = sharedPref?.getString(Constants.ID, "Unrecognized_id")

        if (username != null && username.isNotEmpty() && id != null && id.isNotEmpty()) {
            val chosenButton = chooseRandomButton(view)
            val proxy = LevelOneProxy(id, username)

            val timer = Timer("levelOne", true)
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    Log.d("timer", "run")
                    val waitResponse = proxy.getOtherPlayerChoice()
                    val success = waitResponse.getBoolean("success")

                    if(success){
                        nextLevel()
                        timer.cancel()
                        Log.d("timer", "cancel")
                    }
                }


            }, 1000, 1000)

            chosenButton.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    // Pressed
                    chosenButton.background = activity?.getDrawable(R.drawable.button_oval_shape_green)
                    GlobalScope.launch {
                        async {
                            proxy.sendMyChoice(true)
                        }
                    }
                } else if (event.action == MotionEvent.ACTION_UP) {
                    // Released
                    chosenButton.background = activity?.getDrawable(R.drawable.button_oval_shape_red)
                    GlobalScope.launch {
                        async {
                            proxy.sendMyChoice(false)
                        }
                    }
                }
                true
            }


        } else {
            Toast.makeText(activity, "System error", Toast.LENGTH_SHORT).show()
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

    private fun nextLevel(){
        val bundle = Bundle()
        bundle.putInt(Constants.LEVEL, 1) // Say to the dialog that fragment 1 called it
        bundle.putBoolean(Constants.IS_SINGLE, false)
        winnerDialog.arguments = bundle
        winnerDialog.show(parentFragmentManager, Constants.NEXT_LEVEL)
    }

}