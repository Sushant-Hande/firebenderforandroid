package com.example.firebenderforandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebenderforandroid.model.UserStatsEntity
import com.example.firebenderforandroid.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val _stats = MutableStateFlow<UserStatsEntity?>(null)
    val stats: StateFlow<UserStatsEntity?> = _stats.asStateFlow()

    init {
        viewModelScope.launch {
            UserRepository.getUserStats().collect {
                _stats.value = it
            }
        }
    }
    // TODO: Implement Dashboard logic
}
