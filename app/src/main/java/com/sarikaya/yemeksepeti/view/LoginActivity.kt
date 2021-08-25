package com.sarikaya.yemeksepeti.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.databinding.ActivityLoginBinding
import com.sarikaya.yemeksepeti.databinding.ActivityMainBinding
import com.sarikaya.yemeksepeti.databinding.FragmentMainBinding
import com.sarikaya.yemeksepeti.model.user
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private  lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        auth = FirebaseAuth.getInstance()
        val guncelKullanici = auth.currentUser
        if(guncelKullanici != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.kayitol.setOnClickListener {
            if (binding.kayitol.text == "Hesabınız yokmu?"){
                binding.adressView.visibility = View.VISIBLE
                binding.adressText.visibility = View.VISIBLE
                binding.namesurnameView.visibility = View.VISIBLE
                binding.nameSurnameText.visibility = View.VISIBLE
                binding.Login.text = "Kayıt Ol"
                binding.loginButton.visibility = View.INVISIBLE
                binding.registerButton.visibility = View.VISIBLE
                binding.kayitol.text = "Giriş yap!"
                binding.welcome.text = "Kayıt ol"
            }else{
                binding.adressView.visibility = View.INVISIBLE
                binding.adressText.visibility = View.INVISIBLE
                binding.namesurnameView.visibility = View.INVISIBLE
                binding.nameSurnameText.visibility = View.INVISIBLE
                binding.Login.text = "Giriş Yap"
                binding.loginButton.visibility = View.VISIBLE
                binding.registerButton.visibility = View.INVISIBLE
                binding.kayitol.text = "Hesabınız yokmu?"
                binding.welcome.text = "Hoşgeldiniz"
            }

        }

    }
    fun girisYap(view: View){
        auth.signInWithEmailAndPassword(userText.text.toString(),passwordText.text.toString()).addOnCompleteListener { task->
            if(task.isSuccessful){
                val intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception->
            Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun kayitOl(view: View){
        db = Firebase.firestore
        auth.createUserWithEmailAndPassword(userText.text.toString(),passwordText.text.toString()).addOnCompleteListener { task->
            if(task.isSuccessful){
                val data = user(binding.nameSurnameText.text.toString(),binding.adressText.text.toString())
                db.collection("users").document(auth.currentUser!!.uid).set(data)
                val intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception->
            Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}