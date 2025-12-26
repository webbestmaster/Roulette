package com.statlex.roulette

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.statlex.roulette.ui.theme.RouletteTheme
import androidx.compose.ui.res.stringResource
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import myApp.App

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RouletteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
//                            .background(Color.Yellow)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {

                        App()

/*
                        Greeting(
                            name = "Android",
                            modifier = Modifier.background(Color.White)
                        )
                        Greeting(
                            name = "Android",
                            modifier = Modifier.background(Color.White)
                        )
                        Greeting(
                            name = "Android",
                            modifier = Modifier.background(Color.White)
                        )
                        Greeting(
                            name = "Android",
                            modifier = Modifier.background(Color.White)
                        )
*/
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val locales = LocaleListCompat.forLanguageTags("sv-SE")
    AppCompatDelegate.setApplicationLocales(locales)

    val context = LocalContext.current
    val locale1 = context.resources.configuration.locales
    Log.d("111", locale1.toLanguageTags());

    val appLocales = AppCompatDelegate.getApplicationLocales()
    Log.d("111", "appLocales: " + appLocales.toLanguageTags());

    Text(
        text = "Hello " + stringResource(R.string.app_name) + " - " + stringResource(R.string.hi),
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 40.sp
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RouletteTheme {
        Greeting("Android")
    }
}

