package com.example.projetoappreceitas

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
            val auth = Firebase.auth
        val loginButton = findViewById<Button>(R.id.login_button)
        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val cadastrar = findViewById<TextView>(R.id.cadastrese)
        loginButton.setOnClickListener {
            val emailLogin = emailInput.text.toString()
            val passwordLogin = passwordInput.text.toString()

            if(TextUtils.isEmpty(emailLogin)) {
                Toast.makeText(this, "Digite o seu email", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(passwordLogin)) {
                Toast.makeText(this, "Digite a sua senha", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            baseContext,
                            "Sucesso no login",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(applicationContext, Home::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Falha no login",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
        cadastrar.setOnClickListener {
            val intent = Intent(applicationContext, Cadastro::class.java)
            startActivity(intent)
        }
    }
}