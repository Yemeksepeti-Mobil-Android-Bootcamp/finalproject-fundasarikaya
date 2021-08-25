package com.sarikaya.yemeksepeti.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.adapter.FoodsAdapter
import com.sarikaya.yemeksepeti.databinding.FragmentRestaurantDetailBinding
import com.sarikaya.yemeksepeti.model.Foods
import com.sarikaya.yemeksepeti.model.Restaurants
import java.io.File

class restaurantDetailFragment : Fragment() {

    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var detailList : ArrayList<Restaurants>
    private lateinit var foodList: ArrayList<Foods>
    private lateinit var myAdapter: FoodsAdapter
    private val args by navArgs<restaurantDetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore
        getData(db)
        initView()
    }

    private fun initView(){
        binding.foodsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.foodsRecyclerView.setHasFixedSize(true)

        foodList = arrayListOf()
        myAdapter = FoodsAdapter(foodList)
        binding.foodsRecyclerView.adapter = myAdapter
        db = Firebase.firestore
        getFoodsData(db)
    }

    private fun getData(db: FirebaseFirestore){
        db.collection("restaurants").get()
            .addOnSuccessListener { restaurant->
                for(doc in restaurant){
                    if (args.id == doc.data["id"]){
                        binding.adress.text = doc.data["adress"].toString()
                        binding.minPrice.text = "Min: " + doc.data["min"].toString() +" "+ "TL"
                        binding.pointText.text = doc.data["point"].toString()
                        binding.DetailName.text = doc.data["title"].toString()
                        binding.workHours.text = "Çalışma Saatleri: " + doc.data["workHours"].toString()
                        val image = doc.data["pic"].toString()
                        val imageRef = FirebaseStorage.getInstance().reference.child("images/$image.png")
                        val localfile = File.createTempFile("tempImage","png")
                        imageRef.getFile(localfile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                            binding.DetailView.setImageBitmap(bitmap)
                        }
                    }
                }
            }
            .addOnFailureListener { exception->
                Log.w("errorDocuments","Error getting documents",exception)
            }
    }

    private fun getFoodsData(db:FirebaseFirestore){
        db.collection("restaurants").document(args.id).collection("foods").get()
            .addOnSuccessListener { food->
                for(doc in food){
                    foodList.add(
                        Foods(
                            doc.data["title"].toString(),
                            doc.data["ingredients"].toString(),
                            doc.data["price"].toString(),
                            doc.data["resId"].toString(),
                            doc.data["pic"].toString()
                        )
                    )
                }
                myAdapter.notifyDataSetChanged()
            }
    }
}