package com.example.aboudousamioumoroubumaye_app

import com.example.aboudousamioumoroubumaye_app.model.Client
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aboudousamioumoroubumaye_app.databinding.ActivityClientDetailBinding
import android.net.Uri

class ClientDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientDetailBinding
    private lateinit var client: Client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clientId = intent.getIntExtra("CLIENT_ID", -1)
        if (clientId == -1) {
            Toast.makeText(this, "Erreur : client non trouvé", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        client = ClientManager.getClientById(clientId) ?: run {
            Toast.makeText(this, "Client non trouvé", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        displayClientDetails()

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddEditClientActivity::class.java)
            intent.putExtra("CLIENT_ID", client.id)
            startActivity(intent)
        }

        binding.btnDelete.setOnClickListener {
            ClientManager.deleteClient(client)
            Toast.makeText(this, "Client supprimé", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayClientDetails() {
        binding.tvDetailTitle.text = "Détails de ${client.nomPrenoms}"

        if (client.photoPath.isNotEmpty()) {
            binding.ivDetailPhoto.setImageURI(Uri.parse(client.photoPath))
        }

        binding.tvDetailNom.text = "Nom & Prénoms : ${client.nomPrenoms}"
        binding.tvDetailTelephone.text = "Téléphone : ${client.telephone}"
        // ... tous les autres TextView comme avant
        binding.tvDetailSommeTotale.text = "Somme totale : ${String.format("%.0f", client.sommeTotale)} FCFA"
        binding.tvDetailAvance.text = "Avance : ${String.format("%.0f", client.avance)} FCFA"
        binding.tvDetailReste.text = "Reste à payer : ${String.format("%.0f", client.reste)} FCFA"
    }
}