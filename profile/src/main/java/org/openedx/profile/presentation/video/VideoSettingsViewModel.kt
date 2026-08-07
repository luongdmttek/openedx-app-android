package org.openedx.profile.presentation.video

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.openedx.core.data.storage.CorePreferences
import org.openedx.core.domain.model.AppLanguage
import org.openedx.core.domain.model.VideoSettings
import org.openedx.core.utils.LocaleManager
import org.openedx.core.presentation.settings.video.VideoQualityType
import org.openedx.core.system.notifier.VideoNotifier
import org.openedx.core.system.notifier.VideoQualityChanged
import org.openedx.foundation.presentation.BaseViewModel
import org.openedx.profile.presentation.ProfileAnalytics
import org.openedx.profile.presentation.ProfileAnalyticsEvent
import org.openedx.profile.presentation.ProfileAnalyticsKey
import org.openedx.profile.presentation.ProfileRouter

class VideoSettingsViewModel(
    private val preferencesManager: CorePreferences,
    private val notifier: VideoNotifier,
    private val analytics: ProfileAnalytics,
    private val router: ProfileRouter,
) : BaseViewModel() {

    private val _videoSettings = MutableLiveData<VideoSettings>()
    val videoSettings: LiveData<VideoSettings>
        get() = _videoSettings

    private val _appLanguage = MutableLiveData<AppLanguage>()
    val appLanguage: LiveData<AppLanguage>
        get() = _appLanguage

    val currentSettings: VideoSettings
        get() = preferencesManager.videoSettings

    val currentLanguage: AppLanguage
        get() = LocaleManager.getCurrentLanguage(preferencesManager)

    init {
        _videoSettings.value = preferencesManager.videoSettings
        _appLanguage.value = currentLanguage
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        viewModelScope.launch {
            notifier.notifier.collectLatest {
                if (it is VideoQualityChanged) {
                    _videoSettings.value = preferencesManager.videoSettings
                }
            }
        }
    }

    fun setWifiDownloadOnly(value: Boolean) {
        val currentSettings = preferencesManager.videoSettings
        preferencesManager.videoSettings = currentSettings.copy(wifiDownloadOnly = value)
        _videoSettings.value = preferencesManager.videoSettings
        logProfileEvent(
            ProfileAnalyticsEvent.WIFI_TOGGLE,
            buildMap {
                put(ProfileAnalyticsKey.ACTION.key, value)
            }
        )
    }

    fun navigateToVideoStreamingQuality(fragmentManager: FragmentManager) {
        router.navigateToVideoQuality(
            fragmentManager,
            VideoQualityType.Streaming
        )
    }

    fun navigateToVideoDownloadQuality(fragmentManager: FragmentManager) {
        router.navigateToVideoQuality(
            fragmentManager,
            VideoQualityType.Download
        )
    }

    fun navigateToLanguageSelector(fragmentManager: FragmentManager) {
        router.navigateToLanguageSelector(fragmentManager)
    }

    private fun logProfileEvent(
        event: ProfileAnalyticsEvent,
        params: Map<String, Any?> = emptyMap(),
    ) {
        analytics.logEvent(
            event = event.eventName,
            params = buildMap {
                put(ProfileAnalyticsKey.NAME.key, event.biValue)
                put(ProfileAnalyticsKey.CATEGORY.key, ProfileAnalyticsKey.PROFILE.key)
                putAll(params)
            }
        )
    }
}
