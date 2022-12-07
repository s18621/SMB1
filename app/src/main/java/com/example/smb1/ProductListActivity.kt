package com.example.smb1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smb1.Models.ModelAdapter
import com.example.smb1.Models.dbModel
import com.example.smb1.databinding.ActivityProductListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class ProductListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProductListBinding.inflate(layoutInflater) }
    private val modelAdapter by lazy { ModelAdapter() }

    private val repo: databaseRepo
    val firebaseDatabase: FirebaseDatabase
    val allGroceries: MutableList<dbModel>

    init {
        firebaseDatabase = FirebaseDatabase.getInstance("https://smbgroceries-default-rtdb.europe-west1.firebasedatabase.app")
        repo = databaseRepo(firebaseDatabase)

        val dbModel = dbModel(
            "0",
            2.99,
            "Test",
            1,
            true
        )
        repo.insert(dbModel)
        repo.delete(dbModel)
        allGroceries = repo.allGroceries
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val addButton: FloatingActionButton = binding.addValue
        addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
            finish()
        }
        Log.e("ListActivity", "onCreate allG: ${repo.allGroceries}")
        //Setup Model List
        binding.view.apply {
            adapter = modelAdapter
            layoutManager = LinearLayoutManager(context)
            modelAdapter.allGroceries = repo.allGroceries
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}