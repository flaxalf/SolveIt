package it.sapienza.solveit.ui.levels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import it.sapienza.solveit.R

class LevelOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_one)

        val btn = findViewById(R.id.btnNextFragment) as Button

        /* Do this for 5 levels and replace them */
        val levelOneFragment = LevelOneFragment()
        val levelTwoFragment = LevelTwoFragment()

        //val levelOneFragment = LevelOneFragment()
        //val levelOneFragment = LevelOneFragment()
        //val levelOneFragment = LevelOneFragment()

        btn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, levelTwoFragment)
                commit()
            }
        }
    }
}