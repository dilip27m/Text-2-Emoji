package com.example.text_2_emoji


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.example.text_2_emoji.EmojiUtils
import com.example.text_2_emoji.R

class MainActivity : AppCompatActivity() {
    private lateinit var inputText: EditText
    private lateinit var outputText: TextView
    private lateinit var translateButton: Button
    private lateinit var clearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.input_text)
        outputText = findViewById(R.id.output_text)
        translateButton = findViewById(R.id.translate_button)
        clearButton = findViewById(R.id.clear_button)

        // Load emoji map from the GitHub JSON file
        EmojiUtils.loadEmojiMap(this) {
            translateButton.setOnClickListener {
                translateText()
            }

            clearButton.setOnClickListener {
                clearText()
            }
        }
    }

    private fun translateText() {
        val input = inputText.text.toString()
        if (input.isNotEmpty()) {
            val translatedText = EmojiUtils.translateToEmojis(input)
            outputText.text = translatedText
        } else {
            outputText.text = "Input text is empty."
        }
    }

    private fun clearText() {
        inputText.text.clear()
        outputText.text = ""
    }
}
