package it.sapienza.solveit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.levels.LevelsActivity
import android.graphics.drawable.Drawable
import android.graphics.drawable.AnimationDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import it.sapienza.solveit.ui.models.Constants


class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.layout)
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        val animZoom = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom)
        val btnSingle = findViewById<Button>(R.id.buttonSingle)
        val btnMulti = findViewById<Button>(R.id.buttonMulti)

        val newIntent = Intent(this@MenuActivity, LevelsActivity::class.java)
        val bundle = Bundle()

        btnSingle.setOnClickListener{
            btnSingle.startAnimation(animZoom)
            bundle.putBoolean(Constants.IS_SINGLE, true)
            newIntent.putExtras(bundle)
            startActivity(newIntent)
        }

        btnMulti.setOnClickListener{
            btnMulti.startAnimation(animZoom)
            bundle.putBoolean(Constants.IS_SINGLE, false)
            newIntent.putExtras(bundle)
            startActivity(newIntent)
        }

    }

    // for handling back button of the Android Device
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}