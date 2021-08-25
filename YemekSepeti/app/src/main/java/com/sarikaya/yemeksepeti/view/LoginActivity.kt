package com.sarikaya.yemeksepeti.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.sarikaya.yemeksepeti.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val email = "sarikayamelihcan-01@outlook.com"
        val pass = "Mcan5142"
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.toString(),pass.toString()).addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}