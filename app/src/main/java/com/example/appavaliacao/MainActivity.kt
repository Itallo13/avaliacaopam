package com.example.appavaliacao

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore

        val edtNome: EditText = findViewById(R.id.edtNome)
        val edtEndereco: EditText = findViewById(R.id.edtEndereco)
        val edtBairro: EditText = findViewById(R.id.edtBairro)
        val edtCEP: EditText = findViewById(R.id.edtCEP)
        val btnCadastrar: Button = findViewById(R.id.btnCadastro)
        val btnDados: Button = findViewById(R.id.btnDados)
        val txtID: EditText = findViewById(R.id.txtID)

        btnDados.setOnClickListener{
            val intent = Intent(this, Dados::class.java)
            startActivity(intent)
            txtID.setText(null)
        }

        btnCadastrar.setOnClickListener {
            if (edtNome.text.isEmpty() || edtEndereco.text.isEmpty() || edtBairro.text.isEmpty() || edtCEP.text.isEmpty()){
                Toast.makeText(this, "Insira os Dados!", Toast.LENGTH_LONG).show()
            }else{
                val user = hashMapOf(
                    "nome" to edtNome.text.toString(),
                    "endereco" to edtEndereco.text.toString(),
                    "bairro" to edtBairro.text.toString(),
                    "cep" to edtCEP.text.toString()
                )

                db.collection("cadastro")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        txtID.setText("Guarde seu Id: " + documentReference.id)
                        edtNome.setText(null)
                        edtEndereco.setText(null)
                        edtBairro.setText(null)
                        edtCEP.setText(null)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        }

    }
}