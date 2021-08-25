package com.sarikaya.yemeksepeti.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.model.Foods
import com.sarikaya.yemeksepeti.view.restaurantDetailFragmentDirections
import java.io.File

class FoodsAdapter(val foodsList: ArrayList<Foods>) : RecyclerView.Adapter<FoodsAdapter.ViewHolder>() {

    private lateinit var db : FirebaseFirestore
    private lateinit var idList: ArrayList<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.food_cardview,parent,false)
        return FoodsAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FoodsAdapter.ViewHolder, position: Int) {
        holder.foodName.text = foodsList[position].title
        holder.foodDesc.text = foodsList[position].ingredients
        holder.foodPrice.text = foodsList[position].price.toString() + " TL"
        val image = foodsList[position].pic
        val imageRef = FirebaseStorage.getInstance().reference.child("images/$image.png")
        val localfile = File.createTempFile("tempImage","png")
        imageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.foodView.setImageBitmap(bitmap)
        }


        db = Firebase.firestore

//        db.collection("restaurants").document(foodsList[position].resId).collection("foods").get()
//            .addOnSuccessListener {
//                for(doc in it){
//                    idList.add(doc.id)
//                }
//            }
//        idList = arrayListOf()
        holder.itemView.setOnClickListener {
            val action = restaurantDetailFragmentDirections.actionRestaurantDetailFragmentToFoodDetailFragment(foodsList[position].resId,foodsList[position].title)
            it.findNavController().navigate(action)
        }
        holder.foodAdd.setOnClickListener {
            val action = restaurantDetailFragmentDirections.actionRestaurantDetailFragmentToFoodDetailFragment(foodsList[position].resId,foodsList[position].title)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return foodsList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val foodName: TextView
        val foodDesc: TextView
        val foodPrice: TextView
        val foodAdd : View
        val foodView: ImageView

        init {
            foodName = itemView.findViewById(R.id.foodName)
            foodDesc = itemView.findViewById(R.id.foodDesc)
            foodPrice = itemView.findViewById(R.id.foodPrice)
            foodAdd = itemView.findViewById(R.id.addView)
            foodView = itemView.findViewById(R.id.foodView)
        }
    }
}