package com.example.smb1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.finansemanager.database.Shared
import com.example.smb1.Models.ModelAdapter
import com.example.smb1.Models.dbModel
import com.example.smb1.database.AppDatabase
import com.example.smb1.databinding.ActivityProductListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.concurrent.thread

class ProductListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProductListBinding.inflate(layoutInflater) }
    private val modelAdapter by lazy { ModelAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val addButton: FloatingActionButton = binding.addValue
        addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
            finish()
        }

        Shared.db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "groceries"
        ).build()

        //Setup Model List
        binding.view.apply {
            adapter = modelAdapter
            layoutManager = LinearLayoutManager(context)
        }
        reload()

    }

    private fun reload() = thread {
        Shared.db?.modelBase?.getAll()?.let {
            val newList = it.mapNotNull {
                dbModel(
                    it.price,
                    it.itemName,
                    it.quantity,
                    it.bought,
                )
            }
            modelAdapter.models = newList.toMutableList()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}