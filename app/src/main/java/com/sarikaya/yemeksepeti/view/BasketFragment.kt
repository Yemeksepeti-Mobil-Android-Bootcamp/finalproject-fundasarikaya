package com.sarikaya.yemeksepeti.view

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.databinding.FragmentBasketBinding
import com.sarikaya.yemeksepeti.databinding.FragmentFoodDetailBinding
import com.sarikaya.yemeksepeti.model.oncekiler
import java.io.File

class BasketFragment : Fragment() {

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<BasketFragmentArgs>()
    private lateinit var db: FirebaseFirestore
    private var auth = FirebaseAuth.getInstance()
    private var currentUser = auth.currentUser!!.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)

        if (args.restaurantName == " "){
            binding.emptyBasket.visibility = View.VISIBLE
            binding.emptyBasketView.visibility = View.VISIBLE

        }

        binding.basketResName.text = args.restaurantName
        binding.basketFoodName.text = args.foodName
        binding.adet.text = args.count
        binding.basketNote.text = args.note
        binding.basketPrice.text = args.price

        db = Firebase.firestore
        db.collection("restaurants").document(args.restaurantName.toString()).collection("foods").get()
            .addOnSuccessListener {
                for (doc in it){
                    if (args.foodName == doc.data["title"].toString()){
                        if (args.foodName != " "){
                            val image = doc.data["pic"].toString()
                            val imageRef = FirebaseStorage.getInstance().reference.child("images/$image.png")
                            val localfile = File.createTempFile("tempImage","png")
                            imageRef.getFile(localfile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                                binding.basketFoodView.setImageBitmap(bitmap)
                            }
                        }

                    }
                }
            }
        db.collection("restaurants").document(args.restaurantName.toString()).get()
            .addOnSuccessListener {
                if (args.restaurantName != " "){
                    val image = it.data!!["pic"].toString()
                    val imageRef = FirebaseStorage.getInstance().reference.child("images/$image.png")
                    val localfile = File.createTempFile("tempImage","png")
                    imageRef.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        binding.basketResView.setImageBitmap(bitmap)
                    }
                }
            }

        binding.sepetionaylaview.setOnClickListener {
            val data = oncekiler(args.foodName.toString(),args.price.toString())
            db.collection("users").document(currentUser).collection("oncekiler").document().set(data)
            val action = BasketFragmentDirections.actionBasketFragmentToMainFragment()
            it.findNavController().navigate(action)

        }

        return binding.root
    }


}