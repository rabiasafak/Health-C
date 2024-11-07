package com.example.healthcoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.healthcoach.databinding.ActivityAddDietBinding
import com.example.healthcoach.databinding.ActivityAddRecipeBinding
import com.google.firebase.database.FirebaseDatabase

class AddDietActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddDietBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityAddDietBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonVazgec.setOnClickListener(){
            var intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonKaydet.setOnClickListener(){
            var etad=binding.editTextTitle.text.toString()
            var etaciklama=binding.editTextDesc.text.toString()
            var etkategori="Diyet"


            if(TextUtils.isEmpty(etad)){
                binding.editTextTitle.error="Lütfen veri girişi yapınız"
            }
            if(TextUtils.isEmpty(etaciklama)){
                binding.editTextDesc.error="Lütfen veri girişi yapınız"
            }

            else{

                val database= FirebaseDatabase.getInstance()
                var databaseReferance= database.reference.child("favoriler")
                var id=databaseReferance.push()
                id.child("id").setValue(id.key.toString())
                id.child("adi").setValue(etad)
                id.child("aciklama").setValue(etaciklama)
                id.child("kategori").setValue(etkategori)
                Toast.makeText(applicationContext,"Diyet Kayıt Edildi", Toast.LENGTH_SHORT).show()
                var intent2=Intent(this, MainActivity::class.java)
                startActivity(intent2)
                finish()



            }
        }
    }
}