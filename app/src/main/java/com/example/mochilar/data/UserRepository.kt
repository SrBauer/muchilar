package com.example.mochilar.data

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getUser(email: String, password: String) = userDao.getUser(email, password)
}