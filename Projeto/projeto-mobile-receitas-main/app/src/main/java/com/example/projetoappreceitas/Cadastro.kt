package com.example.projetoappreceitas

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class Cadastro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val auth = Firebase.auth
        setContentView(R.layout.activity_cadastro)
        val cadastroButton = findViewById<Button>(R.id.cadastro_button)
        val emailCadastro = findViewById<EditText>(R.id.emailCadastro)
        val senhaCadastro = findViewById<EditText>(R.id.passwordCadastro)
        val login = findViewById<TextView>(R.id.facaLogin)
        cadastroButton.setOnClickListener {
            val email = emailCadastro.text.toString()
            val password = senhaCadastro.text.toString()

            if(TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Digite o seu email", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Digite a sua senha", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            this,
                            "Conta criada com sucesso!",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(applicationContext, Home::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this,
                            "Erro ao criar conta",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }


        }
        login.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}