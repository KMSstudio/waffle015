package com.example.waffle015

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TurnHistoryAdapter(
    private val viewModel: TurnHistoryViewModel,
    private val onRevertClick: (Int) -> Unit
) : RecyclerView.Adapter<TurnHistoryAdapter.TurnViewHolder>() {

    private var turnHistory: List<TurnState> = listOf()

    class TurnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val turnText: TextView = itemView.findViewById(R.id.turn_text)
        val boardRecyclerView: RecyclerView = itemView.findViewById(R.id.board_state_recycler)
        val revertButton: Button = itemView.findViewById(R.id.revert_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurnViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.turn_history_item, parent, false)
        return TurnViewHolder(view)
    }

    override fun onBindViewHolder(holder: TurnViewHolder, position: Int) {
        val turn = turnHistory[position]
        holder.turnText.text = turn.description

        // 보드 상태를 RecyclerView에 설정
        holder.boardRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 3)
        val boardAdapter = TicTacToeAdapter(turn.board)
        holder.boardRecyclerView.adapter = boardAdapter

        holder.revertButton.setOnClickListener {
            onRevertClick(position)
        }
    }

    override fun getItemCount(): Int {
        return turnHistory.size
    }

    // Update history by newTurnHistory
    fun updateTurnHistory(newTurnHistory: List<TurnState>) {
        turnHistory = newTurnHistory
        notifyDataSetChanged()
    }
}
