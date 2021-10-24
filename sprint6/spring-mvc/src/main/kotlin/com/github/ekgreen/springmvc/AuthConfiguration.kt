package com.github.ekgreen.springmvc

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ekgreen.springmvc.auth.AuthService
import com.github.ekgreen.springmvc.auth.AuthServlet
import com.github.ekgreen.springmvc.auth.AuthTokenizer
import com.github.ekgreen.springmvc.auth.HmacJwtAuthTokenizer
import com.github.ekgreen.springmvc.da.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*


@Configuration
class AuthConfiguration {

    @Bean("authServlet")
    fun authServletBean(authService: AuthService, objectMapper: ObjectMapper)
            : ServletRegistrationBean<AuthServlet> {
        val bean = ServletRegistrationBean(
            AuthServlet(authService, objectMapper), "/auth/*"
        )
        bean.setLoadOnStartup(1)
        return bean
    }

    @Bean
    fun authService(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder,
        authTokenizer: AuthTokenizer
    ): AuthService{
        return AuthService(userRepository, passwordEncoder, authTokenizer)
    }

    @Bean
    fun userRepository(): UserRepository{
        return UserRepository()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder{
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authTokenizer(@Value("\${auth.secret}") token: String): AuthTokenizer{
        // ps понятно что его в открытом виде не надо и хранить
        return HmacJwtAuthTokenizer(String(Base64.getEncoder().encode(token.toByteArray())))
    }
}