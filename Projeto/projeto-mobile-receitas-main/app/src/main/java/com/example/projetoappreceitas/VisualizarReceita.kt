package com.example.projetoappreceitas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class VisualizarReceita : Fragment() {
    private var imagemReceita: String? = null
    private var nomeReceita: String? = null
    private var descricaoReceita: String? = null
    private var ratingReceita: Float? = null
    private var ingredientesReceita: List<String>? = null
    private var modoDePreparo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagemReceita = it.getString("IMAGEM_RECEITA")
            nomeReceita = it.getString("NOME_RECEITA")
            descricaoReceita = it.getString("DESCRICAO_RECEITA")
            ratingReceita = it.getFloat("RATING_RECEITA")
            ingredientesReceita = it.getStringArrayList("INGREDIENTES_RECEITA")
            modoDePreparo = it.getString("MODO_DE_PREPARO")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para o fragment
        val view = inflater.inflate(R.layout.fragment_visualizar_receita, container, false)

        // Atualizar as views com os dados recebidos
        view.findViewById<TextView>(R.id.nomeReceitaTextView).text = nomeReceita
        view.findViewById<TextView>(R.id.descricaoReceitaTextView).text = descricaoReceita
        view.findViewById<RatingBar>(R.id.receitaRatingBar).rating = ratingReceita ?: 0f
        view.findViewById<TextView>(R.id.ingredientesTextView).text = ingredientesReceita?.joinToString(", ")
        view.findViewById<TextView>(R.id.modoDePreparoTextView).text = modoDePreparo
        val botaoLogout = view.findViewById<ImageView>(R.id.botao_logout2)

        // Carregar a imagem
        val imagemView = view.findViewById<ImageView>(R.id.imagemReceitaImageView)
        imagemReceita?.let {
            Glide.with(this)
                .load(it)  // URL da imagem
                .placeholder(R.drawable.placeholder) // Imagem de carregamento
                .error(R.drawable.error_image) // Imagem de erro
                .into(imagemView)  // A ImageView onde a imagem será carregada
        }

        botaoLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();

            val intent = Intent(this.context, MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(
            imagemReceita: String,
            nomeReceita: String,
            descricaoReceita: String,
            modoDePreparo: String,
            ratingReceita: Float,
            ingredientesReceita: ArrayList<String>
        ) = VisualizarReceita().apply {
            arguments = Bundle().apply {
                putString("IMAGEM_RECEITA", imagemReceita)
                putString("NOME_RECEITA", nomeReceita)
                putString("DESCRICAO_RECEITA", descricaoReceita)
                putString("MODO_DE_PREPARO", modoDePreparo)
                putFloat("RATING_RECEITA", ratingReceita)
                putStringArrayList("INGREDIENTES_RECEITA", ingredientesReceita)
            }
        }
    }
}