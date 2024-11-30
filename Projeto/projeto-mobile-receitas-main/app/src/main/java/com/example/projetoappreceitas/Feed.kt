package com.example.projetoappreceitas

import RecipeAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale


class Feed : Fragment(), RecipeAdapter.MyClickListener {

    private lateinit var adapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private val receitas: MutableList<Receita> = mutableListOf()
    private val databaseReference = FirebaseDatabase.getInstance().getReference("receitas")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botaoLogout = view.findViewById<ImageView>(R.id.botao_logout)
        val searchView = view.findViewById<SearchView>(R.id.search_bar)
        searchView.clearFocus();
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView_receitas)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        botaoLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();

            val intent = Intent(this.context, MainActivity::class.java)
            startActivity(intent)
        }

        adapter = RecipeAdapter(receitas, this@Feed)
        recyclerView.adapter = adapter

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                receitas.clear()

                for(receitaSnapshot in snapshot.children) {
                    val receita = receitaSnapshot.getValue(Receita::class.java)
                    receita?.let {
                        receitas.add(receita)
                    }
                }
                receitas.reverse()
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })

    }

    override fun onItemClick(receitaSelecionada: Receita) {
        val ingredientesArraylist = ArrayList(receitaSelecionada.ingredientes ?: listOf())

        val fragment = VisualizarReceita.newInstance(
            receitaSelecionada.imagem?: "",
            receitaSelecionada.nome ?: "",
            receitaSelecionada.descricao ?: "",
            receitaSelecionada.modoDePreparo ?: "",
            receitaSelecionada.ratingBar ?: 0f,
            ingredientesArraylist
        )

        if (isAdded) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            Toast.makeText(context, "Erro ao abrir a receita", Toast.LENGTH_SHORT).show()
        }
    }

    fun searchList(text: String) {
        if (text.isEmpty()) {
            adapter.resetSearch()
        } else {
            val searchList = receitas.filter {
                it.nome?.contains(text, ignoreCase = true) == true
            }.toMutableList()
            adapter.searchDataList(searchList)
        }
    }

}
