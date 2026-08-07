package org.openedx.core.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import org.openedx.core.data.storage.CorePreferences
import org.openedx.core.domain.model.AppLanguage

object LocaleManager {

    fun applySavedLocale(preferences: CorePreferences) {
        val languageCode = preferences.appLanguage
        if (languageCode.isNotEmpty()) {
            applyLocale(languageCode)
        }
    }

    fun applyLocale(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageCode)
        )
    }

    fun getCurrentLanguage(preferences: CorePreferences): AppLanguage {
        val savedLanguage = preferences.appLanguage
        return if (savedLanguage.isEmpty()) {
            val systemLanguage = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
            AppLanguage.fromCode(systemLanguage)
        } else {
            AppLanguage.fromCode(savedLanguage)
        }
    }
}
