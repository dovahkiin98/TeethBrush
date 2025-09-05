package net.inferno.teethbrush.ui.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val dataStore = context.dataStore

    val durationFlow: Flow<Int> = dataStore.data.map {
        it[DURATION_KEY] ?: (2 * 60)
    }

    suspend fun setDuration(duration: Int) = dataStore.edit {
        it[DURATION_KEY] = duration
    }

    companion object {
        private val DURATION_KEY = intPreferencesKey("duration")
    }
}