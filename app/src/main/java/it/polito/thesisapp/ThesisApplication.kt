package it.polito.thesisapp

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class for the ThesisApp.
 * This class initializes Firebase and sets up Hilt for dependency injection.
 */
@HiltAndroidApp
class ThesisApplication : Application() {
    /**
     * Called when the application is starting, before any other application objects have been created.
     * Initializes Firebase for the application.
     */
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}