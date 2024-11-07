package com.example.healthcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.healthcoach.databinding.ActivityDetailBinding
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityDetailBinding.inflate(layoutInflater)
        val backImageButton=binding.backImageButton
        setContentView(binding.root)
        val intent=intent
        var adi=intent.getStringExtra("putadi")
        var aciklama=intent.getStringExtra("putaciklama")
        var kategori=intent.getStringExtra("putkategori")
        val userImg=intent.getStringExtra("puturl")
        Log.e("img",userImg!!)
        Picasso.get().load(userImg).into(binding.ivRecipeImage)

        //tasraım aktar
        binding.tvRecipeTitle.text=adi.toString()
        binding.recipeDetailsTextView.text=aciklama.toString()
        backImageButton.setOnClickListener(){
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.recipeFavImageButton.setOnClickListener(){

            intent.putExtra("puturl",userImg)
            intent.putExtra("putadi", adi)
            intent.putExtra("putaciklama",aciklama)
            intent.putExtra("putkategori",kategori)

            val database= FirebaseDatabase.getInstance()
            var databaseReferance= database.reference.child("favoriler")
            var id=databaseReferance.push()
            id.child("id").setValue(id.key.toString())
            id.child("adi").setValue(adi)
            id.child("aciklama").setValue(aciklama)
            id.child("kategori").setValue(kategori)
            id.child("userImg").setValue(userImg)

        }
        binding.recipeFavImageButton.setOnClickListener(){
            Toast.makeText(this, "Favorilere Eklendi.", Toast.LENGTH_SHORT).show()
            val intentg=Intent(this,MainActivity::class.java)
            startActivity(intentg)
        }
    }
}