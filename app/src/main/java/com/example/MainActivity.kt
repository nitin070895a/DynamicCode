package com.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamiccode.R

/**
 * The Main Activity with demonstration of the project
 */
class MainActivity : AppCompatActivity(), Compiler.Callbacks, View.OnClickListener {

    // UI
    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var output: TextView

    private val _compiler = Compiler(this)      // Code compiler
    private val _logger = Logger()                       // Event Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        output = findViewById(R.id.output)

        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.button) {

            // Grab the number and process it
            editText.text?.toString()?.let {

                if (it.isNotEmpty()) {
                    val number = it.toInt()
                    if (number < 10000) execute(number)
                    else showSnackBar(this, "Number should be less than 10000")
                }
                else showSnackBar(this, "Nothing to process")
            }
        }
    }

    override fun log(msg: String?) {
        _logger.log(msg)
        updateLog()
    }

    override fun onResult(result: String?) {
        _logger.logBoxed(result)
        updateLog()

        runOnUiThread {
            button.isEnabled = true
        }
    }

    /**
     * Updates the [output] text with latest logs from [_logger]
     */
    private fun updateLog() {
        runOnUiThread {
            output.text = _logger.getLog()
        }
    }

    /**
     * Executes the reflection code with [number] as parameter
     */
    private fun execute(number: Int) {

        _logger.clear()
        _logger.log("Executing code...")
        updateLog()
        button.isEnabled = false

        // THE EXECUTION
        _compiler.execute(this, number)
    }

}