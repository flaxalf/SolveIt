package it.sapienza.solveit.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import it.sapienza.solveit.databinding.ActivityLoginBinding
import it.sapienza.solveit.ui.MenuActivity
import android.content.SharedPreferences
import it.sapienza.solveit.ui.models.Constants


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.LoginButton.setOnClickListener {
            val email = binding.EmailLogin.text.toString().trim()
            val password = binding.PasswordLogin.text.toString().trim()

            if (email.isEmpty()) {
                binding.EmailLogin.error = "Please enter an E-mail!"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.PasswordLogin.error = "Please enter a password!"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                    // Save the username
                    val sharedPref = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString(Constants.USERNAME, email.substringBefore('@'))
                    editor.apply()

                    //Go to the main menu
                    val menuIntent = Intent(this@LoginActivity, MenuActivity::class.java)
                    startActivity(menuIntent)

                } else{
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}