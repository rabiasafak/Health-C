package com.example.healthcoach

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcoach.databinding.FragmentDietsBinding
import com.example.healthcoach.databinding.FragmentFavsBinding
import com.google.firebase.database.*
import java.text.Collator
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class FavsFragment : Fragment() {

    private lateinit var binding: FragmentFavsBinding
    private lateinit var recyclerViewFavs: RecyclerView
    private lateinit var favsArrayList:ArrayList<User>
    private lateinit var dbref: DatabaseReference
    private var fragmentContext: Context? = null
    private val rotateOpen:Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.open_anim) }
    private val rotateClose:Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.close_anim) }
    private val toBottom:Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }
    private val fromBottom:Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_button_anim) }
    private var clicked=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentFavsBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Store the context when the fragment is attached
        fragmentContext = context
    }
    override fun onStart() {
        super.onStart()
        recyclerViewFavs=binding.recyclerViewFavs
        recyclerViewFavs.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recyclerViewFavs.setHasFixedSize(true)
        favsArrayList= arrayListOf<User>()
        getFavsData()
        binding.fab.setOnClickListener(){
        onAddButtonClicked()
        }
        binding.fabDiyet.setOnClickListener(){
            var intent= Intent(requireContext(),AddDietActivity::class.java)
            startActivity(intent)

        }
        binding.fabTarif.setOnClickListener(){
            var intent= Intent(requireContext(),AddRecipeActivity::class.java)
            startActivity(intent)

        }
        binding.buttonFilterTarif.setOnClickListener(){
            binding.buttonFilterDiyet.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterTarif.setTypeface(null, Typeface.BOLD)
            val selectedCategories = listOf("Ana Yemek", "Atıştırmalık", "Tatlı","Tarif")
            filterDataByCategory(selectedCategories)
        }
        binding.buttonFilterDiyet.setOnClickListener(){
            binding.buttonFilterTarif.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterDiyet.setTypeface(null, Typeface.BOLD)
            val selectedCategories = listOf("Popüler", "Kültürel", "Ünlüler","Diyet")
            filterDataByCategory(selectedCategories)
        }
        val searchView = binding.searchView as android.widget.SearchView
        val turkishCollator = Collator.getInstance(Locale("tr", "TR"))
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val trimmedText = newText?.trim()
                val normalizedQuery = normalizeText(trimmedText)

                val filteredList = favsArrayList.filter { user ->
                    normalizeText(user.adi)?.contains(normalizedQuery.orEmpty(), true) == true
                }

                recyclerViewFavs.adapter = FavsAdapter(ArrayList(filteredList), requireContext())


                return true
            }
        })

    }


    private fun normalizeText(text: String?): String? {
        return text?.trim()?.toLowerCase(Locale.getDefault())
    }
    private fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }
    private fun setVisibility(clicked:Boolean){
        if(!clicked){
            binding.fabTarif.visibility=View.VISIBLE
            binding.fabDiyet.visibility=View.VISIBLE
        }else{
            binding.fabTarif.visibility=View.INVISIBLE
            binding.fabDiyet.visibility=View.INVISIBLE
        }

    }
    private fun setAnimation(clicked: Boolean){
        if(!clicked){
            binding.fabTarif.startAnimation(fromBottom)
            binding.fabDiyet.startAnimation(fromBottom)
            binding.fab.startAnimation(rotateOpen)
        }else{ binding.fabTarif.startAnimation(toBottom)
            binding.fabDiyet.startAnimation(toBottom)
            binding.fab.startAnimation(rotateClose)

        }

    }
    private fun setClickable(clicked: Boolean){
        if(!clicked){
            binding.fabTarif.isClickable=true
            binding.fabDiyet.isClickable=true
        }else{
            binding.fabTarif.isClickable=false
            binding.fabDiyet.isClickable=false
        }
    }
    private fun filterDataByCategory(categories: List<String>){
        val filteredList = favsArrayList.filter { user -> categories.contains(user.kategori) }
        recyclerViewFavs.adapter = FavsAdapter(ArrayList(filteredList), requireContext())
    }
    private fun getFavsData() {
        dbref= FirebaseDatabase.getInstance().getReference("favoriler")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    if (snapshot.exists()) { //içindkileri dönüyor
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            favsArrayList.add(user!!)
                        }
                        recyclerViewFavs.adapter =
                            FavsAdapter(favsArrayList, requireContext())
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}