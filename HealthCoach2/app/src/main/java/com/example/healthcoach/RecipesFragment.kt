package com.example.healthcoach

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcoach.databinding.FragmentFavsBinding
import com.example.healthcoach.databinding.FragmentRecipesBinding
import com.google.firebase.database.*
import java.text.Collator
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recyclerViewRecipes: RecyclerView
    private lateinit var recipesArrayList:ArrayList<User>
    private lateinit var dbref: DatabaseReference
    private var fragmentContext: Context? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentRecipesBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Store the context when the fragment is attached
        fragmentContext = context
    }

    override fun onStart() {
        super.onStart()

        val fragment = RecipesFragment()

        recyclerViewRecipes=binding.recyclerViewRecipes
        recyclerViewRecipes.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerViewRecipes.setHasFixedSize(true)
        recipesArrayList= arrayListOf<User>()
        getRecipesData()

        binding.buttonFiltertatli.setOnClickListener(){
            filterDataByCategory("Tatlı")
        }
        binding.buttonFilteranayemek.setOnClickListener(){
            filterDataByCategory("Ana Yemek")
        }
        binding.buttonFilteratistirmalik.setOnClickListener(){
            filterDataByCategory("Atıştırmalık")
        }
        val searchView = binding.searchView as android.widget.SearchView
        val turkishCollator = Collator.getInstance(Locale("tr", "TR"))

        val originalUserList = ArrayList(recipesArrayList)
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val trimmedText = newText?.trim()

                // Filter the data based on the normalized search query
                val normalizedQuery = normalizeText(trimmedText)

                val filteredList = recipesArrayList.filter { user ->
                    // Normalize the "adi" field and check if it contains the normalized query
                    normalizeText(user.adi)?.contains(normalizedQuery.orEmpty(), true) == true
                }

                // Update the RecyclerView with the filtered data
                recyclerViewRecipes.adapter = RecipeAdapter(ArrayList(filteredList), requireContext())

                return true
            }
        })
    }

    private fun normalizeText(text: String?): String? {
        return text?.trim()?.toLowerCase(Locale.getDefault())
    }
    private fun filterDataByCategory(category: String) {
        val filteredList = recipesArrayList.filter { user -> user.kategori == category }
        recyclerViewRecipes.adapter = RecipeAdapter(ArrayList(filteredList), requireContext())
    }
    private fun getRecipesData() {
        dbref= FirebaseDatabase.getInstance().getReference("tarif")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    if (snapshot.exists()) { //içindkileri dönüyor
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            recipesArrayList.add(user!!)
                        }
                        recyclerViewRecipes.adapter = RecipeAdapter(recipesArrayList, requireContext())
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}