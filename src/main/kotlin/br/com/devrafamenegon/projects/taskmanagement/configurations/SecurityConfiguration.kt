package br.com.devrafamenegon.projects.taskmanagement.configurations

import br.com.devrafamenegon.projects.taskmanagement.filters.JWTAuthorizerFilter
import br.com.devrafamenegon.projects.taskmanagement.repositories.UserRepository
import br.com.devrafamenegon.projects.taskmanagement.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun configure(http: HttpSecurity?) {
        if (http != null) {

            // which routes will be public
            http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                .anyRequest().authenticated()

            // cors configuration public
            http.cors().configurationSource(configureCors())

            // security filter
            http.addFilter(JWTAuthorizerFilter(authenticationManager(), jwtUtils, userRepository))

            // storage session with tokens
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
    }

    @Bean
    fun configureCors() : CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOrigins = mutableListOf("*")
        configuration.allowedMethods = mutableListOf("*")
        configuration.allowedHeaders = mutableListOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}