package kr.co.pincoin.api.app.auth.controller

import kr.co.pincoin.api.global.response.success.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
}