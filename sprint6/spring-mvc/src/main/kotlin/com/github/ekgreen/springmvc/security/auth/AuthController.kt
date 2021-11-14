package com.github.ekgreen.springmvc.security.auth

import com.github.ekgreen.springmvc.book.model.dto.UserDto
import com.github.ekgreen.springmvc.security.authentication
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.HttpClientErrorException
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@Controller
@RequestMapping("/auth")
class AuthController(private val service: AuthService) {

    @GetMapping("/login")
    fun login(model: Model) : String {
        model.addAttribute("signup", false)
        return "sign"
    }

    @GetMapping("/signup")
    fun signup(model: Model) : String {
        model.addAttribute("signup", true)
        return "sign"
    }

    @PostMapping("/signup")
    fun signup(@Valid dto: UserDto, model: Model, response: HttpServletResponse) : String {
        model.addAttribute("signup", true)

        val token: String = service.signUp(dto)?:
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "not valid login or password")

        response.authentication(token)

        return "redirect:/app/v1/book/main"
    }
}