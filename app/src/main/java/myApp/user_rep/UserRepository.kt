package myApp.user_rep

data class User(val id: Int, val name: String)

class UserRepository {
    fun getUsers(): List<User> {
        // Обычно здесь API или база данных
        return listOf(
            User(1, "Alice"),
            User(2, "Bob"),
            User(3, "Charlie")
        )
    }
}
