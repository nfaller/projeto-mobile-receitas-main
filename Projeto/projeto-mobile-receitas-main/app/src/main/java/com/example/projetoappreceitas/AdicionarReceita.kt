package com.example.projetoappreceitas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.net.Uri
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage


class AdicionarReceita : Fragment() {

    val database = Firebase.database

    private lateinit var image_button: Button
    private lateinit var image_save: ImageView
    private lateinit var add_receita: Button
    private lateinit var addButton: Button
    private lateinit var ingredientsLayout: LinearLayout
    private var imagemUri: Uri? = null


    private val escolherImagemLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagemUri = uri
            image_save.setImageURI(uri)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_adicionar_receita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image_button = view.findViewById(R.id.image_button)
        val nome_receita = view.findViewById<EditText>(R.id.nome_receita)
        val descricao_receita = view.findViewById<EditText>(R.id.descricao_receita)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val modoDePreparo = view.findViewById<EditText>(R.id.modoDePreparo)
        val botaoLogout = view.findViewById<ImageButton>(R.id.botao_logout3)


        image_save = view.findViewById(R.id.image_save)
        addButton = view.findViewById(R.id.add_button)
        ingredientsLayout = view.findViewById(R.id.ingredientes)

        image_button.setOnClickListener {
            escolherImagemLauncher.launch("image/*")
        }

        add_receita = view.findViewById<Button>(R.id.add_receita)

        add_receita.setOnClickListener(){

                val ingredientesList = ArrayList<String>()

            for (i in 0 until ingredientsLayout.childCount) {
                val viewIngredient = ingredientsLayout.getChildAt(i);
                if (viewIngredient is EditText) {
                    val texto = viewIngredient.text.toString().trim()
                    if(texto.isNotEmpty()) {
                        ingredientesList.add(texto)
                    }
                }
            }

            if (imagemUri == null) {
                Toast.makeText(requireContext(), "Por favor, selecione uma imagem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nome = nome_receita.text.toString()
            val descricao = descricao_receita.text.toString()
            val modo = modoDePreparo.text.toString()
            val rating = ratingBar.rating

            if (nome.isEmpty() || descricao.isEmpty() || modo.isEmpty()
                || ingredientesList.size == 0) {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadImagem(imagemUri!!) { imageUrl ->
                salvarReceita(
                    Receita(
                        imagem = imageUrl,
                        nome = nome,
                        descricao = descricao,
                        ratingBar = rating,
                        ingredientes = ingredientesList,
                        modoDePreparo = modo
                    )
                )
            }

        }

        addButton.setOnClickListener {
            addNewIngredientField()
        }

        botaoLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();

            val intent = Intent(this.context, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Função para upload imagem
    private fun uploadImagem(uri: Uri, onSuccess: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().getReference("imagens_receitas/${uri.lastPathSegment}")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Erro ao fazer upload: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun salvarReceita(receita: Receita) {
        val myRef = database.getReference("receitas")
        val recipeId = myRef.push().key

        if (recipeId != null) {
            myRef.child(recipeId).setValue(receita)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, Home::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao adicionar receita", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Erro ao gerar ID da receita", Toast.LENGTH_SHORT).show()
        }
    }

    // Função para adicionar um novo campo de ingrediente
    private fun addNewIngredientField() {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 25
        }

        val newEditText = EditText(requireContext()).apply {
            this.layoutParams = layoutParams
            hint = "Adicionar ingrediente..."
            setBackgroundResource(R.drawable.input_beige)
            setPadding(43, 25, 17, 25)

            textSize = 15f
        }

        ingredientsLayout.addView(newEditText, ingredientsLayout.childCount - 1)
    }
}

