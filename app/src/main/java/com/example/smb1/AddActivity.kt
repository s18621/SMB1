package com.example.smb1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.finansemanager.database.Shared
import com.example.smb1.Models.ModelDto
import com.example.smb1.databinding.ActivityAddBinding
import kotlin.concurrent.thread

class AddActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

         binding.Add.setOnClickListener {
            onSave(binding.root)
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onSave(view: View){

        val itemName = binding.itemName.text.toString().trim()
        val price = binding.price.text.toString().trim()
        val quantity = binding.quantity.text.toString().trim()
        val bought = binding.bought.isChecked


        validate(itemName, binding.itemName)
        validate(price, binding.price)
        validate(quantity, binding.quantity)


        val newModel = ModelDto(
            itemName = itemName,
            price = price.toFloat(),
            quantity = quantity.toInt(),
            bought = bought
        )
        thread {
            Shared.db?.modelBase?.save(newModel)
        }

        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }


    private fun validate(text: String, editText: EditText): Boolean {
        return if("" == text) {
            editText.requestFocus()
            editText.error = "Wrong Value"
            false
        } else true
    }
}