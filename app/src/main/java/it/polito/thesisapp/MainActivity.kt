package it.polito.thesisapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import it.polito.thesisapp.ui.screens.MainScreen
import it.polito.thesisapp.ui.theme.ThesisAppTheme

/**
 * MainActivity is the entry point of the application.
 * It sets up the main content view using Jetpack Compose.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThesisAppTheme {
                MainScreen()
            }
        }
    }
}
