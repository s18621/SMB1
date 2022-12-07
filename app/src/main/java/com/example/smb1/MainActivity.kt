package com.example.smb1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smb1.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val loginbutton: Button = binding.Login
        val signinbutton: Button = binding.Signin
        val settings: Button = binding.settingsButton

        settings.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            startActivity(intent)
        }

        signinbutton.setOnClickListener {
            if (binding.editUsername.text.toString() != "" || binding.editPassword.text.toString() != "") {
            auth.createUserWithEmailAndPassword(
                binding.editUsername.text.toString(),
                binding.editPassword.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        println(binding.editUsername.text.toString())
                        Toast.makeText(this, "Successfully registered!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ProductListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                } else{
                Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_SHORT).show()
            }
        }

        loginbutton.setOnClickListener {
            if (binding.editUsername.text.toString() != "" || binding.editPassword.text.toString() != "") {
                auth.signInWithEmailAndPassword(
                    binding.editUsername.text.toString(),
                    binding.editPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Successfully Logged In!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ProductListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}