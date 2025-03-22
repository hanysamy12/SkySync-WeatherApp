package com.example.skysync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.skysync.ui.MainScreen
import com.example.skysync.ui.theme.SkySyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SkySyncTheme {
                MainScreen()
            }

        }
    }
}


