package com.sarikaya.yemeksepeti.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.model.Favorites
import com.sarikaya.yemeksepeti.model.Restaurants
import com.sarikaya.yemeksepeti.view.RestaurantsFragmentDirections
import java.io.File

class CategoryTatlilarAdapter(val tatlilarList: ArrayList<Restaurants>) : RecyclerView.Adapter<CategoryTatlilarAdapter.ViewHolder>() {

    private lateinit var db: FirebaseFirestore
    private var auth = FirebaseAuth.getInstance()
    private lateinit var favouriteList : ArrayList<Favorites>
    private var currentUser = auth.currentUser!!.uid
    private lateinit var asd :String
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryTatlilarAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_cardview,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryTatlilarAdapter.ViewHolder, position: Int) {
        val restaurant: Restaurants =tatlilarList[position]
        var count = 0
        db = Firebase.firestore
        favouriteList = arrayListOf()
        holder.restaurantName.text = tatlilarList[position].title
        holder.restaurantPoint.text = tatlilarList[position].point
        val image = restaurant.pic
        val imageRef = FirebaseStorage.getInstance().reference.child("images/$image.png")
        val localfile = File.createTempFile("tempImage","png")
        imageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.restaurantImage.setImageBitmap(bitmap)
        }



        for (i in favouriteList){
            fav(i.favourite,tatlilarList[position].id,holder)
        }
        holder.restaurantFav.setOnClickListener {
            db.collection("users").document(currentUser).collection("favourites").get()
                .addOnSuccessListener { user->
                    for(doc in user){
                        if(tatlilarList[position].id == doc.data["favourite"]){
                            count++
                            if (count>0){
                                asd = doc.id
                                break
                            }
                        }
                    }
                    if(count==0){
                        val data = Favorites(tatlilarList[position].id)
                        db.collection("users").document(currentUser).collection("favourites").document().set(data)
                        holder.restaurantFav.setBackgroundResource(R.drawable.star_fill)
                    }
                    else if(count>0){
                        db.collection("users").document(currentUser).collection("favourites").document(asd).delete()
                        holder.restaurantFav.setBackgroundResource(R.drawable.star_fill)
                    }
                }
        }

        holder.itemView.setOnClickListener {
            val action = RestaurantsFragmentDirections.actionRestaurantsFragmentToRestaurantDetailFragment(tatlilarList[position].id)
            it.findNavController().navigate(action)
        }

    }
    private fun fav(i:String, x:String, holder: ViewHolder){
        if(i==x){
            holder.restaurantFav.setBackgroundResource(R.drawable.star_fill)
        }
    }
    override fun getItemCount(): Int {
        return tatlilarList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //val pointText: TextView
        val restaurantName: TextView
        val restaurantPoint: TextView
        val restaurantFav: View
        val restaurantImage : ImageView
        init {
            //pointText = itemView.findViewById(R.id.point_view_text)
            restaurantName = itemView.findViewById(R.id.restaurant_name)
            restaurantPoint = itemView.findViewById(R.id.point_text)
            restaurantFav = itemView.findViewById(R.id.FavouriteStar)
            restaurantImage = itemView.findViewById(R.id.restaurant_image)
        }
    }
}