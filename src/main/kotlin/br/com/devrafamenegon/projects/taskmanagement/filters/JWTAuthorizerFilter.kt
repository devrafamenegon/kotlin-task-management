package br.com.devrafamenegon.projects.taskmanagement.filters

import br.com.devrafamenegon.projects.taskmanagement.authorization
import br.com.devrafamenegon.projects.taskmanagement.bearer
import br.com.devrafamenegon.projects.taskmanagement.impl.UserDetailImpl
import br.com.devrafamenegon.projects.taskmanagement.models.User
import br.com.devrafamenegon.projects.taskmanagement.repositories.UserRepository
import br.com.devrafamenegon.projects.taskmanagement.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizerFilter (authenticationManager : AuthenticationManager, val jwtUtils: JWTUtils, val userRepository: UserRepository) : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader(authorization)

        if (authorizationHeader != null && authorizationHeader.startsWith(bearer)) {
            val authorized = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = authorized
        }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorization: String): UsernamePasswordAuthenticationToken {
        val token = authorization.substring(7)
        if (jwtUtils.isValidToken(token)) {
            val idString = jwtUtils.getUserId(token)
            if (!idString.isNullOrBlank() && idString.isNotEmpty()) {
                val user = userRepository.findByIdOrNull(idString.toLong()) ?: throw UsernameNotFoundException("User not found")
                val userImpl = UserDetailImpl(user)
                return UsernamePasswordAuthenticationToken(userImpl, null, userImpl.authorities)
            }
        }

        throw UsernameNotFoundException("Invalid token, or don't have an information")
    }
}