package com.sarikaya.yemeksepeti.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.adapter.ViewPagerAdapter
import com.sarikaya.yemeksepeti.model.onBoardingData
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {

    var ViewPagerAdapter: ViewPagerAdapter? = null
    var TabLayout: TabLayout? = null
    var onBoardingViewPager: ViewPager? = null
    var sharedPreferences: SharedPreferences? = null
    var next: TextView? = null
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        if(restorePrefDAta()){
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        TabLayout = findViewById(R.id.tablayout)
        next = findViewById(R.id.next)


        val onBoardingData:MutableList<onBoardingData> = ArrayList()
        onBoardingData.add(onBoardingData("Acıktınız mı?","Birbirinden güzel Restoran ve Yemek çeşitleri!",R.drawable.viewpage1))
        onBoardingData.add(onBoardingData("Restoranını Seç!","Yüzlerce seçenek arasından restoranını ve yemeğini seç.",R.drawable.viewpage2))
        onBoardingData.add(onBoardingData("Hemen Teslim!","Hiç beklemeden yemeğine kavuş.",R.drawable.viewpage3))

        setOnBoardingViewPagerAdapter(onBoardingData)

        position = onBoardingViewPager!!.currentItem

        next?.setOnClickListener {
            if(position<onBoardingData.size){
                position++
                onBoardingViewPager!!.currentItem = position
            }
            if(position == onBoardingData.size){
                savePrefData()
                val intent = Intent(applicationContext,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        tablayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if(tab.position == onBoardingData.size -1){
                    next!!.text = "Hadi Başlayalım!"
                }else{
                    next!!.text = "İleri"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<onBoardingData>){

        onBoardingViewPager = findViewById(R.id.screenpager)
        ViewPagerAdapter = ViewPagerAdapter(this,onBoardingData)
        onBoardingViewPager!!.adapter = ViewPagerAdapter
        tablayout.setupWithViewPager(onBoardingViewPager)
    }

    private fun savePrefData(){
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putBoolean("first",true)
        editor.apply()
    }

    private fun restorePrefDAta(): Boolean{
        sharedPreferences = applicationContext.getSharedPreferences("pref",Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("first",false)
    }
}