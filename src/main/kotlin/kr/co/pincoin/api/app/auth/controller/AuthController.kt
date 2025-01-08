package kr.co.pincoin.api.app.auth.controller

import kr.co.pincoin.api.global.response.success.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
    @GetMapping("/hello")
    fun helloWorld(): ResponseEntity<ApiResponse<String>> {
        return ResponseEntity.ok(
            ApiResponse.of(
                data = "Hello, World!",
                message = "Welcome to the API"
            )
        )
    }

    @PostMapping("/sign-in")
    fun signIn(): ResponseEntity<ApiResponse<String>> {
        return ResponseEntity.ok(ApiResponse.of(data = "Sign In!"))
    }

    @PostMapping("/refresh")
    fun refresh(): ResponseEntity<ApiResponse<String>> {
        return ResponseEntity.ok(ApiResponse.of(data = "Refresh!"))
    }

    @PostMapping("/sign-out")
    fun signOut(): ResponseEntity<ApiResponse<String>> {
        return ResponseEntity.ok(ApiResponse.of(data = "Sign Out!"))
    }
}