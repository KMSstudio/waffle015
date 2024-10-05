package com.example.waffle015

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TurnHistoryViewModel : ViewModel() {
    private val _turnHistory = MutableLiveData<List<TurnState>>(emptyList())
    val turnHistory: LiveData<List<TurnState>> = _turnHistory

    // Add current Turnstate
    fun addTurn(turn: TurnState) {
        val updatedHistory = _turnHistory.value?.toMutableList() ?: mutableListOf()
        updatedHistory.add(turn)
        _turnHistory.value = updatedHistory
    }

    // Revert current turn to index turn
    fun revertToTurn(turnIndex: Int) {
        val updatedHistory = _turnHistory.value?.subList(0, turnIndex + 1)
        _turnHistory.value = updatedHistory
    }

    // Clear History
    fun clearHistory() {
        _turnHistory.value = emptyList()
    }
}
