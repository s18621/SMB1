package com.example.smb1.Models

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smb1.ProductListActivity
import com.example.smb1.R
import com.example.smb1.databaseRepo
import com.example.smb1.databinding.ItemModelBinding
import com.google.firebase.database.FirebaseDatabase

class ModelItem(private val binding: ItemModelBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: dbModel) {
        binding.apply {
            price.text = model.price.toString()
            itemName.text = model.itemName
            quantity.text = model.quantity.toString()
            bought.isChecked = model.bought
        }
        Log.e("ModelAdapter", "ModelItem bind")
    }
}

class ModelAdapter() : RecyclerView.Adapter<ModelItem>() {

    private var color: Int? = null
    private var font_size: Int? = null
    private lateinit var repo :databaseRepo

    private val handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var allGroceries: MutableList<dbModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelItem {
        val binding = ItemModelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val firebaseDatabase = FirebaseDatabase.getInstance("https://smbgroceries-default-rtdb.europe-west1.firebasedatabase.app")
        repo =  databaseRepo(firebaseDatabase)
        return ModelItem(binding)
    }

    override fun onBindViewHolder(holder: ModelItem, position: Int) {
        holder.bind(allGroceries[position])

        Log.e("ModelAdapter", "Model Adapter values: $allGroceries")

        load(holder.itemView.context)
        loadFont(holder)

        //Remove Item
        holder.itemView.findViewById<Button>(R.id.Delete).setOnClickListener {
            Log.e("ModelAdapter", "Position at start: $position")
            val deletedItem = allGroceries[position].itemName
            Toast.makeText(it.context, "Successfully Deleted $deletedItem", Toast.LENGTH_SHORT)
                .show()
            repo.delete(allGroceries[position])
            Log.e("ModelAdapter", "allG: $allGroceries, size: $itemCount, position: $position")

            allGroceries.removeAt(position)

            Log.e("ModelAdapter", "After Del allG: $allGroceries, size: $itemCount, position: $position")

            this.notifyItemRemoved(position)
            this.notifyItemRangeRemoved(position, 1)
        }

        //Edit Item
        holder.itemView.findViewById<Button>(R.id.Edit).setOnClickListener {

            val dialog = Dialog(it.context)
            dialog.setContentView(R.layout.activity_add)
            dialog.findViewById<ConstraintLayout>(R.id.topPanel).background = color!!.toDrawable()

            val eName = dialog.findViewById<TextView>(R.id.itemName)
            eName.text = allGroceries[position].itemName

            val price = dialog.findViewById<TextView>(R.id.price)
            price.text = allGroceries[position].price.toString()

            val quantity = dialog.findViewById<TextView>(R.id.quantity)
            quantity.text = allGroceries[position].quantity.toString()

            val button = dialog.findViewById<Button>(R.id.Add)
            button.text = "Save"

            button.setOnClickListener {
                val dbModel = dbModel(
                    allGroceries[position].id,
                    price.text.toString().toDouble(),
                    eName.text.toString(),
                    quantity.text.toString().toInt(),
                    allGroceries[position].bought
                )
                val intent = Intent(it.context, ProductListActivity::class.java)
                it.context.startActivity(intent)

                dialog.dismiss()
                repo.update(dbModel)
                this.notifyItemChanged(position)

            }
            dialog.show()

        }

        holder.itemView.findViewById<ConstraintLayout>(R.id.bgColor).background =
            color!!.toDrawable()
        //TODO: Think about this cheesy tactic

        holder.itemView.findViewById<CheckBox>(R.id.bought).setOnClickListener {
            allGroceries[position].bought = holder.itemView.findViewById<CheckBox>(R.id.bought).isChecked
            repo.update(allGroceries[position])
        }
    }

    override fun getItemCount(): Int = allGroceries.size

    private fun loadFont(holder: ModelItem) {
        holder.itemView.findViewById<TextView>(R.id.textView6)
            .setTextSize(TypedValue.COMPLEX_UNIT_DIP, font_size!!.toFloat())
        holder.itemView.findViewById<TextView>(R.id.name)
            .setTextSize(TypedValue.COMPLEX_UNIT_DIP, font_size!!.toFloat())
        holder.itemView.findViewById<TextView>(R.id.itemName)
            .setTextSize(TypedValue.COMPLEX_UNIT_DIP, font_size!!.toFloat())
        holder.itemView.findViewById<TextView>(R.id.price)
            .setTextSize(TypedValue.COMPLEX_UNIT_DIP, font_size!!.toFloat())
        holder.itemView.findViewById<TextView>(R.id.textView8)
            .setTextSize(TypedValue.COMPLEX_UNIT_DIP, font_size!!.toFloat())
        holder.itemView.findViewById<TextView>(R.id.quantity)
            .setTextSize(TypedValue.COMPLEX_UNIT_DIP, font_size!!.toFloat())
        holder.itemView.findViewById<TextView>(R.id.textView10)
            .setTextSize(TypedValue.COMPLEX_UNIT_DIP, font_size!!.toFloat())
    }

    private fun load(context: Context) {
        val preferences = context.getSharedPreferences("dataSave", Context.MODE_PRIVATE)
        color = getColor(preferences.getString("font_color", "RED")!!)
        font_size = preferences.getInt("size_font", 24)
    }

    private fun getColor(str: String): Int {
        return when (str) {
            "BLACK" -> Color.BLACK
            "RED" -> Color.RED
            "BLUE" -> Color.BLUE
            "YELLOW" -> Color.YELLOW
            "GREEN" -> Color.GREEN
            "CYAN" -> Color.CYAN
            "MAGENTA" -> Color.MAGENTA
            "GRAY" -> Color.GRAY
            else -> 0
        }
    }
}