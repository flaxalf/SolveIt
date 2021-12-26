package it.sapienza.solveit.ui.levels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import it.sapienza.solveit.R


class LevelOneFragment : Fragment(), View.OnClickListener {
    private lateinit var buttonIV: ImageView
    private val winnerDialog = CustomDialogFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // The fragment contains a custom view that handle sensor and canvas drawing
        val view: View = inflater.inflate(R.layout.fragment_level_one, container, false)

        buttonIV = view.findViewById(R.id.buttonIV)
        buttonIV.isClickable = false

        return view
    }

    fun activateButton(v: View) {
        buttonIV = v.findViewById(R.id.buttonIV)
        buttonIV.isClickable = true
        buttonIV.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val bundle = Bundle()
        bundle.putInt("Level", 1) // Say to the dialog that fragment 1 call it

        winnerDialog.arguments = bundle
        when (v.id) {
            R.id.buttonIV -> {
                winnerDialog.show(parentFragmentManager, "Next level")
            }
        }
    }
}