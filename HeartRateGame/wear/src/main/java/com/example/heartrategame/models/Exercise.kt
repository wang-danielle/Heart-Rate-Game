package com.example.heartrategame.models

// TODO: consider moving to common module
enum class Exercise(val title: String, val imageResource: Int) {
    RUNNING("Running", com.example.heartrategame.R.drawable.running),
    JUMPING("Jumping", com.example.heartrategame.R.drawable.jumping),
}