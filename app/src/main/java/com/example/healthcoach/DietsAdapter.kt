package com.example.healthcoach

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DietsAdapter(private val userList: ArrayList<User>, val context: Context) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewAd: TextView =view.findViewById(R.id.textViewAd)
        val textViewAciklama: TextView =view.findViewById(R.id.textViewAciklama)
        val textViewKategori: TextView =view.findViewById(R.id.textViewKategori)
        val imageView: ImageView =view.findViewById(R.id.imageView)


    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecipeAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pop_item, viewGroup, false)

        return RecipeAdapter.ViewHolder(view)
    }

    override fun getItemCount()=userList.size

    override fun onBindViewHolder(viewHolder: RecipeAdapter.ViewHolder, position: Int) {

        var user=userList[position]
        viewHolder.textViewAd.text = userList[position].adi
        viewHolder.textViewAciklama.text = userList[position].aciklama
        viewHolder.textViewKategori.text = userList[position].kategori

        if (userList[position].userImg!!.isEmpty()) {
            viewHolder.imageView.setImageResource(R.drawable.img_6);
        } else{
            Picasso.get().load(userList[position].userImg).into(viewHolder.imageView);
        }

        viewHolder.itemView.setOnClickListener(){

            var adi:String?=user.adi
            var aciklama:String?=user.aciklama
            var kategori:String?=user.kategori
            val userImg:String?=user.userImg
            var intent= Intent(context,DietDetailActivity::class.java)
            intent.putExtra("puturl",userImg)
            intent.putExtra("putadi",adi)
            intent.putExtra("putaciklama",aciklama)
            intent.putExtra("putkategori",kategori)

            context.startActivity(intent)
        }
    }
}