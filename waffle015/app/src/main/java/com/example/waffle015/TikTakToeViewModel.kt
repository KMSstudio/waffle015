package com.example.waffle015

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TicTacToeViewModel : ViewModel() {

    // 게임 판을 저장할 배열
    private val _board = MutableLiveData(Array(3) { Array(3) { "" } })
    val board: LiveData<Array<Array<String>>> = _board

    private val _currentPlayer = MutableLiveData("O")
    val currentPlayer: LiveData<String> = _currentPlayer

    private val _gameState = MutableLiveData("진행 중")
    val gameState: LiveData<String> = _gameState

    private var winner: String? = null

    // Select Tile
    fun selectTile(row: Int, col: Int) {
        if (_board.value?.get(row)?.get(col).isNullOrEmpty() && winner == null) {
            _board.value?.get(row)?.set(col, _currentPlayer.value ?: "O")
            _board.postValue(_board.value)
            if (checkWin()) {
                _gameState.value = "${_currentPlayer.value} 승리!"
                winner = _currentPlayer.value
            } else if (checkDraw()) {
                _gameState.value = "무승부"
            } else {
                _currentPlayer.value = if (_currentPlayer.value == "O") "X" else "O"
            }
        }
    }

    // 게임 초기화
    fun resetGame() {
        _board.value = Array(3) { Array(3) { "" } }
        _currentPlayer.value = "O"
        _gameState.value = "진행 중"
        winner = null
    }

    // 승리 체크
    private fun checkWin(): Boolean {
        val b = _board.value ?: return false
        // Range to (0..2)
        for (i in 0..2) {
            if (b[i][0] == b[i][1] && b[i][1] == b[i][2] && b[i][0].isNotEmpty()) return true
            if (b[0][i] == b[1][i] && b[1][i] == b[2][i] && b[0][i].isNotEmpty()) return true
        }
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2] && b[0][0].isNotEmpty()) return true
        if (b[0][2] == b[1][1] && b[1][1] == b[2][0] && b[0][2].isNotEmpty()) return true
        return false
    }

    // 무승부 체크
    private fun checkDraw(): Boolean {
        val b = _board.value ?: return false
        return b.all { row -> row.all { it.isNotEmpty() } }
    }
}
