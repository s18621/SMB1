package com.example.smb1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.finansemanager.database.Shared
import com.example.smb1.Models.ModelDto
import com.example.smb1.databinding.ActivityAddBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.i("AddActivity", "AddActivity intent indentifier: ${intent}")


        val id = intent.getLongExtra("itemID", -1)
        val itemName = intent.getStringExtra("itemName")
        val price = intent.getFloatExtra("price", 0f)
        val quantity = intent.getIntExtra("quantity", 0)
        val bought = intent.getBooleanExtra("bought", false)
        Log.i("AddActivity", "AddActivity inside if cation vals: $id, $itemName, $price")
        if (id.toInt() != -1) {
            Log.i("AddActivity", "AddActivity Inside IF")
            binding.itemName.setText(itemName)
            binding.price.setText(price.toString())
            binding.quantity.setText(quantity.toString())
            binding.bought.isChecked = bought
        }

        binding.Add.setOnClickListener {
            onSave(binding.root)
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }

        binding.Cancel.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun onSave(view: View) {

        val itemName = binding.itemName.text.toString().trim()
        val price = binding.price.text.toString().trim()
        val quantity = binding.quantity.text.toString().trim()
        val bought = binding.bought.isChecked

        validate(itemName, binding.itemName)
        validate(price, binding.price)
        validate(quantity, binding.quantity)

        val intentId = intent.getLongExtra("itemID", -1).toInt()

        if (intentId != -1) {
            runBlocking {
                val job = launch {
                    Shared.db?.modelBase?.updateFromId(
                        intentId.toLong(),
                        price.toFloat(),
                        quantity.toInt(),
                        itemName,
                        bought
                    )
                }
                job.join()
                finish()
            }
        } else {

            var id: Long? = 0
            val newModel = ModelDto(
                itemName = itemName,
                price = price.toFloat(),
                quantity = quantity.toInt(),
                bought = bought
            )

            runBlocking {
                val job = launch {
                    id = Shared.db?.modelBase?.save(newModel)
                }
                job.join()
            }

            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
            setResult(Activity.RESULT_OK, intent)
            sendMessage(id, itemName, quantity.toInt(), price.toFloat(), bought, this)
            finish()
        }

    }

    private fun sendMessage(
        itemID: Long?,
        itemName: String,
        quantity: Int,
        price: Float,
        bought: Boolean,
        context: Context
    ) {
        val broadIntent = Intent()
        Log.i("AddActivity", "Inside send Message ID: $itemID")
        broadIntent.putExtra("channelID", "ItemAddChannel")
        broadIntent.putExtra("itemID", itemID)
        broadIntent.putExtra("itemName", itemName)
        broadIntent.putExtra("quantity", quantity)
        broadIntent.putExtra("price", price)
        broadIntent.putExtra("bought", bought)
        broadIntent.action = "com.example.smb1.action.AddActivity"
//        broadIntent.component = ComponentName(
//            "com.example.broadcastreceiver",
//            "com.example.broadcastreceiver.ItemNotifier"
//        )
        context.sendBroadcast(broadIntent)
        Log.i("AddActivity", "Message send with action : ${broadIntent.action}")
    }

    private fun validate(text: String, editText: EditText): Boolean {
        return if ("" == text) {
            editText.requestFocus()
            editText.error = "Wrong Value"
            false
        } else true
    }

    override fun onRestart() {
        super.onRestart()
        val i = Intent(this, AddActivity::class.java) //your class
        startActivity(i)
        finish()
    }
}