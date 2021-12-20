package it.sapienza.solveit.ui.levels

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import it.sapienza.solveit.R


class LevelTwoFragment : Fragment(), View.OnClickListener {
    private lateinit var buttonIV2: ImageView
    private val winnerDialog = CustomDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // The fragment contains a custom view that handle sensor and canvas drawing
        val view: View = inflater.inflate(R.layout.fragment_level_two, container, false)

        buttonIV2 = view.findViewById(R.id.buttonIV2)
        buttonIV2.isClickable = false

        return view
    }

    fun activateButton(v: View) {
        buttonIV2 = v.findViewById(R.id.buttonIV2)
        buttonIV2.setOnClickListener(this)
        buttonIV2.alpha = 1f
        buttonIV2.isClickable = true
    }

    override fun onClick(v: View) {
        val bundle = Bundle()
        bundle.putInt("Level", 2) // Say to the dialog that fragment 2 call it

        winnerDialog.arguments = bundle
        when (v.id) {
            R.id.buttonIV2 -> {
                winnerDialog.show(parentFragmentManager, "Next level")
            }
        }
    }
}