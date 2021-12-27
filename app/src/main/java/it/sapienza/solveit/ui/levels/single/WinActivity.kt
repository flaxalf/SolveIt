package it.sapienza.solveit.ui.levels.single

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.MenuActivity

class WinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        val btnSingle = findViewById<Button>(R.id.homeBtn)

        btnSingle.setOnClickListener{
            startActivity(Intent(this@WinActivity, MenuActivity::class.java))
        }
    }
}