package com.sarikaya.yemeksepeti.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.model.Favorites
import com.sarikaya.yemeksepeti.model.Restaurants
import com.sarikaya.yemeksepeti.view.MainFragmentDirections
import java.io.File

class oncekiSipAdapter(val restaurantList: ArrayList<Restaurants>,val favouriteList: ArrayList<Favorites>) : RecyclerView.Adapter<oncekiSipAdapter.ViewHolder>() {

    private lateinit var db: FirebaseFirestore
    private var auth = FirebaseAuth.getInstance()
    private var currentUser = auth.currentUser!!.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): oncekiSipAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.onceki_siparis_cardview,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: oncekiSipAdapter.ViewHolder, position: Int) {
        val restaurant: Restaurants =restaurantList[position]
        for (i in favouriteList){
            if (restaurantList[position].id == i.favourite){
                holder.pointText.text = restaurantList[position].point
                holder.restaurantName.text = restaurantList[position].title
            }
        }
        val image = restaurant.pic
        val imageRef = FirebaseStorage.getInstance().reference.child("images/$image.png")
        val localfile = File.createTempFile("tempImage","png")
        imageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.restaurantImage.setImageBitmap(bitmap)
        }


        holder.itemView.setOnClickListener {
            val action =MainFragmentDirections.actionMainFragmentToRestaurantDetailFragment(restaurantList[position].id)
            it.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val pointText: TextView
        val restaurantName: TextView
        val restaurantImage : ImageView
        init {
            pointText = itemView.findViewById(R.id.point_view_text)
            restaurantName = itemView.findViewById(R.id.restaurant_name)
            restaurantImage = itemView.findViewById(R.id.restaurant_image)
        }
    }


}