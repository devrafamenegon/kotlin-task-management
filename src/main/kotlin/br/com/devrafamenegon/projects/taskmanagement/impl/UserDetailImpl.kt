package br.com.devrafamenegon.projects.taskmanagement.impl

import br.com.devrafamenegon.projects.taskmanagement.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailImpl (private val user: User) : UserDetails {
    override fun getAuthorities() = mutableListOf<GrantedAuthority>()

    override fun getPassword() = user.password

    override fun getUsername() = user.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

}