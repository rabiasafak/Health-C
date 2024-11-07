package com.example.healthcoach

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Toast
import com.example.healthcoach.databinding.ActivityEditBinding
import com.example.healthcoach.databinding.ActivityFavDetailBinding
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intentb=intent
        val databaseReference = FirebaseDatabase.getInstance().getReference("favoriler")
        var butonKaydet=binding.butonKaydet
        var adi=intentb.getStringExtra("putadim")
        var aciklama=intentb.getStringExtra("putaciklamam")
        var kategori=intentb.getStringExtra("putkategorim")
        val userImg=intentb.getStringExtra("puturlm")
        var id =intentb.getStringExtra("putidm")
        val adiEditable = if (adi != null) SpannableStringBuilder(adi) else SpannableStringBuilder()
        val aciklamaEditable = if (aciklama != null) SpannableStringBuilder(aciklama) else SpannableStringBuilder()
        // Set the text of the EditTexts
        binding.editTextTitle.text = adiEditable
        binding.editTextDescription.text = aciklamaEditable

        butonKaydet.setOnClickListener(){

            val id = intentb.getStringExtra("putidm")
            val newTitle = binding.editTextTitle.text.toString()
            val newDescription = binding.editTextDescription.text.toString()

            if (id != null) {
                updateData(id, newTitle, newDescription)
            }
            Toast.makeText(applicationContext,"Güncellendi",Toast.LENGTH_SHORT).show()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()


        }
        binding.imgBack.setOnClickListener(){
            var intent=Intent(this,FavDetailActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun updateData(id: String, newTitle: String, newDescription: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("favoriler")


        val newData = mapOf(
            "adi" to newTitle,
            "aciklama" to newDescription,

        )

        databaseReference.child(id).updateChildren(newData)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e(TAG, "Error updating data: ${e.message}")
            }
    }

}