package myApp.user_rep

import android.util.Log

data class User(val id: Int, val name: String)

class UserRepository {

    var userList: MutableList<User> = mutableListOf<User>();


    fun addNewUsers() {
        userList.add(User(1, "Alice"))
        userList.add(User(1, "Bob"))
        userList.add(User(1, "Bob"))
        userList.add(User(1, "Bob"))
        userList.add(User(2, "Charlie"))
        Log.d("!!!", "initUsers: " + userList.toString())
    }

    fun getUsers(): MutableList<User> {
        return userList.toMutableList();

        /*
                // Обычно здесь API или база данных
                return listOf(
                    User(1, "Alice"),
                    User(2, "Bob"),
                    User(3, "Charlie")
                )
        */
    }
}
