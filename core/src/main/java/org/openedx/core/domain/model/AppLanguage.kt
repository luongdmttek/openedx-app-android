package org.openedx.core.domain.model

import org.openedx.core.R

enum class AppLanguage(
    val code: String,
    val titleResId: Int,
) {
    ENGLISH(
        code = "en",
        titleResId = R.string.core_language_english,
    ),
    VIETNAMESE(
        code = "vi",
        titleResId = R.string.core_language_vietnamese,
    );

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}
