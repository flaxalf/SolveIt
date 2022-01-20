package it.sapienza.solveit.ui.levels.multi

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
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
import it.sapienza.solveit.ui.proxy.LevelTwoProxy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class MultiLevelTwoFragment : Fragment() {
    private val winnerDialog = CustomDialogFragment()
    private lateinit var numberET: EditText
    private lateinit var buttonMulti2: Button
    private lateinit var successInfoTV : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dynamically change hint and level number on the activity textviews'
        val activity = context as Activity
        val hint = activity.findViewById<TextView>(R.id.hintTV)
        hint.text = getString(R.string.hint_multi_level_two)
        val textLevel = activity.findViewById<TextView>(R.id.levelNumberTV)
        textLevel.text = getString(R.string.text_level_two)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_multi_level_two, container, false)

        numberET = view.findViewById(R.id.numberET)
        buttonMulti2 = view.findViewById(R.id.buttonMulti2)
        successInfoTV = view.findViewById(R.id.successInfoTV)

        // Assigning filters
        numberET.filters = arrayOf<InputFilter>(MinMaxFilter(0, 10))


        val sharedPref = activity?.getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPref?.getString(Constants.USERNAME, "Unrecognized_username")
        val id = sharedPref?.getString(Constants.ID, "Unrecognized_id")

        if (username != null && username.isNotEmpty() && id != null && id.isNotEmpty()) {
            val proxy = LevelTwoProxy(username, id)

            buttonMulti2.setOnClickListener {
                if (numberET.text != null && numberET.text.isNotEmpty()) {
                    val number = numberET.text.toString()
                    Log.d("number", "numberEt.text=$number")
                    buttonMulti2.isClickable = false
                    numberET.isClickable = false

                    GlobalScope.launch {
                        async {
                            proxy.selectNumber(number)

                        }
                    }

                    val timer = Timer("levelTwo", true)
                    timer.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            val waitResponse = proxy.getOtherPlayerChoice()
                            val success = waitResponse.getString("success")

                            when {
                                success.equals("success") -> {
                                    nextLevel()
                                    timer.cancel()
                                }
                                success.equals("retry") -> {
                                    val otherPlayerNumber = waitResponse.getString("number")

                                    activity?.runOnUiThread() {
                                        run() {
                                            buttonMulti2.isClickable = true
                                            numberET.isClickable = true

                                            successInfoTV.text = "Try again! The other " +
                                                    "player choose $otherPlayerNumber"
                                        }
                                    }
                                    timer.cancel()
                                }
                                success.equals("wait") -> {
                                    activity?.runOnUiThread(){
                                        run(){
                                            successInfoTV.text = "Waiting for the other player"
                                        }
                                    }

                                }
                            }
                        }


                    }, 1000, 1000)

                } else {
                    Toast.makeText(
                        context as Activity,
                        "You must insert a number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(activity, "System error", Toast.LENGTH_SHORT).show()
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