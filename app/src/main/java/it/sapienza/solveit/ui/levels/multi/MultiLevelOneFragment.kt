package it.sapienza.solveit.ui.levels.multi

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import it.sapienza.solveit.R
import android.view.MotionEvent



class MultiLevelOneFragment : Fragment() {
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


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_multi_level_one, container, false)

        button1 = view.findViewById(R.id.button1)
        button1.isClickable = true
        button1.setOnClickListener{
            button1.setBackgroundColor(Color.GREEN)
        }

        button1.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Pressed
                button1.setBackgroundColor(Color.GREEN)
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Released
                button1.setBackgroundColor(Color.RED)
            }
            true
        }

        return view
    }

}