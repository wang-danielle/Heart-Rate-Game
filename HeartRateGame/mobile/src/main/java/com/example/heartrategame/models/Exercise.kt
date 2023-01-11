package com.example.heartrategame.models

enum class Exercise(val title: String, val imageResource: Int) {
    RUNNING("Running", com.example.heartrategame.R.drawable.running),
    JUMPING("Jumping", com.example.heartrategame.R.drawable.jumping),
    WALKING("Walking", com.example.heartrategame.R.drawable.walking),
    DANCING("Dancing", com.example.heartrategame.R.drawable.dancing),
    CYCLING("Cycling", com.example.heartrategame.R.drawable.cycling),
    SWIMMING("Swimming", com.example.heartrategame.R.drawable.swimming),
}