package it.sapienza.solveit.ui.levels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import it.sapienza.solveit.R

class CustomDialogFragment: DialogFragment() {
    private lateinit var nextFragmentButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bundle: Bundle? = this.arguments
        var info_level : Int = 0

        if (bundle != null) {
            info_level = bundle.getInt("Level")
        }

        val rootView: View = inflater.inflate(R.layout.fragment_custom_dialog, container, false)

        nextFragmentButton = rootView.findViewById(R.id.nextLevelButton) as Button
        nextFragmentButton.setOnClickListener {
            // TODO: riempire con gli altri livelli
            val levelFragment: Fragment = when (info_level) {
                0 -> LevelOneFragment()
                1 -> LevelTwoFragment()
                2 -> LevelThreeFragment()
                3 -> LevelFourFragment()
                //4 -> LevelFiveFragment()
                else -> LevelOneFragment()
            }

            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, levelFragment).commit()
            dismiss()
        }
        return rootView
    }
}