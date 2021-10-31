package com.github.ekgreen.springmvc.filter

import com.github.ekgreen.springmvc.auth.AuthServlet.Companion.AUTHORIZATION
import com.github.ekgreen.springmvc.auth.AuthServlet.Companion.logger
import com.github.ekgreen.springmvc.auth.AuthTokenizer
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoggingAddressBookFilter: OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        if(Companion.logger.isDebugEnabled)
            // не очень хорошо логировать урл потому-что могут быть инъекции, но у нас самые лучшие пользователи
            Companion.logger.debug { "servlet request { uri=${request.requestURI} method=${request.method} }" }

        filterChain.doFilter(request,response)
    }

    companion object: KLogging()
}