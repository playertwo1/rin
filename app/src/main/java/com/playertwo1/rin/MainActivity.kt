package com.playertwo1.rin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.playertwo1.rin.ui.navigation.RinApp
import com.playertwo1.rin.ui.theme.RinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RinTheme {
                RinApp()
            }
        }
    }
}
