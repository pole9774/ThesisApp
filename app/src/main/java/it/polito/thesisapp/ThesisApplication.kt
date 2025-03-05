package it.polito.thesisapp

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class for the ThesisApp.
 * This class is used to initialize Firebase when the application starts.
 */
@HiltAndroidApp
class ThesisApplication : Application() {
    /**
     * Called when the application is starting, before any other application objects have been created.
     * This is where Firebase is initialized.
     */
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}