package it.sapienza.solveit.ui.levels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.MenuActivity

class LevelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)

        var homeIV = findViewById<ImageView>(R.id.homeIV)
        homeIV.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LevelsActivity, MenuActivity::class.java))
        })
    }

}