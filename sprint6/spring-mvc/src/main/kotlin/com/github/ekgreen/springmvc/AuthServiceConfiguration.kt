package com.github.ekgreen.springmvc

import com.github.ekgreen.springmvc.auth.AuthService
import com.github.ekgreen.springmvc.auth.AuthServlet
import com.github.ekgreen.springmvc.auth.AuthTokenizer
import com.github.ekgreen.springmvc.auth.HmacJwtAuthTokenizer
import com.github.ekgreen.springmvc.da.UserRepository
import com.github.ekgreen.springmvc.model.converter.UserTransformation
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.thymeleaf.TemplateEngine
import java.util.*


@Configuration
class AuthServiceConfiguration {

    @Bean("authServlet")
    fun authServletBean(authService: AuthService, engine: TemplateEngine)
            : ServletRegistrationBean<AuthServlet> {
        val bean = ServletRegistrationBean(
            AuthServlet(authService, engine), "/auth/*"
        )
        bean.setLoadOnStartup(1)
        return bean
    }

    @Bean
    fun authService(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder,
        userMapper: UserTransformation,
        authTokenizer: AuthTokenizer
    ): AuthService{
        return AuthService(userRepository, passwordEncoder, userMapper, authTokenizer)
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