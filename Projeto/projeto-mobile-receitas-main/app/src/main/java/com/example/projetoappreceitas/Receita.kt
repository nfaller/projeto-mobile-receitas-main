package com.example.projetoappreceitas

data class Receita(
    val imagem: String? = null,
    val nome: String? = null,
    val descricao: String? = null,
    val ratingBar: Float = 0.0f,
    val ingredientes: ArrayList<String>? = null,
    val modoDePreparo: String? = null
)