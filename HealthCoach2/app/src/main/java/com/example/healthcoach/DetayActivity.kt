package com.example.healthcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import com.bumptech.glide.Glide
import com.example.healthcoach.databinding.ActivityDetayBinding

class DetayActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityDetayBinding.inflate(layoutInflater)
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
        binding.detayAd.text=adi.toString()
        binding.detayAciklama.text=aciklama.toString()
        binding.detayKategori.text=kategori.toString()
        backImageButton.setOnClickListener(){
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}