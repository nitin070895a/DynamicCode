package com.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamiccode.R
import com.google.android.material.snackbar.Snackbar
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), Compiler.Callbacks {

    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var output: TextView

    private val _compiler = Compiler(this)
    private var _log = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        output = findViewById(R.id.output)

        button.setOnClickListener {

            editText.text?.toString()?.let {

                if (it.isNotEmpty()) {
                    val number = it.toInt()
                    if (number < 10000) execute(number)
                    else showMessage("Number should be less than 10000")
                }
                else showMessage(msg = "Nothing to process")
            }
        }

    }

    override fun log(message: String?) {
        _log.append(getTime())
        _log.append(">>  ")
        _log.append(message)
        updateLog()
    }

    override fun onResult(result: String?) {
        _log.append(getTime())
        _log.append(">>----------------------------------<<\n")
        _log.append(result)
        _log.append(getTime())
        _log.append(">>----------------------------------<<\n")
        updateLog()
    }

    private fun updateLog() {

        runOnUiThread {
            output.text = _log.toString()
        }
    }

    fun getTime(): String? {
        return SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
    }

    @Suppress("SameParameterValue")
    private fun showMessage(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    fun execute(number: Int) {

        _log = StringBuilder("Executing code...\n\n")
        updateLog()

        _compiler.execute(this, number)
    }
}