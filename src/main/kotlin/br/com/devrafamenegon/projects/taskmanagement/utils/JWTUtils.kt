package br.com.devrafamenegon.projects.taskmanagement.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class JWTUtils {
    private val secretKey = "883aa5a61c37513862cffcddf31df730"

    fun generateToken (userId : String) : String {
        return Jwts.builder().setSubject(userId).signWith(SignatureAlgorithm.HS256, secretKey.toByteArray()).compact()
    }

    fun isValidToken (token : String) : Boolean {
        val claims = getClaimsToken(token)

        if (claims != null) {
            val userId = claims.subject
            if (!userId.isNullOrEmpty() && userId.isNotBlank()) {
                return true
            }
        }

        return false
    }

    private fun getClaimsToken(token: String) = try {
        Jwts.parser().setSigningKey(secretKey.toByteArray()).parseClaimsJws(token).body
    } catch (exception: Exception) {
        null
    }

    fun getUserId (token: String) : String? {
        val claims = getClaimsToken(token)
        return claims?.subject
    }
}