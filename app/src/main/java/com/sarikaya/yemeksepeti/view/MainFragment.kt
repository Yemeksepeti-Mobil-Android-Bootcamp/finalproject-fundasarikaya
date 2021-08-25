package com.sarikaya.yemeksepeti.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sarikaya.yemeksepeti.adapter.PreOrderAdapter
import com.sarikaya.yemeksepeti.adapter.oncekiSipAdapter
import com.sarikaya.yemeksepeti.databinding.FragmentMainBinding
import com.sarikaya.yemeksepeti.model.Favorites
import com.sarikaya.yemeksepeti.model.Restaurants
import com.sarikaya.yemeksepeti.model.oncekiler

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var restaurantList : ArrayList<Restaurants>
    private lateinit var favouriteList: ArrayList<Favorites>
    private lateinit var resFavList : ArrayList<Restaurants>
    private lateinit var preOrderList: ArrayList<oncekiler>
    private lateinit var myFavAdapter : oncekiSipAdapter
    private lateinit var myPreAdapter : PreOrderAdapter
    private lateinit var db: FirebaseFirestore
    private var auth = FirebaseAuth.getInstance()
    private var currentUser = auth.currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        initViewFavourite()
        initPreOrder()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore
        db.collection("users").get()
            .addOnSuccessListener {
                for (doc in it){
                    if(currentUser == doc.id){
                        binding.userNameText.text = doc.data["name"].toString()
                        binding.adressText.text = doc.data["adress"].toString()
                    }
                }
            }
    }

    private fun initViewFavourite(){
        binding.favorilerimRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favorilerimRecyclerView.setHasFixedSize(true)

        restaurantList = arrayListOf()
        favouriteList = arrayListOf()
        resFavList = arrayListOf()
        myFavAdapter = oncekiSipAdapter(resFavList,favouriteList)
        binding.favorilerimRecyclerView.adapter = myFavAdapter
        db = Firebase.firestore
        getData(db)
        favData(db)
    }

    private fun initPreOrder(){
        binding.oncekiSiparislerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.oncekiSiparislerRecyclerView.setHasFixedSize(true)

        preOrderList = arrayListOf()
        myPreAdapter = PreOrderAdapter(preOrderList)
        binding.oncekiSiparislerRecyclerView.adapter = myPreAdapter
        db = Firebase.firestore
        getPreData(db)
    }

    private fun getPreData(db: FirebaseFirestore){
        db.collection("users").document(currentUser).collection("oncekiler").get()
            .addOnSuccessListener { pre->
                for (doc in pre){
                    preOrderList.add(
                        oncekiler(
                            doc.data["title"].toString(),
                            doc.data["price"].toString()
                        )
                    )
                }
                myPreAdapter.notifyDataSetChanged()
            }
    }

    private fun getData(db: FirebaseFirestore){
        db.collection("restaurants").get()
            .addOnSuccessListener { restaurant->
                for(doc in restaurant){
                        restaurantList.add(
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
                myFavAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception->
                Log.w("errorDocuments","Error getting documents",exception)
            }
    }
    private fun favData(db: FirebaseFirestore){
        db.collection("users").document(currentUser).collection("favourites").get()
            .addOnSuccessListener { user ->
                for (doc in user){
                    favouriteList.add(
                        Favorites(
                            doc.data["favourite"].toString()
                        )
                    )
                }
                for (i in favouriteList){
                    for (x in restaurantList){
                        if (x.id == i.favourite){
                            resFavList.add(
                                Restaurants(
                                    x.adress,
                                    x.category,
                                    x.id,
                                    x.minPrice,
                                    x.point,
                                    x.title,
                                    x.workHours,
                                    x.pic
                                )
                            )
                        }
                    }
                }
                myFavAdapter.notifyDataSetChanged()
            }
    }

}


