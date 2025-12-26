package myApp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import myApp.Routes
import myApp.UserViewModel

@Composable
fun Pape1(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    val users by userViewModel.users.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Первая страница - separated")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Routes.SECOND) }) {
            Text("Перейти на вторую")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Routes.USER) }) {
            Text("Перейти на user")
        }

        Column(
            modifier = Modifier
//            .background(Color.Red)
                .background(Color.Red)
                .fillMaxSize()
                .padding(16.dp)
            ,
            verticalArrangement = Arrangement.Center
        ) {
            users.map { it ->
                Text(it.name + " " + it.id)
            }



        }
    }
}