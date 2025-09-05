package net.inferno.teethbrush.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {
    val durationFlow = settingsDataStore.durationFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(200L),
            null,
        )

    fun setDuration(duration: Int) {
        viewModelScope.launch {
            settingsDataStore.setDuration(duration)
        }
    }
}