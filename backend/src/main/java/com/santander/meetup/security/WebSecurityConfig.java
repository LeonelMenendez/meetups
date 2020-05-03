package com.santander.meetup.security;

import com.santander.meetup.endpoint.AuthEndpoint;
import com.santander.meetup.endpoint.EnrollmentEndpoint;
import com.santander.meetup.endpoint.InvitationEndpoint;
import com.santander.meetup.endpoint.MeetupEndpoint;
import com.santander.meetup.endpoint.UserEndpoint;
import com.santander.meetup.endpoint.WeatherEndpoint;
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
                    /************ SWAGGER ************/
                    .antMatchers(SWAGGER_AUTH_WHITELIST).permitAll()

                    /************ AUTH ************/
                    .antMatchers(HttpMethod.POST, AuthEndpoint.BASE + AuthEndpoint.SIGN_UP).permitAll()
                    .antMatchers(HttpMethod.POST, AuthEndpoint.BASE + AuthEndpoint.SIGN_IN).permitAll()

                    /************ USER ************/
                    .antMatchers(HttpMethod.GET, UserEndpoint.ANT_BASE).hasRole(Role.ADMIN.getName())
                    .antMatchers(HttpMethod.GET, UserEndpoint.BASE + UserEndpoint.ANT_MEETUPS_ENROLLMENTS).hasRole(Role.USER.getName())
                    .antMatchers(HttpMethod.GET, UserEndpoint.BASE + UserEndpoint.ANT_MEETUPS_CREATED).hasRole(Role.ADMIN.getName())
                    .antMatchers(HttpMethod.GET, UserEndpoint.BASE + UserEndpoint.ANT_MEETUPS_ENROLLED).hasRole(Role.USER.getName())

                    /************ MEETUP ************/
                    .antMatchers(HttpMethod.POST, MeetupEndpoint.BASE).hasRole(Role.ADMIN.getName())
                    .antMatchers(HttpMethod.GET, MeetupEndpoint.BASE + MeetupEndpoint.ANT_BEER_CASES).hasRole(Role.ADMIN.getName())
                    .antMatchers(HttpMethod.GET, MeetupEndpoint.BASE + MeetupEndpoint.ANT_TEMPERATURE).hasAnyRole(Role.USER.getName(), Role.ADMIN.getName())
                    .antMatchers(HttpMethod.POST, MeetupEndpoint.BASE + MeetupEndpoint.ANT_INVITATIONS).hasRole(Role.ADMIN.getName())

                    /************ ENROLLMENT ************/
                    .antMatchers(HttpMethod.POST, EnrollmentEndpoint.BASE).hasRole(Role.USER.getName())
                    .antMatchers(HttpMethod.POST, EnrollmentEndpoint.BASE + EnrollmentEndpoint.ANT_CHECK_IN).hasRole(Role.USER.getName())

                    /************ INVITATION ************/
                    .antMatchers(HttpMethod.GET, InvitationEndpoint.ANT_BASE).hasRole(Role.USER.getName())
                    .antMatchers(HttpMethod.POST, InvitationEndpoint.BASE).hasRole(Role.ADMIN.getName())
                    .antMatchers(HttpMethod.PATCH, InvitationEndpoint.BASE + InvitationEndpoint.ANT_INVITATION_STATUS).hasRole(Role.USER.getName())

                    /************ WEATHER ************/
                    .antMatchers(HttpMethod.GET, WeatherEndpoint.BASE + WeatherEndpoint.ANT_DAILY_FORECAST).hasAnyRole(Role.USER.getName(), Role.ADMIN.getName())

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
}
