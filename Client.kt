package com.example.aboudousamioumoroubumaye_app.model

data class Client(
    var id: Int = 0,

    // Informations personnelles
    var nomPrenoms: String = "",
    var telephone: String = "",

    // Mesures
    var epaule: String = "",
    var poitrine: String = "",
    var longueurTaille: String = "",
    var tourVentrale: String = "",
    var hanche: String = "",
    var longueurCorsage: String = "",
    var ceinture: String = "",
    var longueurJupe: String = "",
    var longueurRobe: String = "",
    var longueurManche: String = "",
    var tailleManche: String = "",
    var longueurPantalon: String = "",
    var longueurRobeCourte: String = "",
    var tourCuisse: String = "",
    var longueurGenoux: String = "",
    var tourGenoux: String = "",
    var bas: String = "",
    var autresMesures: String = "",

    // Suivi de commande
    var dateCommande: String = "",
    var dateLivraison: String = "",

    // Paiement
    var sommeTotale: Double = 0.0,
    var avance: Double = 0.0,
    var reste: Double = 0.0,

    // BONUS PHOTO
    var photoPath: String = ""
) {
    fun calculerReste() {
        reste = sommeTotale - avance
        if (reste < 0) reste = 0.0
    }
}