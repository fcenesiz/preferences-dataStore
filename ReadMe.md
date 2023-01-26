``SharedPreferences`` is deprecated!

Use ``DataStore<Preferences>`` instead of ``SharedPreferences`` now.

### Create a Preferences DataStore

````kotlin
// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
````

### Write to a Preferences DataStore

````kotlin
private suspend fun save(key: String, data: String) {
    val dataStoreKey = stringPreferencesKey(key)
    dataStore.edit { settings ->
        settings[dataStoreKey] = data
    }
}
````

### Read from a Preferences DataStore

````kotlin
private suspend fun read(key: String): String? {
    val dataStoreKey = stringPreferencesKey(key)
    val preferences = dataStore.data.first()
    return preferences[dataStoreKey]
}
````
<img src="preferences-datastore.gif" width=40%>
