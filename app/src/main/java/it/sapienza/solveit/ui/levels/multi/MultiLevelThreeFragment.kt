package it.sapienza.solveit.ui.levels.multi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import it.sapienza.solveit.R
import android.widget.TextView
import android.widget.Toast
import it.sapienza.solveit.ui.levels.CustomDialogFragment
import it.sapienza.solveit.ui.models.Constants
import it.sapienza.solveit.ui.proxy.LevelThreeProxy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*


class MultiLevelThreeFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()
    private lateinit var counterTV: TextView
    private lateinit var buttonIV6: ImageView

    var timer = Timer("levelThree", true)
    lateinit var proxy : LevelThreeProxy

    private var goal = 50
    private var check = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = "Reach exactly $goal"
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = getString(R.string.text_level_three)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_multi_level_three, container, false)
        counterTV = view.findViewById(R.id.counterTV)
        buttonIV6 = view.findViewById(R.id.buttonIV6)

        val sharedPref =
            activity?.getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
        val id = sharedPref?.getString(Constants.ID, "Unrecognized_id")

        if (id != null && id.isNotEmpty()) {
            proxy = LevelThreeProxy(id)

            timer.scheduleAtFixedRate(TimerLevelThree(proxy), 1000, 1000)


            buttonIV6.setOnClickListener {
                GlobalScope.launch {
                    async {
                        val increaseResponse = proxy.increaseCounter()
                        val post = increaseResponse.getString("POST")
                        if (post.equals("OK")) {
                            val count = increaseResponse.getInt("count")
                            activity?.runOnUiThread() {
                                run() {
                                    counterTV.text = count.toString()
                                }
                            }
                        }
                    }
                }
            }

        } else {
            Toast.makeText(activity, "System error", Toast.LENGTH_SHORT).show()
        }

        return view
    }



    private fun nextLevel(){
        val bundle = Bundle()
        bundle.putInt(Constants.LEVEL, 3) // Say to the dialog that fragment 3 called it
        bundle.putBoolean(Constants.IS_SINGLE, false)
        winnerDialog.arguments = bundle
        winnerDialog.show(parentFragmentManager, Constants.NEXT_LEVEL)
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onResume() {
        super.onResume()
        timer = Timer("levelThree", true)
        timer.scheduleAtFixedRate(TimerLevelThree(proxy), 1000, 1000)
    }


    inner class TimerLevelThree(var proxy : LevelThreeProxy) : TimerTask(){
        override fun run() {
            Log.d("timer", "run")
            val counterResponse = proxy.readCounter()
            val count = counterResponse.getInt("count")

            activity?.runOnUiThread() {
                run() {
                    counterTV.text = count.toString()
                }
            }

            if(count == goal){
                check++
            } else{
                check = 0
            }

            if(check == 5){ //it means that the counter has been equal to goal for 5 seconds
                nextLevel()
                Log.d("timer", "cancel")
                timer.cancel()
            }
        }

    }

}