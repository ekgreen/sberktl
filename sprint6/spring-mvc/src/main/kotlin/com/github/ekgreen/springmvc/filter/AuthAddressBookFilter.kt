package com.github.ekgreen.springmvc.filter

import com.github.ekgreen.springmvc.auth.AuthConfiguration
import com.github.ekgreen.springmvc.auth.AuthServlet.Companion.AUTHORIZATION
import com.github.ekgreen.springmvc.auth.AuthTokenizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
// я умышлено не использую аннотацию WebFilter
class AuthAddressBookFilter(
    private val configuration: AuthConfiguration,
    private val tokenizer: AuthTokenizer
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        if (configuration.security.any { path -> request.requestURI.startsWith(path) }) {
            val cookies: Array<out Cookie>? = request.cookies

            if (cookies == null) {
                response.sendError(403)
                return
            }

            val authorization: Cookie? = request.cookies.last { AUTHORIZATION == it.name }

            if (authorization == null || !tokenizer.validateToken(authorization.value)) {
                response.sendError(403)
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}