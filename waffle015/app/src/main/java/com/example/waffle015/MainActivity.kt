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
import com.example.waffle015.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // TicTacToeViewModel과 TurnHistoryViewModel 초기화
    private val viewModel: TicTacToeViewModel by viewModels()
    private val turnHistoryViewModel: TurnHistoryViewModel by viewModels()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var turnHistoryRecyclerView: RecyclerView
    private lateinit var turnHistoryAdapter: TurnHistoryAdapter

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reset 버튼 클릭 시 모든 기록과 보드를 초기화
        binding.resetButton.setOnClickListener {
            viewModel.resetBoard() // 보드 초기화
            turnHistoryViewModel.clearHistory() // 기록 삭제
            updateBoardAndHistory() // UI 갱신
        }

        // Setup Toolbar
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Setup DrawerLayout and ActionBarDrawerToggle
        drawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Setup TicTacToe board RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val adapter = TicTacToeAdapter(viewModel=viewModel, history=turnHistoryViewModel) // turnHistoryViewModel 전달
        recyclerView.adapter = adapter

        // Observe the board state and update the UI accordingly
        viewModel.board.observe(this) {
            adapter.notifyDataSetChanged()
        }

        // Observe the game state
        viewModel.gameState.observe(this) { gameState ->
            binding.gameStatusText.text = gameState
        }

        turnHistoryRecyclerView = binding.turnHistoryRecycler
        turnHistoryRecyclerView.layoutManager = LinearLayoutManager(this) // 세로 리스트
        turnHistoryAdapter = TurnHistoryAdapter(turnHistoryViewModel) { turnIndex ->
            // restoreGameState
            turnHistoryViewModel.revertToTurn(turnIndex)
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
        binding.recyclerView.adapter?.notifyDataSetChanged()
        binding.turnHistoryRecycler.adapter?.notifyDataSetChanged()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) { return true }
        return super.onOptionsItemSelected(item)
    }
}
