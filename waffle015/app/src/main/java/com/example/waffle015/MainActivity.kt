package com.example.waffle015

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // TicTacToeViewModel과 TurnHistoryViewModel 초기화
    private val viewModel: TicTacToeViewModel by viewModels()
    private val turnHistoryViewModel: TurnHistoryViewModel by viewModels()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var turnHistoryRecyclerView: RecyclerView
    private lateinit var turnHistoryAdapter: TurnHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reset 버튼 클릭 시 모든 기록과 보드를 초기화
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            viewModel.resetBoard() // 보드 초기화
            turnHistoryViewModel.clearHistory() // 기록 삭제
            updateBoardAndHistory() // UI 갱신
        }

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setup DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Setup TicTacToe board RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val adapter = TicTacToeAdapter(viewModel=viewModel, history=turnHistoryViewModel) // turnHistoryViewModel 전달
        recyclerView.adapter = adapter

        // Observe the board state and update the UI accordingly
        viewModel.board.observe(this) {
            adapter.notifyDataSetChanged()
        }

        // Observe the game state
        viewModel.gameState.observe(this) { gameState ->
            findViewById<TextView>(R.id.game_status_text).text = gameState
        }

        turnHistoryRecyclerView = findViewById(R.id.turn_history_recycler)
        turnHistoryRecyclerView.layoutManager = LinearLayoutManager(this) // 세로 리스트
        turnHistoryAdapter = TurnHistoryAdapter(turnHistoryViewModel) { turnIndex ->
            // restoreGameState
            val turnState = turnHistoryViewModel.turnHistory.value?.get(turnIndex)
            turnState?.let {
                viewModel.restoreGameState(it)
                updateBoardAndHistory()
            }
        }
        turnHistoryRecyclerView.adapter = turnHistoryAdapter


        // Observe turn history from TurnHistoryViewModel and update the adapter
        turnHistoryViewModel.turnHistory.observe(this) { turnHistory ->
            turnHistoryAdapter.updateTurnHistory(turnHistory)
        }
    }

    private fun updateBoardAndHistory() {
        findViewById<RecyclerView>(R.id.recycler_view).adapter?.notifyDataSetChanged()
        findViewById<RecyclerView>(R.id.turn_history_recycler).adapter?.notifyDataSetChanged()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) { return true }
        return super.onOptionsItemSelected(item)
    }
}
