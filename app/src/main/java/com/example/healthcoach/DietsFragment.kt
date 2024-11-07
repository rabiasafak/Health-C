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
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcoach.databinding.FragmentBlogBinding
import com.example.healthcoach.databinding.FragmentDietsBinding
import com.example.healthcoach.databinding.FragmentRecipesBinding
import com.google.firebase.database.*
import java.text.Collator
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class DietsFragment : Fragment() {
    private lateinit var binding: FragmentDietsBinding
    private lateinit var recyclerViewDiets: RecyclerView
    private lateinit var dietsArrayList:ArrayList<User>
    private lateinit var dbref: DatabaseReference
    private var fragmentContext: Context? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentDietsBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Store the context when the fragment is attached
        fragmentContext = context
    }
    override fun onStart() {
        super.onStart()

        val id ="-NecA4OchxN7F8CEdx6k"
        val newTitle = "Lahana Diyeti"
        val newDescription="Merkezinde bol şifalı lahana çorbasının yer aldığı ve hızlı kilo verdirmesi sebebiyle oldukça popüler olan bir diyet lahana çorbası diyeti. Güçlü ve hızlı yağ yakımını sağlayan lahana çorbası diyetinde lahana çorbası dışında yağsız süt, sebze ve meyve gibi bir ya da iki çeşit besin yer alıyor. Zaten kendisi oldukça düşük kalorili olan lahana çorbasından aldığınızdan daha fazla kaloriyi yaktırdığı bile iddia edilen bu diyette dikkat edilmesi gereken en önemli nokta 1 haftadan fazla süreyle devam ettirilmemesi. Eğer kilo vermeye devam etmek istiyorsanız 15 gün ara verip yeniden başlamalısınız. "+
                "\n"+
                "Sınırsız lahana çorbası ve sebze tüketebilirsiniz. Burada dikkat etmeniz gereken ise şu; bezelye, mısır ve fasulye yememelisiniz. Akşam saatlerinde bir adet haşlanmış patates de yiyebilirsiniz. İkinci günde meyve hiç tüketmemeli, bol bol su içmeyi de unutmamalısınız.\n" +
                "\n" +
                "3.gün:\n" +
                "\n" +
                "Sınırsız lahana çorbası içebilir, mısır ve patates gibi nişastalı besinler hariç doyana kadar sebze tüketebilirsiniz. Muz hariç diğer meyveleri de yiyebilir, şekersiz çay içebilirsiniz. Su içmeye yine özen gösterin.\n" +
                "\n" +
                "4.gün:\n" +
                "\n" +
                "Sınırsız lahana çorbası içebilir, üç adet muz tüketebilir ve yağsız süt içebilirsiniz.\n" +
                "\n" +
                "5.gün:\n" +
                "\n" +
                "500 grama kadar beyaz ya da kırmızı et ve taze domates yiyebilirsiniz. Lahana çorbasını sınırsız içebilirsiniz.\n" +
                "\n" +
                "6.gün:\n" +
                "\n" +
                "Sınırsız lahana çorbasının yanı sıra yağsız kırmızı et ve patates hariç pişmiş sebze yiyebilirsiniz.\n" +
                "\n" +
                "7.gün:\n" +
                "\n" +
                "Son günde yine sınırsız lahana çorba, buna ilave olarak esmer pirinç, düşük kalorili sebzeler tüketebilir, katkısız, doğal meyve sularından içebilirsiniz."
        if (id != null) {
            updateData(id, newTitle, newDescription)
        }
        recyclerViewDiets=binding.recyclerViewDiets
        recyclerViewDiets.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recyclerViewDiets.setHasFixedSize(true)
        dietsArrayList= arrayListOf<User>()
        getRecipesData()

        binding.buttonFilterKulturel.setOnClickListener(){
            binding.buttonFilterPop.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterUnluler.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterKulturel.setTypeface(null, Typeface.BOLD)
            filterDataByCategory("Kültürel")
        }
        binding.buttonFilterPop.setOnClickListener(){
            binding.buttonFilterKulturel.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterUnluler.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterPop.setTypeface(null, Typeface.BOLD)
            filterDataByCategory("Popüler")
        }
        binding.buttonFilterUnluler.setOnClickListener(){
            binding.buttonFilterPop.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterKulturel.setTypeface(null, Typeface.NORMAL)
            binding.buttonFilterUnluler.setTypeface(null, Typeface.BOLD)
            filterDataByCategory("Ünlüler")
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

                val filteredList = dietsArrayList.filter { user ->
                    normalizeText(user.adi)?.contains(normalizedQuery.orEmpty(), true) == true
                }

                recyclerViewDiets.adapter = RecipeAdapter(ArrayList(filteredList), requireContext())


                return true
            }
        })
    }
    private fun updateData(id: String, newTitle: String, newDescription: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("diyet")

        // Create a map with the new data
        val newData = mapOf(
            "adi" to newTitle,
            "aciklama" to newDescription,

        )

        databaseReference.child(id).updateChildren(newData)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error updating data: ${e.message}")
            }
    }
    private fun normalizeText(text: String?): String? {
        return text?.trim()?.toLowerCase(Locale.getDefault())
    }
    private fun filterDataByCategory(category: String) {
        val filteredList = dietsArrayList.filter { user -> user.kategori == category }
        recyclerViewDiets.adapter = DietsAdapter(ArrayList(filteredList), requireContext())
    }
    private fun getRecipesData() {
        dbref= FirebaseDatabase.getInstance().getReference("diyet")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    if (snapshot.exists()) { //içindkileri dönüyor
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            dietsArrayList.add(user!!)
                        }
                        recyclerViewDiets.adapter =
                            DietsAdapter(dietsArrayList, requireContext())
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}