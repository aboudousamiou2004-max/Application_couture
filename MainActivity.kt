package com.example.aboudousamioumoroubumaye_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aboudousamioumoroubumaye_app.adapter.ClientAdapter
import com.example.aboudousamioumoroubumaye_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ClientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFab()
    }

    private fun setupRecyclerView() {
        adapter = ClientAdapter { client ->
            val intent = Intent(this, ClientDetailActivity::class.java)
            intent.putExtra("CLIENT_ID", client.id)
            startActivity(intent)
        }
        binding.recyclerViewClients.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewClients.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAddClient.setOnClickListener {
            startActivity(Intent(this, AddEditClientActivity::class.java))
        }
    }

    private fun updateList() {
        adapter.submitList(ClientManager.getClients())
        if (ClientManager.getClients().isEmpty()) {
            binding.tvEmptyList.visibility = View.VISIBLE
            binding.recyclerViewClients.visibility = View.GONE
        } else {
            binding.tvEmptyList.visibility = View.GONE
            binding.recyclerViewClients.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }
}