package com.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamiccode.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), Compiler.Callbacks {

    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var output: TextView

    private val _compiler = Compiler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        output = findViewById(R.id.output)

        button.setOnClickListener {

            editText.text?.toString()?.let {

                if (it.isNotEmpty()) execute(it.toInt())
                else showMessage(msg = "Nothing to process")
            }
        }

    }

    @Suppress("SameParameterValue")
    private fun showMessage(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    fun execute(number: Int) {
        output.text = "Executing code..."

        _compiler.execute(this, number)
    }

    override fun onResult(result: String?) {
        runOnUiThread {
            output.text = result ?: "Error"
        }
    }

}