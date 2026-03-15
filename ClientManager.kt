package com.example.aboudousamioumoroubumaye_app

import com.example.aboudousamioumoroubumaye_app.model.Client

object ClientManager {
    private val clients = mutableListOf<Client>()
    private var nextId = 1

    fun getClients(): List<Client> = clients

    fun addClient(client: Client) {
        client.id = nextId++
        client.calculerReste()
        clients.add(client)
    }

    fun updateClient(updatedClient: Client) {
        val index = clients.indexOfFirst { it.id == updatedClient.id }
        if (index != -1) {
            updatedClient.calculerReste()
            clients[index] = updatedClient
        }
    }

    fun deleteClient(client: Client) {
        clients.remove(client)
    }

    fun getClientById(id: Int): Client? = clients.find { it.id == id }
}