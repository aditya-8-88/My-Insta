package com.example.insta_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.insta_clone.databinding.ActivityLoginBinding
import com.example.insta_clone.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding by lazy {
            ActivityLoginBinding.inflate(layoutInflater)
        }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            if (binding.passwordField.editText?.text.toString().equals("") or
                binding.loginEmailField.editText?.text.toString().equals("")
            ) {
                Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                var user = User(binding.loginEmailField.editText?.text.toString(),
                    binding.passwordField.editText?.text.toString()
                )
                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener {
                        Toast.makeText(this@LoginActivity, "Logged in successfully", Toast.LENGTH_SHORT)
                            .show()
                        if (it.isSuccessful) {
                            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))

                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        binding.createAccount.setOnClickListener{
            startActivity(Intent(this@LoginActivity,signup::class.java))
            finish()
        }
    }
}