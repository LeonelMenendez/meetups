package com.santander.meetup.security;

import com.santander.meetup.constant.AuthEndPoint;
import com.santander.meetup.constant.EnrollmentEndPoint;
import com.santander.meetup.constant.MeetupEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.santander.meetup.config.SwaggerConfig.SWAGGER_AUTH_WHITELIST;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(SWAGGER_AUTH_WHITELIST).permitAll()
                    .antMatchers(HttpMethod.POST, AuthEndPoint.ROOT + AuthEndPoint.SIGN_UP).permitAll()
                    .antMatchers(HttpMethod.POST, AuthEndPoint.ROOT + AuthEndPoint.SIGN_IN).permitAll()
                    .antMatchers(HttpMethod.POST, MeetupEndPoint.ROOT).hasRole(Role.ADMIN.getName())
                    .antMatchers(HttpMethod.POST, EnrollmentEndPoint.ROOT).hasRole(Role.USER.getName())
                    .antMatchers(HttpMethod.POST, EnrollmentEndPoint.ROOT + EnrollmentEndPoint.ANT_CHECK_IN).hasRole(Role.USER.getName())
                .anyRequest().authenticated().and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler).and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
