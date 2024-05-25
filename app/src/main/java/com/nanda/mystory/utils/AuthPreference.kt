package com.nanda.mystory.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nanda.mystory.data.model.DataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class AuthPreference private constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun getData(): Flow<DataModel> {
        return dataStore.data.map { preferences ->
            DataModel(
                preferences[NAME] ?: "",
                preferences[TOKEN] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    suspend fun saveData(dataModel: DataModel) {
        dataStore.edit { preferences ->
            preferences[NAME] = dataModel.name
            preferences[TOKEN] = dataModel.token
            preferences[IS_LOGIN] = true
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

        private val NAME = stringPreferencesKey("Username")
        private val TOKEN = stringPreferencesKey("KeyToken")
        private val IS_LOGIN = booleanPreferencesKey("Login")

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}



