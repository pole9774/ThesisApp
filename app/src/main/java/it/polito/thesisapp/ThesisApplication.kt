package it.polito.thesisapp

import android.app.Application
import com.google.firebase.FirebaseApp

class ThesisApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}