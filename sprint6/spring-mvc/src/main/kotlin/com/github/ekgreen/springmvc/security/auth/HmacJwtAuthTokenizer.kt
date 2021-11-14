package com.github.ekgreen.springmvc.security.auth

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import mu.KLogging
import org.springframework.security.core.userdetails.UserDetails
import java.time.Duration
import java.time.Instant
import java.util.*

class HmacJwtAuthTokenizer(private val secret: String): AuthTokenizer {

    override fun generateAuthToken(user: UserDetails): String {
        return Jwts.builder()
            .setSubject(user.username)
            .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(3))))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)), SignatureAlgorithm.HS256)
            .compact()
    }

    override fun validateToken(token: String): Boolean {
        try {
            // билдер для верификации токена
            val parser: JwtParser = parser()

            // проверим что токен валидный и не протух
            return parser.isSigned(token) && parser.parseClaimsJws(token) != null
        }catch (expired: ExpiredJwtException){
            logger.warn(t = expired, msg = { "token expired" })
            return false
        }
    }

    override fun getSubject(token: String): String? {
        return if(validateToken(token))
            parser().parseClaimsJws(token).body.subject
        else
            null
    }

    private fun parser(): JwtParser {
        // билдер для верификации токена
        val parser: JwtParser = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
            .build()

        return parser
    }

    companion object: KLogging()

}