package com.nanda.mystory.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import com.nanda.mystory.data.model.DataModel
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthPreference private constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun getData(): Flow<DataModel> {
        return dataStore.data.map { preferences ->
            DataModel(
                preferences[KEY_NAME] ?: "",
                preferences[KEY_TOKEN] ?: "",
                preferences[KEY_IS_LOGIN] ?: false
            )
        }
    }
    suspend fun saveData(dataModel: DataModel) {
        dataStore.edit { preferences ->
            preferences[KEY_NAME] = dataModel.name
            preferences[KEY_TOKEN] = dataModel.token
            preferences[KEY_IS_LOGIN] = true
        }
    }


    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthPreference? = null

        private val KEY_NAME = stringPreferencesKey("Username")
        private val KEY_TOKEN = stringPreferencesKey("KeyToken")
        private val KEY_IS_LOGIN = booleanPreferencesKey("Login")

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}
