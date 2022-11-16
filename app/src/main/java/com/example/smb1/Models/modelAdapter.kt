package com.example.smb1.Models

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Looper
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
import com.example.finansemanager.database.Shared
import com.example.smb1.ProductListActivity
import com.example.smb1.R
import com.example.smb1.databinding.ItemModelBinding
import kotlin.concurrent.thread

class ModelItem(private val binding: ItemModelBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: dbModel) {
        binding.apply {
            price.text = model.price.toString()
            itemName.text = model.itemName
            quantity.text = model.quantity.toString()
            bought.isChecked = model.bought
        }
    }
}

class ModelAdapter() : RecyclerView.Adapter<ModelItem>() {

    private var myItems: List<ModelDto> = emptyList()
    private var color: Int? = null
    private var font_size: Int? = null

    private val handler = HandlerCompat.createAsync(Looper.getMainLooper())
    var models: MutableList<dbModel> = mutableListOf<dbModel>()
        set(value) {
            field = value
            handler.post {
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelItem {
        val binding = ItemModelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ModelItem(binding)
    }

    override fun onBindViewHolder(holder: ModelItem, position: Int) {
        holder.bind(models[position])
        getAll()
        load(holder.itemView.context)
        loadFont(holder)

        //Remove Item
        holder.itemView.findViewById<Button>(R.id.Delete).setOnClickListener {
            val deletedItem = myItems[position].itemName
            Toast.makeText(it.context, "Successfully Deleted $deletedItem", Toast.LENGTH_SHORT)
                .show();
            deleteDao(myItems[position])
            myItems.drop(position)
            models.removeAt(position)
            myItems = myItems.dropLast(myItems.size)
            getAll()
            this.notifyItemRemoved(position)
        }

        //Edit Item
        holder.itemView.findViewById<Button>(R.id.Edit).setOnClickListener {

            val dialog: Dialog = Dialog(it.context)
            dialog.setContentView(R.layout.activity_add)
            dialog.findViewById<ConstraintLayout>(R.id.topPanel).background = color!!.toDrawable()

            val eName = dialog.findViewById<TextView>(R.id.itemName)
            eName.text = myItems[position].itemName

            val price = dialog.findViewById<TextView>(R.id.price)
            price.text = myItems[position].price.toString()

            val quantity = dialog.findViewById<TextView>(R.id.quantity)
            quantity.text = myItems[position].quantity.toString()

            val button = dialog.findViewById<Button>(R.id.Add)
            button.text = "Save"

            button.setOnClickListener {
                val newModel = ModelDto(
                    myItems[position].id,
                    price.text.toString().toFloat(),
                    eName.text.toString(),
                    quantity.text.toString().toInt(),
                    myItems[position].bought
                )
                saveDao(newModel)
                dialog.dismiss()
                val intent = Intent(holder.itemView.context, ProductListActivity::class.java)
                holder.itemView.context.startActivity(intent)
            }
            dialog.show()
        }

        holder.itemView.findViewById<ConstraintLayout>(R.id.bgColor).background =
            color!!.toDrawable()
        //TODO: Think about this cheesy tactic
        holder.itemView.findViewById<CheckBox>(R.id.bought).setOnClickListener {
            myItems[position].bought = holder.itemView.findViewById<CheckBox>(R.id.bought).isChecked
            saveDao(myItems)
        }
    }

    override fun getItemCount(): Int = models.size

    private fun saveDao(it: List<ModelDto>) {
        thread {
            Shared.db?.modelBase?.update(it)
        }
    }

    private fun saveDao(it: ModelDto) {
        thread {
            Shared.db?.modelBase?.update(it)
        }
    }

    private fun deleteDao(it: ModelDto) {
        thread {
            Shared.db?.modelBase?.delete(it)
        }
    }

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

    private fun getAll() = thread {
        myItems = Shared.db?.modelBase?.getAll()!!
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