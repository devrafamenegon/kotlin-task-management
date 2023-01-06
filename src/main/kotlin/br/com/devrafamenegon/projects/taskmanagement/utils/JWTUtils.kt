package br.com.devrafamenegon.projects.taskmanagement.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class JWTUtils {
    private val secretKey = "883aa5a61c37513862cffcddf31df730"

    fun generateToken(userId : String) : String {
        return Jwts.builder().setSubject(userId).signWith(SignatureAlgorithm.HS256, secretKey.toByteArray()).compact()
    }
}