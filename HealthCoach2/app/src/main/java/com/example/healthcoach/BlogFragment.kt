package com.example.healthcoach

import android.content.ContentValues
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.healthcoach.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

import androidx.recyclerview.widget.RecyclerView
import com.example.healthcoach.databinding.FragmentBlogBinding
import com.google.firebase.database.*
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList
import java.text.Normalizer
import java.util.regex.Pattern

class BlogFragment : Fragment() {

    lateinit var binding: FragmentBlogBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var PopRecyclerView: RecyclerView
    private lateinit var userArrayList:ArrayList<User>
    private lateinit var PopulerArrayList:ArrayList<User>
    private var fragmentContext: Context? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentBlogBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Store the context when the fragment is attached
        fragmentContext = context

    }

    override fun onStart() {
        super.onStart()
        userRecyclerView=binding.userListRecylerView
        PopRecyclerView=binding.recyclerViewPopular
        userRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)
        userArrayList= arrayListOf<User>()
        PopRecyclerView.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        PopRecyclerView.setHasFixedSize(true)
        PopulerArrayList= arrayListOf<User>()
        getPopulerData()
        getUserData()


        binding.buttonFilterTumu.setOnClickListener(){
            binding.buttonFilterBakim.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterIOlus.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterPMS.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterTumu.setTypeface(null, Typeface.BOLD)
            getUserData()

        }
        binding.buttonFilterPMS.setOnClickListener(){
            binding.buttonFilterBakim.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterIOlus.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterPMS.setTypeface(null, Typeface.BOLD)
            filterDataByCategory("PMS")
        }
        binding.buttonFilterBakim.setOnClickListener(){
            binding.buttonFilterPMS.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterIOlus.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterBakim.setTypeface(null, Typeface.BOLD)
            filterDataByCategory("Bakım")
        }
        binding.buttonFilterIOlus.setOnClickListener(){
            binding.buttonFilterBakim.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterPMS.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterIOlus.setTypeface(null, Typeface.BOLD)
            filterDataByCategory("İyi Oluş")
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

                val filteredList = userArrayList.filter { user ->
                    normalizeText(user.adi)?.contains(normalizedQuery.orEmpty(), true) == true
                }

                userRecyclerView.adapter = CustomAdapter(ArrayList(filteredList), requireContext())


                return true
            }
        })

    }


    private fun normalizeText(text: String?): String? {
        return text?.trim()?.toLowerCase(Locale.getDefault())
    }

    private fun filterDataByCategory(category: String) {
        val filteredList = userArrayList.filter { user -> user.kategori == category }
        userRecyclerView.adapter = CustomAdapter(ArrayList(filteredList), requireContext())
    }

    private fun getUserData() {
        dbref= FirebaseDatabase.getInstance().getReference("post")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    if (snapshot.exists()) { //içindkileri dönüyor
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            userArrayList.add(user!!)
                        }
                        userRecyclerView.adapter = CustomAdapter(userArrayList, requireContext())
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    private fun getPopulerData() {
        dbref= FirebaseDatabase.getInstance().getReference("post")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){ //içindkileri dönüyor
                    var count=0
                    for(userSnapshot in snapshot.children.reversed()){
                        val user=userSnapshot.getValue(User::class.java)
                        PopulerArrayList.add(user!!)
                        count ++
                        if (count == 3) {
                            break}
                    }
                    PopRecyclerView.adapter=CustomAdapter(PopulerArrayList,requireContext())
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }



}