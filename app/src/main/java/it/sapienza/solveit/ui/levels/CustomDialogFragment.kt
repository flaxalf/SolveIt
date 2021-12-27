package it.sapienza.solveit.ui.levels

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.single.*
import it.sapienza.solveit.ui.models.Constants

class CustomDialogFragment: DialogFragment() {
    private lateinit var nextFragmentButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bundle: Bundle? = this.arguments
        var infoLevel = 0
        var isSingle = true

        if (bundle != null) {
            infoLevel = bundle.getInt(Constants.LEVEL)
            isSingle = bundle.getBoolean(Constants.IS_SINGLE)
        }

        val rootView: View = inflater.inflate(R.layout.fragment_custom_dialog, container, false)

        nextFragmentButton = rootView.findViewById(R.id.nextLevelButton) as Button
        nextFragmentButton.setOnClickListener {
            var levelFragment : Fragment?
            if(isSingle) {
                 levelFragment = when (infoLevel) {
                    0 -> LevelOneFragment()
                    1 -> LevelTwoFragment()
                    2 -> LevelThreeFragment()
                    3 -> LevelFourFragment()
                    4 -> LevelFiveFragment()
                    5 -> null
                    else -> null
                }
            } else{
                levelFragment = when (infoLevel) {
                    0 -> LevelOneFragment()
                    1 -> LevelTwoFragment()
                    2 -> LevelThreeFragment()
                    3 -> LevelFourFragment()
                    4 -> LevelFiveFragment()
                    5 -> null
                    else -> null
                }
            }
            if(levelFragment != null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, levelFragment).commit()
                dismiss()
            } else{
                startActivity(Intent(activity, WinActivity::class.java))
            }
        }
        return rootView
    }
}