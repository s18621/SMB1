package com.example.smb1

import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smb1.databinding.SettingsActivityBinding


class OptionsActivity : AppCompatActivity() {
    private val binding by lazy { SettingsActivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        load()

        val seekbar = binding.seekBar
        seekbar.progress = binding.getSize.text.toString().toInt()
        setSeekBar(seekbar, binding.getSize)

        binding.saveButton.setOnClickListener {
            save()
            finish()
        }


    }

    private fun setSeekBar(bar: SeekBar, textView: TextView) {
        bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var progressChangedValue = 0
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
                textView.text = progressChangedValue.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                textView.text = progressChangedValue.toString()
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                textView.text = progressChangedValue.toString()
            }
        })
    }

    private fun load() {
        val preferences = getSharedPreferences("dataSave", Context.MODE_PRIVATE)
        val color = getColor(preferences.getString("font_color", "RED")!!)
        binding.getSize.text = preferences.getInt("size_font", 24).toString()
        binding.getColor.setSelection(color)
    }


    private fun getColor(str: String): Int {
        return when (str) {
            "BLACK" -> 0
            "RED" -> 1
            "BLUE" -> 2
            "YELLOW" -> 3
            "WHITE" -> 4
            "GREEN" -> 5
            "CYAN" -> 6
            "MAGENTA" -> 7
            else -> 0
        }
    }

    private fun save() {
        val preferences = getSharedPreferences("dataSave", Context.MODE_PRIVATE)
        val edit = preferences.edit()
        edit.putString("font_color", binding.getColor.selectedItem.toString())
        edit.putInt("size_font", binding.getSize.text.toString().toInt())
        edit.apply()
    }

}