package br.com.devrafamenegon.projects.taskmanagement.configurations

import br.com.devrafamenegon.projects.taskmanagement.filters.JWTAuthorizerFilter
import br.com.devrafamenegon.projects.taskmanagement.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    override fun configure(http: HttpSecurity?) {
        if (http != null) {

            // which routes will be public
            http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .anyRequest().authenticated()

            // security filter
            http.addFilter(JWTAuthorizerFilter(authenticationManager(), jwtUtils))

            // storage session with tokens
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

    }
}