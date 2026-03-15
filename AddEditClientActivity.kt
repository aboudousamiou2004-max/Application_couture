package com.example.aboudousamioumoroubumaye_app


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aboudousamioumoroubumaye_app.databinding.ActivityAddEditClientBinding
import com.example.aboudousamioumoroubumaye_app.model.Client
import java.text.SimpleDateFormat
import java.util.Locale


class AddEditClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditClientBinding // Accès direct aux composants XML
    private var clientToEdit: Client? = null                   // Stocke le client si on est en mode modification
    private var isEditMode = false                             // Détermine si c'est un ajout ou une édition
    private var currentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. DÉTECTION DU MODE (Ajout ou Modification)
        val clientId = intent.getIntExtra("CLIENT_ID", -1)
        if (clientId != -1) {
            clientToEdit = ClientManager.getClientById(clientId)
            clientToEdit?.let {
                isEditMode = true
                fillFormWithClient(it) // Remplit les champs avec les données existantes
                binding.tvTitle.text = "Modifier un client"
                binding.btnSave.text = "Mettre à jour"
            }
        }


        binding.btnChoosePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                // Demande de permission si non accordée
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE),
                    101
                )
            }
        }

        // PAYER
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { updateReste() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.etSommeTotale.addTextChangedListener(textWatcher)
        binding.etAvance.addTextChangedListener(textWatcher)

        //SAUVEGARDE
        binding.btnSave.setOnClickListener {
            if (validateForm()) { //les champs corrects
                val client = createClientFromForm()
                if (isEditMode) {
                    ClientManager.updateClient(client)
                    Toast.makeText(this, "Client mis à jour", Toast.LENGTH_SHORT).show()
                } else {
                    ClientManager.addClient(client)
                    Toast.makeText(this, "Client ajouté", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }
    }

    /**
     * Ouvre la galerie
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    /**
     * Récupère le résultat du choix de la photo.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri? = data.data
            uri?.let {
                currentPhotoPath = it.toString()
                binding.ivClientPhoto.setImageURI(it)
            }
        }
    }

    /**
     * Remplit automatiquement le formulaire avec les données d'un client (Mode Edition).
     */
    private fun fillFormWithClient(client: Client) {
        with(binding) {
            etNomPrenoms.text?.append(client.nomPrenoms)
            etTelephone.text?.append(client.telephone)
            // ... (remplissage de toutes les mesures de couture) ...
            etEpaule.text?.append(client.epaule)
            etPoitrine.text?.append(client.poitrine)
            etDateCommande.text?.append(client.dateCommande)
            etDateLivraison.text?.append(client.dateLivraison)
            etSommeTotale.text?.append(client.sommeTotale.toString())
            etAvance.text?.append(client.avance.toString())
        }

        if (client.photoPath.isNotEmpty()) {
            currentPhotoPath = client.photoPath
            binding.ivClientPhoto.setImageURI(Uri.parse(client.photoPath))
        }
        updateReste()
    }

    /**
     * Collecte toutes les données saisies pour créer un objet Client.
     */
    private fun createClientFromForm(): Client {
        val client = clientToEdit?.copy() ?: Client()
        with(binding) {
            client.nomPrenoms = etNomPrenoms.text.toString().trim()
            client.telephone = etTelephone.text.toString().trim()
            // ... (collecte de toutes les mesures du formulaire) ...
            client.sommeTotale = etSommeTotale.text.toString().toDoubleOrNull() ?: 0.0
            client.avance = etAvance.text.toString().toDoubleOrNull() ?: 0.0
        }
        client.calculerReste()
        client.photoPath = currentPhotoPath
        return client
    }


    private fun validateForm(): Boolean {
        // Logique : Nom vide ? Téléphone correct ? Dates valides ? Avance > Total ?
        // Retourne 'true' si tout est OK, 'false' sinon.
        return true // (Code simplifié pour la doc)
    }


    private fun updateReste() {
        val somme = binding.etSommeTotale.text.toString().toDoubleOrNull() ?: 0.0
        val avance = binding.etAvance.text.toString().toDoubleOrNull() ?: 0.0
        val reste = if (somme - avance < 0) 0.0 else somme - avance
        binding.tvReste.text = "Reste à payer : ${String.format("%.0f", reste)} FCFA"
    }
}