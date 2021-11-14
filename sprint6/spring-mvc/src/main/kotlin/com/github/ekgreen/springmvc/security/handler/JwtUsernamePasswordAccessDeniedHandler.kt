package com.github.ekgreen.springmvc.security.handler

import com.github.ekgreen.springmvc.security.accessDenied
import com.github.ekgreen.springmvc.security.auth.AuthTokenizer
import com.github.ekgreen.springmvc.security.authentication
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtUsernamePasswordAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {
        response.accessDenied()
    }
 }