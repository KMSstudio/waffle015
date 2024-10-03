package com.example.waffle015

data class TurnState(
    val board: Array<Array<String>>,
    val currentPlayer: String,
    val description: String
)