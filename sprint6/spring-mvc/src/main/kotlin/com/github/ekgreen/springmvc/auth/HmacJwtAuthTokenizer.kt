package com.github.ekgreen.springmvc.auth

import com.github.ekgreen.springmvc.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.time.Duration
import java.time.Instant
import java.util.*

class HmacJwtAuthTokenizer(private val secret: String): AuthTokenizer {

    override fun generateToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.login)
            .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(3))))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)), SignatureAlgorithm.HS256)
            .compact()
    }

    override fun validateToken(token: String): Boolean {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
            .build()
            .isSigned(token);
    }

}