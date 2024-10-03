package com.example.waffle015

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicTacToeAdapter(
    private val board: Array<Array<String>>? = null,
    private val viewModel: TicTacToeViewModel? = null,
    private val history: TurnHistoryViewModel? = null
) : RecyclerView.Adapter<TicTacToeAdapter.TicTacToeViewHolder>() {

    class TicTacToeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicTacToeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tic_tac_toe_item, parent, false)
        return TicTacToeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicTacToeViewHolder, position: Int) {
        val row = position / 3
        val col = position % 3

        board?.let {
            val value = it[row][col]
            holder.textView.text = value ?: ""
        }

        viewModel?.let { viewModel ->
            val valueFromViewModel = viewModel.board.value?.get(row)?.get(col)
            holder.textView.text = valueFromViewModel ?: ""

            holder.itemView.setOnClickListener {
                viewModel.selectTile(row, col, history)
            }
        }
    }

    override fun getItemCount(): Int {
        return 9
    }
}
