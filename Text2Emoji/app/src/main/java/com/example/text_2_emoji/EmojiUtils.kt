package com.example.text_2_emoji





import android.content.Context
import android.widget.Toast
import com.example.text_2_emoji.EmojiApi
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object EmojiUtils {
    private var emojiMap: Map<String, String>? = null

    fun loadEmojiMap(context: Context, onLoaded: () -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dilip27m.github.io/api/emoji_map.json/") // Adjust base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(EmojiApi::class.java)
        service.getEmojis().enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    emojiMap = response.body()
                    onLoaded()
                } else {
                    Toast.makeText(context, "Failed to load emoji map", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(context, "Failed to load emoji map: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun translateToEmojis(input: String): String {
        var output = input

        // Handle phrases first
        emojiMap?.keys?.sortedByDescending { it.length }?.forEach { key ->
            if (output.contains(key, ignoreCase = true)) {
                emojiMap?.get(key)?.let { emoji ->
                    output = output.replace(key.toRegex(RegexOption.IGNORE_CASE), emoji)
                }
            }
        }

        // Handle individual words
        val words = output.split("\\s+".toRegex()).toMutableList()
        for (i in words.indices) {
            val word = words[i]
            emojiMap?.get(word.lowercase())?.let { emoji ->
                words[i] = emoji
            }
        }

        return words.joinToString(" ")
    }
}
