package com.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.dynamiccode.R
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var output: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        output = findViewById(R.id.output)

        button.setOnClickListener {

            editText.text?.toString()?.let {

                if (it.isNotEmpty()) runCode(it)
                else showMessage(msg = "Nothing to execute")
            }
        }

    }

    private fun showMessage(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show()
    }

    fun runCode(code: String) {
        output.text = ""
        output.text = exectue(code)
    }

    fun exectue(code: String): String {
        Executors.newSingleThreadExecutor().execute {
            Compiler().execute(
                this,
                "https://github.com/nitin070895a/DynamicCode/raw/master/app/src/main/java/com/example/MyJar.jar"
            )
        }
        return "Error: Not Implemented"
    }
}