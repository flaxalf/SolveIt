package it.sapienza.solveit.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import it.sapienza.solveit.databinding.ActivitySignUpBinding
import it.sapienza.solveit.ui.MenuActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.SignUpButton.setOnClickListener {
            val email : String = binding.EmailSignUp.text.toString()
            val password : String = binding.PasswordSignUp.text.toString()

            if (email.length == 0) {
                binding.EmailSignUp.setError("Please enter an E-mail!")
                return@setOnClickListener
            }
            if (password.length == 0) {
                binding.PasswordSignUp.setError("Please enter a password!")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val loginIntent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(loginIntent)

                } else{
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

