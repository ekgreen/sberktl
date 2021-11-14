package com.github.ekgreen.springmvc.security.handler

import com.github.ekgreen.springmvc.security.auth.AuthTokenizer
import com.github.ekgreen.springmvc.security.authentication
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// вообще, так как я отключил http сессию, достаточно бесполезное занятие наследоваться от SavedRequestAwareAuthenticationSuccessHandler
// но вдруг потом передумаю =_=
// ЗЫ передумал и унаследовался от простого SimpleUrlAuthenticationSuccessHandler
class JwtUsernamePasswordSuccessHandler(val redirectOn: String, private val tokenizer: AuthTokenizer) : SimpleUrlAuthenticationSuccessHandler() {

    init {
        defaultTargetUrl = redirectOn
    }

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication
    ) {
        // сгенерируем токен и проставим его в куки и хэдер
        val authToken: String = tokenizer.generateAuthToken(authentication.principal as UserDetails)

        // добавим в ответ
        response.authentication(authToken)

        super.onAuthenticationSuccess(request, response, authentication)
    }
}