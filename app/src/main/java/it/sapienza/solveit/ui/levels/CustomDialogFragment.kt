package it.sapienza.solveit.ui.levels

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.multi.*
import it.sapienza.solveit.ui.levels.single.*
import it.sapienza.solveit.ui.models.Constants
import it.sapienza.solveit.ui.proxy.EndgameProxy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class CustomDialogFragment: DialogFragment() {
    private lateinit var nextFragmentButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bundle: Bundle? = this.arguments
        val sharedPref =
            activity?.getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
        var infoLevel = 0
        var isSingle = true

        if (bundle != null) {
            infoLevel = bundle.getInt(Constants.LEVEL)
            isSingle = bundle.getBoolean(Constants.IS_SINGLE)
        }
        val rootView: View = inflater.inflate(R.layout.fragment_custom_dialog, container, false)

        val winIntent = Intent(activity, WinActivity::class.java)
        if(infoLevel == 3 && !isSingle)
        {
            // Multiplayer win, need to stop the game and obtain the total_time
            val proxy = EndgameProxy()
            GlobalScope.launch {
                async {
                    // Get ID of the match
                    val id = sharedPref?.getString(Constants.ID, "Unrecognized_id")
                    val reply = proxy.end(id.toString())

                    Log.d("reply", "win multi: "+ reply.toString())

                    val postStatus = reply.getString("POST")

                    if (postStatus.equals("OK")) {
                        val endTime = reply.getString("time")
                        if (bundle != null) {
                            bundle.putBoolean(Constants.IS_SINGLE, false)
                            bundle.putString(Constants.END_TIME, endTime)
                            winIntent.putExtras(bundle)
                        } else {
                            // System error
                            Log.d("reply", "SYSTEM ERROR")
                        }
                    } else {
                        // Error with the server
                        Log.d("reply", "SERVER ERROR ENDING GAME")
                    }
                }
            }

        }


        nextFragmentButton = rootView.findViewById(R.id.nextLevelButton) as Button
        nextFragmentButton.setOnClickListener {
            val levelFragment : Fragment?
            if(isSingle) {
                 levelFragment = when (infoLevel) {
                    0 -> SingleLevelOneFragment()
                    1 -> SingleLevelTwoFragment()
                    2 -> SingleLevelThreeFragment()
                    3 -> SingleLevelFourFragment()
                    4 -> SingleLevelFiveFragment()
                    5 -> null
                    else -> null
                }
            } else{
                levelFragment = when (infoLevel) {
                    0 -> MultiLevelOneFragment()
                    1 -> MultiLevelTwoFragment()
                    2 -> MultiLevelThreeFragment()
                    3 -> null
                    else -> null
                }
            }
            if(levelFragment != null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, levelFragment).commit()
                dismiss()
            } else{

                startActivity(winIntent)
            }
        }

        this.isCancelable = false

        return rootView
    }
}