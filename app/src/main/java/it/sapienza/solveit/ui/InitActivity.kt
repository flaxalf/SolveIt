package it.sapienza.solveit.ui

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.auth.LoginActivity
import it.sapienza.solveit.ui.auth.SignUpActivity

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.initLayout)
        val animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        val animZoom = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)
        val btnRegister = findViewById<Button>(R.id.buttonRegister)

        val newIntent = Intent(this@InitActivity, LoginActivity::class.java)
        val newIntentSignUp = Intent(this@InitActivity, SignUpActivity::class.java)

        btnLogin.setOnClickListener{
            btnLogin.startAnimation(animZoom)
            startActivity(newIntent)
        }

        btnRegister.setOnClickListener{
            btnRegister.startAnimation(animZoom)
            startActivity(newIntentSignUp)
        }
    }
}