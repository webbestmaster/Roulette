package myApp.pages

//import UserViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import myApp.Routes
import myApp.user_rep.User
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import myApp.UserViewModel
import myApp.user_rep.UserRepository
import com.statlex.roulette.R

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
/*

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    // StateFlow — поток состояний для Compose
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        loadUsers()
    }

    private fun loadUsers() {
        _users.value = repository.getUsers()
    }
}
*/

@Composable
fun UserPage(navController: NavHostController) {
//    val users by userViewModel.users.collectAsState()

    val userViewModel: UserViewModel = viewModel()
    val users by userViewModel.users.collectAsState()

    Log.d("!!!", users.toString())

    val items = List(30) { "Элемент №$it" }

    Column(
        modifier = Modifier
//            .background(Color.Red)
            .background(Color.Red)
            .fillMaxSize()
            .padding(16.dp)
        ,
        verticalArrangement = Arrangement.Center
    ) {
        Text("User")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Routes.SECOND) }) {
            Text("Перейти на вторую")
        }


        Image(
            painter = painterResource(id = R.drawable.www),
            contentDescription = "Логотип",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop,
        )

        LazyColumn(
            modifier = Modifier.weight(0.3f) // 30%
                .fillMaxHeight(),
//            userScrollEnabled = false,

//            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            items(items) { item ->
//                Text(
//                    text = item,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = Color.White
//                )
//            }

//            users.map { it ->
//            }
            items(users) { user ->
                UserCard(user)
            }
        }
    }
}

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = user.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}