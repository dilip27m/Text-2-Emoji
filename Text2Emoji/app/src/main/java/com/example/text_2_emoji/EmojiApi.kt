package com.example.text_2_emoji


import retrofit2.http.GET

interface EmojiApi {
    @GET("https://dilip27m.github.io/api/emoji_map.json")
    fun getEmojis(): retrofit2.Call<Map<String, String>>
}
