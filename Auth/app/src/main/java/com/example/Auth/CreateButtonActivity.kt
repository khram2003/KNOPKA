package com.example.Auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CreateButtonActivity : AppCompatActivity() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_button)
        supportActionBar?.hide();

        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val createButton = findViewById<Button>(R.id.createButton)
        val editTextLabel = findViewById<EditText>(R.id.editTextLabel)
        val editTextDescr = findViewById<EditText>(R.id.editTextDescription)

        createButton.setOnClickListener {
            val labelString: String = editTextLabel.text.toString()
            val descrString: String = editTextDescr.text.toString()

            val returnIntent = Intent()
            val mapData = mapOf(
                "name" to labelString,
                "style" to "", // TODO
                "descr" to descrString
            )
            val jsonData = Json.encodeToString(mapData)

            returnIntent.putExtra("data", jsonData)

            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }
}