package org.openedx.core.presentation.settings.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.openedx.core.data.storage.CorePreferences
import org.openedx.core.domain.model.AppLanguage
import org.openedx.core.utils.LocaleManager
import org.openedx.foundation.presentation.BaseViewModel

class LanguageSelectViewModel(
    private val preferencesManager: CorePreferences,
) : BaseViewModel() {

    private val _selectedLanguage = MutableLiveData<AppLanguage>()
    val selectedLanguage: LiveData<AppLanguage>
        get() = _selectedLanguage

    init {
        _selectedLanguage.value = LocaleManager.getCurrentLanguage(preferencesManager)
    }

    fun getCurrentLanguage(): AppLanguage {
        return LocaleManager.getCurrentLanguage(preferencesManager)
    }

    fun setLanguage(language: AppLanguage): Boolean {
        val currentLanguage = getCurrentLanguage()
        if (currentLanguage == language && preferencesManager.appLanguage.isNotEmpty()) {
            return false
        }
        preferencesManager.appLanguage = language.code
        LocaleManager.applyLocale(language.code)
        _selectedLanguage.value = language
        return true
    }
}
