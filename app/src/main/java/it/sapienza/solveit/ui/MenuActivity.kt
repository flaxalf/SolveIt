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




class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val logoIV = findViewById<ImageView>(R.id.logoIV)
        assets.open("LOGO SOLVE IT.png")
        logoIV.setImageDrawable(Drawable.createFromStream(assets.open("LOGO SOLVE IT.png"), null))

        val animZoom = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom)
        val btnSingle = findViewById<Button>(R.id.buttonSingle)

        btnSingle.setOnClickListener{
            btnSingle.startAnimation(animZoom)
            startActivity(Intent(this@MenuActivity, LevelsActivity::class.java))
        }
    }

    // for handling back button of the Android Device
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}