package com.example.skysync.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import com.example.skysync.repo.DataStoreRepository
import kotlinx.coroutines.flow.first
import java.util.Locale

class LocalHelper(private val dataStoreRepo: DataStoreRepository) {

    suspend fun applySavedLanguage(context: Context): Context {
        val language = dataStoreRepo.getLanguage().first()
        return updateResources(context, language)
    }

    suspend fun changeLanguage(context: Context, newLanguage: String) {
        dataStoreRepo.setLanguage(newLanguage)
        updateResources(context, newLanguage)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val config = Configuration(context.resources.configuration).apply {
                setLocale(locale)
                setLayoutDirection(locale)
            }
            context.createConfigurationContext(config)
        } else {
            val config = Configuration(context.resources.configuration).apply {
                this.locale = locale
                setLayoutDirection(locale)
            }
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    fun restartActivity(activity: Activity) {

        activity.startActivity(Intent(activity, activity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
                activity.finish()

    }
}