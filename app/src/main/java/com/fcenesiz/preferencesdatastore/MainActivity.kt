package com.fcenesiz.preferencesdatastore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fcenesiz.preferencesdatastore.ui.theme.PreferencesDataStoreTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreferencesDataStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val inputKey = remember {
                        mutableStateOf("")
                    }
                    val inputData = remember {
                        mutableStateOf("")
                    }
                    val inputKeyFrom = remember {
                        mutableStateOf("")
                    }
                    var outputData = remember {
                        mutableStateOf("")
                    }
                    val scope = rememberCoroutineScope()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            OutlinedTextField(
                                value = inputKey.value,
                                onValueChange = {
                                    inputKey.value = it
                                },
                                placeholder = { Text(text = "key") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = inputData.value,
                                onValueChange = {
                                    inputData.value = it
                                },
                                placeholder = { Text(text = "data") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(onClick = {
                                scope.launch {
                                    save(inputKey.value, inputData.value)
                                }
                            }) {
                                Text(text = "Save")
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            OutlinedTextField(
                                value = inputKeyFrom.value,
                                onValueChange = {
                                    inputKeyFrom.value = it
                                },
                                placeholder = { Text(text = "key") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(onClick = {
                                scope.launch {
                                    outputData.value = read(inputKeyFrom.value) ?: "No data found."
                                }
                            }) {
                                Text(text = "Read")
                            }
                            Text(
                                text = outputData.value
                            )
                        }
                    }
                }
            }
        }

    }

    private suspend fun save(key: String, data: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = data
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

}