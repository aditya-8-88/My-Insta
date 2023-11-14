package com.example.insta_clone

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.insta_clone.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.insta_clone.models.User
import com.example.insta_clone.utils.USER_NODE
import com.example.insta_clone.utils.USER_PROFILE_FOLDER
import com.example.insta_clone.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class signup : AppCompatActivity() {
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it != null) {
                    user.image = it
                    binding.userProfilePhoto.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        user = User()
        binding.loginButton.setOnClickListener {
            if (binding.nameField.editText?.text.toString().equals("") or
                binding.emailField.editText?.text.toString().equals("") or
                binding.passwordField.editText?.text.toString().equals("")
            ) {
                Toast.makeText(this@signup, "Please fill all the fields!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.emailField.editText?.text.toString(),
                    binding.passwordField.editText?.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        user.name = binding.nameField.editText?.text.toString()
                        user.email = binding.emailField.editText?.text.toString()
                        user.password = binding.passwordField.editText?.text.toString()
                        Firebase.firestore.collection(USER_NODE)
                            .document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                startActivity(Intent(this@signup, HomeActivity::class.java))
                                finish()
                            }
                        Toast.makeText(
                            this@signup,
                            "You have registered successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@signup,
                            result.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        binding.userProfilePhotoPlusSymbol.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.Login1.setOnClickListener(){
            startActivity(Intent(this@signup, LoginActivity::class.java))
            finish()
        }
    }
}