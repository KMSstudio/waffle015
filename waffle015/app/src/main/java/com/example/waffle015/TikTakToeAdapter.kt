package com.example.waffle015

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicTacToeAdapter(
    private val board: Array<Array<String>>? = null,  // 보드 데이터를 받을 때는 board를 사용
    private val viewModel: TicTacToeViewModel? = null, // ViewModel을 받을 때는 viewModel을 사용
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

        // 보드 상태가 전달된 경우 (turn history 등에서 사용)
        board?.let {
            val value = it[row][col]
            holder.textView.text = value ?: ""
        }

        // ViewModel이 전달된 경우 (실제 게임 보드에서 사용)
        viewModel?.let { viewModel ->
            val valueFromViewModel = viewModel.board.value?.get(row)?.get(col)
            holder.textView.text = valueFromViewModel ?: ""

            // ViewModel을 통해 tile을 선택하도록 설정
            holder.itemView.setOnClickListener {
                viewModel.selectTile(row, col, history)
            }
        }
    }

    override fun getItemCount(): Int {
        return 9 // 3x3 보드이므로 9칸
    }
}
