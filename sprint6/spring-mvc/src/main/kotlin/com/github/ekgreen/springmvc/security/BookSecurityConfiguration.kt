package com.github.ekgreen.springmvc.security

import com.github.ekgreen.springmvc.book.da.UserRepository
import com.github.ekgreen.springmvc.book.model.converter.UserTransformation
import com.github.ekgreen.springmvc.security.auth.AuthService
import com.github.ekgreen.springmvc.security.auth.AuthTokenizer
import com.github.ekgreen.springmvc.security.auth.HmacJwtAuthTokenizer
import com.github.ekgreen.springmvc.security.handler.JwtSecurityContextRepository
import com.github.ekgreen.springmvc.security.handler.JwtUsernamePasswordSuccessHandler
import com.github.ekgreen.springmvc.security.service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.access.vote.RoleHierarchyVoter
import org.springframework.security.access.vote.RoleVoter
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import java.util.*
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
class BookSecurityConfiguration(
    @Value("\${auth.secret}") val token: String,
    val userRepository: UserRepository
) : WebSecurityConfigurerAdapter() {

    companion object{
        const val AUTHORIZATION = "Authorization"
    }

    override fun configure(http: HttpSecurity) {
        // @formatter:off
        http
            .authenticationProvider(daoAuthenticationProvider())

            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // так как у нас JWT уберем сессию

            .and()

            .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/image/**").permitAll()
                .regexMatchers("/ap(p|i)/v[0-9]+/book/(add|delete)(/|[a-zA-Z0-9-]+)*").hasRole("ADMIN")
                .antMatchers("/api/**").hasRole("API_OWNER")
                .antMatchers("/app/**").hasRole("APP_OWNER")
                .anyRequest().authenticated()

            .and()

            .formLogin()
                .loginPage("/auth/login")
                .usernameParameter("nickname")
                .successHandler(JwtUsernamePasswordSuccessHandler("/app/v1/book/main", authTokenizer())) // так как мы балуемся с JWT без OAuth2

            .and()

            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository())

            .and()

            .securityContext()
                .securityContextRepository(JwtSecurityContextRepository(authTokenizer())) // так как сессию мы отключили, а между запросами разлогиниваться не круто

        // @formatter:on
    }

    @Bean
    fun userDetailsServiceImpl(): UserDetailsService {
        return UserDetailsServiceImpl(userRepository)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun daoAuthenticationProvider(): AuthenticationProvider {
        val provider: DaoAuthenticationProvider = DaoAuthenticationProvider()

        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(userDetailsServiceImpl())

        return provider
    }

    @Bean
    fun authTokenizer(): AuthTokenizer {
        // ps понятно что его в открытом виде не надо и хранить
        return HmacJwtAuthTokenizer(String(Base64.getEncoder().encode(token.toByteArray())))
    }

    @Bean
    fun authService(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder,
        userMapper: UserTransformation,
        authTokenizer: AuthTokenizer
    ): AuthService {
        return AuthService(userRepository, passwordEncoder, userMapper, authTokenizer)
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy: RoleHierarchyImpl = RoleHierarchyImpl()

        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_API_OWNER > ROLE_APP_OWNER")

        return roleHierarchy
    }

    @Bean
    fun roleVoter(): RoleVoter {
        return RoleHierarchyVoter(roleHierarchy())
    }
}

fun HttpServletResponse.authentication(token: String){
    this.addHeader(BookSecurityConfiguration.AUTHORIZATION, token)
    this.addHeader("Set-Cookie", "${BookSecurityConfiguration.AUTHORIZATION}=$token; path=/")
}

fun HttpServletResponse.accessDenied(){
    this.addHeader("Set-Cookie", "${BookSecurityConfiguration.AUTHORIZATION}=; path=/; Max-Age=0")
}