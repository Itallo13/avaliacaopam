package com.example.appavaliacao

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Dados : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados)

        val db = Firebase.firestore

        val edtProcurarID: EditText = findViewById(R.id.edtProcurarID)
        val btnProcurar: Button = findViewById(R.id.btnProcurar)
        val txtNome: TextView = findViewById(R.id.txtNome)
        val txtId: TextView = findViewById(R.id.txtId)
        val txtEndereco: TextView = findViewById(R.id.txtEndereco)
        val txtBairro: TextView = findViewById(R.id.txtBairro)
        val txtCEP: TextView = findViewById(R.id.txtCEP)
        val btnExcluir: Button = findViewById(R.id.btnExcluir)
        val btnEDitar: Button = findViewById(R.id.btnEditar)

        db.collection("cadastro")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        btnProcurar.setOnClickListener {
            if (edtProcurarID.text.isNotEmpty()){
                val docRef = db.collection("cadastro").document(edtProcurarID.text.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                            txtId.setText("${document.id}")
                            txtNome.setText(" ${document.get("nome")}")
                            txtEndereco.setText(" ${document.get("endereco")}")
                            txtBairro.setText(" ${document.get("bairro")}")
                            txtCEP.setText("${document.get("cep")}")
                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }
            }else{
                Toast.makeText(this,"Insira um Id para completar a busca!", Toast.LENGTH_LONG).show()
            }
        }
        btnExcluir.setOnClickListener{

            db.collection("cadastro").document(edtProcurarID.text.toString())
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    txtId.setText(null)
                    txtNome.setText(null)
                    txtEndereco.setText(null)
                    txtBairro.setText(null)
                    txtCEP.setText(null)}
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
            Toast.makeText(this,"Deletado com Sucesso!", Toast.LENGTH_SHORT).show()
        }

        btnEDitar.setOnClickListener{
            val id = db.collection("cadastro").document(edtProcurarID.text.toString())

            // Set the "isCapital" field of the city 'DC'
            id
                .update(mapOf(
                    "nome" to txtNome.text.toString(),
                    "endereco" to txtEndereco.text.toString(),
                    "bairro" to txtBairro.text.toString(),
                    "cep" to txtCEP.text.toString()
                ))
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")}
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            Toast.makeText(this,"Atualizado com Sucesso!", Toast.LENGTH_SHORT).show()
        }
    }
}