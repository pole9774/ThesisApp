package it.polito.thesisapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import it.polito.thesisapp.ui.screens.MainScreen
import it.polito.thesisapp.ui.theme.ThesisAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThesisAppTheme {
                MainScreen()
            }
        }
    }
}

