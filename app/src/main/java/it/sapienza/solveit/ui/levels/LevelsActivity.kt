package it.sapienza.solveit.ui.levels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import it.sapienza.solveit.R
import it.sapienza.solveit.ui.MenuActivity
import it.sapienza.solveit.ui.levels.multi.MultiLevelOneFragment
import it.sapienza.solveit.ui.levels.multi.MultiLevelThreeFragment
import it.sapienza.solveit.ui.levels.single.SingleLevelOneFragment
import it.sapienza.solveit.ui.models.Constants

class LevelsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)

        var isSingle = true
        val bundle: Bundle? = intent.extras
        if(bundle != null){
            isSingle = bundle.getBoolean(Constants.IS_SINGLE)
        }

        val homeIV = findViewById<ImageView>(R.id.homeIV)
        homeIV.setOnClickListener {
            startActivity(Intent(this@LevelsActivity, MenuActivity::class.java))
        }

        if(isSingle) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, SingleLevelOneFragment())
                .commit()
        } else{
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, MultiLevelThreeFragment())
                .commit()
        }

    }

}