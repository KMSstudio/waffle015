package com.example.waffle015

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val viewModel: TicTacToeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val adapter = TicTacToeAdapter(viewModel)
        recyclerView.adapter = adapter

        viewModel.board.observe(this) {
            adapter.notifyDataSetChanged()
        }

        viewModel.gameState.observe(this) { gameState ->
            findViewById<TextView>(R.id.game_status_text).text = gameState
        }
    }
}
