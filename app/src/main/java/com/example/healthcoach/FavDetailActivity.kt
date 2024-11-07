package com.example.healthcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.healthcoach.databinding.ActivityDetailBinding
import com.example.healthcoach.databinding.ActivityDetayBinding
import com.example.healthcoach.databinding.ActivityFavDetailBinding
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class FavDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityFavDetailBinding.inflate(layoutInflater)
        val backImageButton=binding.backImageButton
        val silBtn=binding.silBtn
        val duzenleBtn=binding.duzenleBtn
        setContentView(binding.root)
        val intent=intent
        var adi=intent.getStringExtra("putadi")
        var aciklama=intent.getStringExtra("putaciklama")
        var kategori=intent.getStringExtra("putkategori")
        val userImg=intent.getStringExtra("puturl")
        var idm =intent.getStringExtra("putid")
        if (!userImg.isNullOrEmpty()) { // Check if userImg is not empty or null
            Log.e("img", userImg!!)
            Picasso.get().load(userImg).into(binding.ivRecipeImage)
        } else {
           binding.ivRecipeImage.setImageResource(R.drawable.defaultrpic)
        }
        //tasraım aktar
        binding.detayAd.text=adi.toString()
        binding.detayAciklama.text=aciklama.toString()
        binding.duzenleBtn.setOnClickListener(){
            var intentb=Intent(this,EditActivity::class.java)
            intentb.putExtra("putidm",idm)
            intentb.putExtra("putadim",adi)
            intentb.putExtra("putaciklamam",aciklama)
            intentb.putExtra("puturlm",userImg)
            intentb.putExtra("putkategorim",kategori)
            startActivity(intentb)
            finish()
        }


        backImageButton.setOnClickListener(){
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        silBtn.setOnClickListener(){
            deleteRecord(idm.toString())
        }

    }
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("favoriler").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Silme İşlemi Başarılı", Toast.LENGTH_LONG).show()

            val intent = Intent(this,MainActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

}
