package myApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import myApp.pages.Home
import myApp.pages.Pape1
import myApp.pages.UserPage

@Preview(showBackground = true)
@Composable
fun App() {
    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .padding(16.dp)
//            .weight(1f)
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.USER,
            modifier = Modifier
                .fillMaxSize()
//                .weight(1f)
                .background(Color.Green)
                .padding(16.dp)
        ) {
            composable(Routes.FIRST) {
                Pape1(navController)
            }

            composable(Routes.SECOND) {
                Home(navController)
            }

            composable(Routes.USER) {
                UserPage(navController)
            }
        }
    }


}