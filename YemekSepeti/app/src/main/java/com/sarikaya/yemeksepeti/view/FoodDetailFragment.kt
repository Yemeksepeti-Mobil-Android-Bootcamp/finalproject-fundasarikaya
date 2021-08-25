package com.sarikaya.yemeksepeti.view

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.databinding.FragmentFoodDetailBinding
import com.sarikaya.yemeksepeti.databinding.FragmentRestaurantDetailBinding
import java.io.File
import kotlin.math.roundToInt
import kotlin.Float as Float1

class FoodDetailFragment : Fragment() {

    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val args by navArgs<FoodDetailFragmentArgs>()
    private var count=1
    private  var price: kotlin.Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore
        getData(db)

        binding.addFood.setOnClickListener {
            count++
            binding.foodAdet.text = count.toString() + " Adet"
            binding.foodDetailPrice.text = String.format("%.2f",count*price)
        }


        binding.removeFood.setOnClickListener {
            if(count>1){
                count--
                binding.foodAdet.text = count.toString() + " Adet"
                binding.foodDetailPrice.text = String.format("%.2f",count*price)
            }
        }
    }

    private fun getData(db: FirebaseFirestore){
        db.collection("restaurants").document(args.id).collection("foods").get()
            .addOnSuccessListener { food->
                for(doc in food){
                    if(args.title == doc.data["title"].toString()){
                        binding.foodDetailName.text = doc.data["title"].toString()
                        binding.foodDetailDesc.text = doc.data["ingredients"].toString()
                        binding.foodDetailPrice.text = doc.data["price"].toString() + " TL"
                        price = doc.data["price"].toString().toFloat()
                        val image = doc.data["pic"].toString()
                        val imageRef = FirebaseStorage.getInstance().reference.child("images/$image.png")
                        val localfile = File.createTempFile("tempImage","png")
                        imageRef.getFile(localfile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                            binding.foodDetailView.setImageBitmap(bitmap)
                        }
                    }
                }
            }
    }

}