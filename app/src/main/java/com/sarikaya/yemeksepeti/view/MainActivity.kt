 package com.sarikaya.yemeksepeti.view

import android.content.ClipData
import android.content.ClipData.Item
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.databinding.ActivityLoginBinding
import com.sarikaya.yemeksepeti.databinding.ActivityMainBinding
import com.sarikaya.yemeksepeti.databinding.FragmentRestaurantsBinding
import kotlinx.android.synthetic.main.activity_main.*

 class MainActivity : AppCompatActivity() {

     private lateinit var binding: ActivityMainBinding
     private lateinit var navController: NavController
     private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController=findNavController(R.id.navHostFragment)
        bottom_nav.setupWithNavController(navController)
        auth = FirebaseAuth.getInstance()

        bottom_nav.menu.findItem(R.id.otherNavBar).setOnMenuItemClickListener { menuItem ->
            logOut()
            true
        }
    }

     private fun logOut(){
         auth.signOut()
         val intent = Intent(this,LoginActivity::class.java)
         startActivity(intent)
         finish()
     }
}