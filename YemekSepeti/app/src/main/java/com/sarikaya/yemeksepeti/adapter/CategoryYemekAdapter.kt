package com.sarikaya.yemeksepeti.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.R.*
import com.sarikaya.yemeksepeti.model.Favorites
import com.sarikaya.yemeksepeti.model.Restaurants
import com.sarikaya.yemeksepeti.view.RestaurantsFragmentDirections
import java.io.File

class CategoryYemekAdapter(val restaurantList: ArrayList<Restaurants>,val favouriteList: ArrayList<Favorites>, private val mContext: Context) : RecyclerView.Adapter<CategoryYemekAdapter.ViewHolder>() {

    private lateinit var db: FirebaseFirestore
    private var auth = FirebaseAuth.getInstance()
    private var currentUser = auth.currentUser!!.uid
    private lateinit var myYemekAdapter : CategoryYemekAdapter
    private lateinit var asd :String

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryYemekAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout.restaurant_cardview,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryYemekAdapter.ViewHolder, position: Int) {
        val restaurant: Restaurants =restaurantList[position]
        var count = 0
        db = Firebase.firestore
        holder.restaurantName.text = restaurantList[position].title
        holder.restaurantPoint.text = restaurantList[position].point
        holder.circularProgressDrawable.strokeWidth = 5f
        holder.circularProgressDrawable.centerRadius = 30f
        holder.circularProgressDrawable.start()
        val image = restaurant.pic
        val imageRef = FirebaseStorage.getInstance().reference.child("images/$image.png")
        val localfile = File.createTempFile("tempImage","png")
        imageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            holder.restaurantImage.setImageBitmap(bitmap)
        }

//        Glide
//            .with(mContext)
//            .load(imageRef)
//            .centerCrop()
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .placeholder(holder.circularProgressDrawable)
//            .into(holder.restaurantImage)



        for (i in favouriteList){
            if (i.favourite == restaurantList[position].id){
                holder.restaurantFav.setBackgroundResource(drawable.star_fill)
                break
            }
        }
        holder.restaurantFav.setOnClickListener {
            db.collection("users").document(currentUser).collection("favourites").get()
                .addOnSuccessListener { user->
                    for(doc in user){
                        if(restaurantList[position].id == doc.data["favourite"]){
                            count++
                            if (count>0){
                                asd = doc.id
                                break
                            }
                        }
                    }
                    if(count==0){
                        val data = Favorites(restaurantList[position].id)
                        db.collection("users").document(currentUser).collection("favourites").document().set(data)
                        favouriteList.add(
                            Favorites(restaurantList[position].id)
                        )
                        holder.restaurantFav.setBackgroundResource(R.drawable.star_fill)
                    }
                    else if(count>0){
                        db.collection("users").document(currentUser).collection("favourites").document(asd).delete()
                        holder.restaurantFav.setBackgroundResource(R.drawable.star_fill)
                        favouriteList.remove(
                            Favorites(restaurantList[position].id)
                        )
                    }
                }
        }

        holder.itemView.setOnClickListener {
            val action = RestaurantsFragmentDirections.actionRestaurantsFragmentToRestaurantDetailFragment(restaurantList[position].id)
            it.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        //val pointText: TextView
        val restaurantName: TextView
        val restaurantPoint: TextView
        val restaurantFav: View
        val restaurantImage : ImageView
        val circularProgressDrawable = CircularProgressDrawable(mContext)
        init {
            //pointText = itemView.findViewById(R.id.point_view_text)
            restaurantName = itemView.findViewById(id.restaurant_name)
            restaurantPoint = itemView.findViewById(id.point_text)
            restaurantFav = itemView.findViewById(id.FavouriteStar)
            restaurantImage = itemView.findViewById(R.id.restaurant_image)
        }
    }
}