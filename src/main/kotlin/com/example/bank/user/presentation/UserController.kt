package com.example.bank.user.presentation

import com.example.bank.user.application.UserApplication
import com.example.bank.user.application.UserCreateRequest
import com.example.bank.user.application.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired
    val userApplication: UserApplication
) {
    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    suspend fun createUser(@RequestBody accountCreateRequest: UserCreateRequest): UserResponse {
        return userApplication.createUser(accountCreateRequest)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getUser(@PathVariable("id") id: Long): UserResponse {
        return userApplication.getUser(id)
    }
}