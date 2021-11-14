package com.github.ekgreen.springmvc.security.handler

import com.github.ekgreen.springmvc.security.BookSecurityConfiguration.Companion.AUTHORIZATION
import com.github.ekgreen.springmvc.security.auth.AuthTokenizer
import org.springframework.security.authentication.AuthenticationTrustResolver
import org.springframework.security.authentication.AuthenticationTrustResolverImpl
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.context.HttpRequestResponseHolder
import org.springframework.security.web.context.SecurityContextRepository
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtSecurityContextRepository(
    private val tokenizer: AuthTokenizer,
    private val stash: MutableMap<String, SecurityContext> = ConcurrentHashMap(),
    private var trustResolver: AuthenticationTrustResolver = AuthenticationTrustResolverImpl()
) : SecurityContextRepository {

    override fun loadContext(requestResponseHolder: HttpRequestResponseHolder): SecurityContext? {
        val token: String = getAuthorizationToken(requestResponseHolder.request) ?: return generateNewContext()

        val username: String = tokenizer.getSubject(token) ?: return generateNewContext()

        return stash.getOrElse(username, this::generateNewContext)
    }

    override fun saveContext(context: SecurityContext, request: HttpServletRequest, response: HttpServletResponse) {
        val authentication: Authentication? = context.authentication

        if (authentication != null && !trustResolver.isAnonymous(authentication))
            stash[authentication.name] = context
    }

    override fun containsContext(request: HttpServletRequest): Boolean {
        val token: String = getAuthorizationToken(request) ?: return false

        val username: String = tokenizer.getSubject(token) ?: return false

        return stash.containsKey(username)
    }

    private fun getAuthorizationToken(request: HttpServletRequest): String? {
        return if (request.cookies != null)
            request.cookies.lastOrNull { AUTHORIZATION == it.name }?.value
        else
            null
    }

    private fun generateNewContext(): SecurityContext? {
        return SecurityContextHolder.createEmptyContext()
    }
}