package myApp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import myApp.user_rep.User
import myApp.user_rep.UserRepository

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {


    // StateFlow — поток состояний для Compose
    private val _users = MutableStateFlow<MutableList<User>>(mutableListOf())
    var users: StateFlow<MutableList<User>> = _users

    init {
//        repository.initUsers();
//        loadUsers()
    }

    fun addUsers() {
        repository.addNewUsers();
        _users.value = repository.getUsers()
//        users = _users
        loadUsers()
    }

    private fun loadUsers() {
        _users.value = repository.getUsers()
    }
    // ViewModel logic here
}