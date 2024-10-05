package com.example.waffle015

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TicTacToeViewModel : ViewModel() {
    private val _board = MutableLiveData(Array(5) { Array(5) { "" } })
    val board: LiveData<Array<Array<String>>> = _board

    private val _currentPlayer = MutableLiveData("O")
    val currentPlayer: LiveData<String> = _currentPlayer

    private val _gameState = MutableLiveData("In progress")
    val gameState: LiveData<String> = _gameState

    private var winner: String? = null

    // Select Tile and update turnHistory
    fun selectTile(row: Int, col: Int, turnHistoryViewModel: TurnHistoryViewModel? = null) {
        if (_board.value?.get(row)?.get(col).isNullOrEmpty() && winner == null) {
            _board.value?.get(row)?.set(col, _currentPlayer.value ?: "O")
            _board.postValue(_board.value)

            // What the fuck is going on (Written by GPT)
            turnHistoryViewModel?.let { viewModel ->
                val currentTurn = TurnState(
                    board = _board.value!!.map { it.copyOf() }.toTypedArray(),
                    currentPlayer = _currentPlayer.value ?: "O",
                    description = "${_currentPlayer.value}'s Turn"
                )
                viewModel.addTurn(currentTurn)
            }

            if (checkWin()) {
                _gameState.value = "${_currentPlayer.value} win"
                winner = _currentPlayer.value
            } else if (checkDraw()) {
                _gameState.value = "Draw"
            } else {
                _currentPlayer.value = if (_currentPlayer.value == "O") "X" else "O"
            }
        }
    }

    // reset the game
    fun resetBoard() {
        _board.value = Array(5) { Array(5) { "" } } // 빈 보드로 초기화
        _currentPlayer.value = "O"
        _gameState.value = "In progress"
        winner = null // 승자 초기화
    }

    private fun addTurnToHistory(description: String = "Player ${_currentPlayer.value}'s turn") {
        val currentBoard = _board.value?.map { it.copyOf() }?.toTypedArray() ?: return
        val newTurn = TurnState(currentBoard, _currentPlayer.value ?: "O", description)
    }

    fun restoreGameState(turnState: TurnState) {
        _board.value = turnState.board
        _currentPlayer.value = turnState.currentPlayer
        _currentPlayer.value = if (_currentPlayer.value == "O") "X" else "O"
        _gameState.value = "In progress"
        _board.postValue(_board.value)
        winner = null
    }

    // Check win
    private fun checkWin(): Boolean {
        val b = _board.value ?: return false
        for (i in 0..2) {
            if (b[i][0] == b[i][1] && b[i][1] == b[i][2] && b[i][2] == b[i][3] && b[i][3] == b[i][4] && b[i][0].isNotEmpty()) return true
            if (b[0][i] == b[1][i] && b[1][i] == b[2][i] && b[2][i] == b[3][i] && b[3][i] == b[4][i] && b[0][i].isNotEmpty()) return true
        }
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2] && b[2][2] == b[3][3] && b[3][3] == b[4][4] && b[0][0].isNotEmpty()) return true
        if (b[0][4] == b[1][3] && b[1][3] == b[2][2] && b[2][2] == b[3][1] && b[3][1] == b[4][0] && b[0][4].isNotEmpty()) return true
        return false
    }

    // Check draw
    private fun checkDraw(): Boolean {
        val b = _board.value ?: return false
        return b.all { row -> row.all { it.isNotEmpty() } }
    }
}
