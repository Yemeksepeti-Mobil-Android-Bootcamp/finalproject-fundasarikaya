package com.sarikaya.yemeksepeti.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.adapter.CategoryTatlilarAdapter
import com.sarikaya.yemeksepeti.adapter.CategoryYemekAdapter
import com.sarikaya.yemeksepeti.databinding.FragmentMainBinding
import com.sarikaya.yemeksepeti.databinding.FragmentRestaurantsBinding
import com.sarikaya.yemeksepeti.model.Favorites
import com.sarikaya.yemeksepeti.model.Restaurants
import kotlinx.android.synthetic.main.onboarding.*
import kotlinx.android.synthetic.main.restaurant_cardview.*
import java.util.*
import kotlin.collections.ArrayList

class RestaurantsFragment : Fragment() {

    private var _binding: FragmentRestaurantsBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private lateinit var yemeklerList: ArrayList<Restaurants>
    private lateinit var tatlilarList: ArrayList<Restaurants>
    private lateinit var tempYemekList: ArrayList<Restaurants>
    private lateinit var tempTatliList: ArrayList<Restaurants>
    private lateinit var favouriteList: ArrayList<Favorites>
    private lateinit var myYemekAdapter : CategoryYemekAdapter
    private lateinit var myTatliAdapter : CategoryTatlilarAdapter
    private var auth = FirebaseAuth.getInstance()
    private var currentUser = auth.currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantsBinding.inflate(inflater, container, false)
        initViewYemek()
        initViewTatli()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initViewYemek(){
        binding.yemekRecyclerView.layoutManager = LinearLayoutManager(requireContext(),OrientationHelper.HORIZONTAL,false)
        binding.yemekRecyclerView.setHasFixedSize(true)
        db = Firebase.firestore
        getData(db,"yemek")
        favData(db)
        yemeklerList = arrayListOf()
        favouriteList = arrayListOf()
        tempYemekList = arrayListOf()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()){
                    yemeklerList = tempYemekList
                    myYemekAdapter.notifyDataSetChanged()
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText.toString().lowercase().isEmpty()){
                    yemeklerList = tempYemekList
                    myYemekAdapter.notifyDataSetChanged()
                }

                if(newText.toString().lowercase().isNotEmpty()){
                    db.collection("restaurants").get()
                        .addOnSuccessListener {
                            for(doc in it){
                                if(doc.data["title"].toString().lowercase().contains(newText.toString().lowercase()) && doc.data["category"] == "yemek"){
                                    yemeklerList.clear()
                                    yemeklerList.add(
                                        Restaurants(
                                            doc.data["adress"].toString(),
                                            doc.data["category"].toString(),
                                            doc.data["id"].toString(),
                                            doc.data["minPrice"].toString(),
                                            doc.data["point"].toString(),
                                            doc.data["title"].toString(),
                                            doc.data["workHours"].toString(),
                                            doc.data["pic"].toString()
                                        )
                                    )
                                }
                                myYemekAdapter.notifyDataSetChanged()
                            }
                            myYemekAdapter.notifyDataSetChanged()
                        }
                    myYemekAdapter.notifyDataSetChanged()
                }else{
                    yemeklerList = tempYemekList
                    myYemekAdapter.notifyDataSetChanged()
                }

                return false
            }

        })

        myYemekAdapter = CategoryYemekAdapter(yemeklerList,favouriteList,requireContext())
        binding.yemekRecyclerView.adapter = myYemekAdapter
    }


    private fun initViewTatli(){
        binding.tatlilarRecyclerView.layoutManager = LinearLayoutManager(requireContext(),OrientationHelper.HORIZONTAL,false)
        binding.tatlilarRecyclerView.setHasFixedSize(true)
        db = Firebase.firestore
        getData(db,"tatli")
        tatlilarList = arrayListOf()
        tempTatliList = arrayListOf()

        myTatliAdapter = CategoryTatlilarAdapter(tatlilarList)
        binding.tatlilarRecyclerView.adapter = myTatliAdapter

    }
    private fun favData(db:FirebaseFirestore){
        db.collection("users").document(currentUser).collection("favourites").get()
            .addOnSuccessListener { user ->
                for (doc in user){
                    favouriteList.add(
                        Favorites(
                            doc.data["favourite"].toString()
                        )
                    )
                }
            }
    }


    private fun getData(db: FirebaseFirestore, category:String){
        db.collection("restaurants").get()
            .addOnSuccessListener { restaurant->
                for(doc in restaurant){
                    if(doc.data["category"].toString() == "yemek" && category == "yemek"){
                        yemeklerList.add(
                            Restaurants(
                                doc.data["adress"].toString(),
                                doc.data["category"].toString(),
                                doc.data["id"].toString(),
                                doc.data["minPrice"].toString(),
                                doc.data["point"].toString(),
                                doc.data["title"].toString(),
                                doc.data["workHours"].toString(),
                                doc.data["pic"].toString()
                            )
                        )
                        tempYemekList.add(
                            Restaurants(
                                doc.data["adress"].toString(),
                                doc.data["category"].toString(),
                                doc.data["id"].toString(),
                                doc.data["minPrice"].toString(),
                                doc.data["point"].toString(),
                                doc.data["title"].toString(),
                                doc.data["workHours"].toString(),
                                doc.data["pic"].toString()
                            )
                        )
                    }
                    if(doc.data["category"].toString() == "tatli" && category == "tatli"){
                        tatlilarList.add(
                            Restaurants(
                                doc.data["adress"].toString(),
                                doc.data["category"].toString(),
                                doc.data["id"].toString(),
                                doc.data["minPrice"].toString(),
                                doc.data["point"].toString(),
                                doc.data["title"].toString(),
                                doc.data["workHours"].toString(),
                                doc.data["pic"].toString()
                            )
                        )
                    }

                }
                myYemekAdapter.notifyDataSetChanged()
                myTatliAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception->
                Log.w("errorDocuments","Error getting documents",exception)
            }
    }

}