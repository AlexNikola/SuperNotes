package com.alexnikola.supernotes.data.prefs

import android.content.SharedPreferences
import javax.inject.Inject

class PrefsManagerImpl @Inject constructor(private val prefs: SharedPreferences) : PrefsManager {

}