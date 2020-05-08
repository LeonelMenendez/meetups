package io.github.lzmz.meetups.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static io.github.lzmz.meetups.security.JwtUtil.HEADER_AUTHORIZATION_KEY;
import static io.github.lzmz.meetups.security.JwtUtil.TOKEN_BEARER_PREFIX;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final SecurityResponder securityResponder;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, SecurityResponder securityResponder) {
        this.jwtUtil = jwtUtil;
        this.securityResponder = securityResponder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String jwt = getJwtFromHeader(req);

        if (jwt != null) {
            try {
                Claims claims = jwtUtil.parseClaims(jwt);
                setAuthentication(claims, req);
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | ExpiredJwtException | IllegalArgumentException ex) {
                securityResponder.handleJwtException(res, ex);
                return;
            }
        }

        chain.doFilter(req, res);
    }

    /**
     * Retrieves the JWT present in the header or {@code null} if there is none.
     *
     * @param req {@link HttpServletRequest}
     * @return the JWT present in the header.
     */
    private String getJwtFromHeader(HttpServletRequest req) {
        String header = req.getHeader(HEADER_AUTHORIZATION_KEY);
        if (header == null || !header.startsWith(TOKEN_BEARER_PREFIX)) {
            return null;
        }

        return req.getHeader(HEADER_AUTHORIZATION_KEY).replace(TOKEN_BEARER_PREFIX, "");
    }

    /**
     * Sets the authentication context with the given claims.
     *
     * @param claims {@link Claims}
     * @param req    {@link HttpServletRequest}
     */
    private void setAuthentication(Claims claims, HttpServletRequest req) {
        Collection<GrantedAuthority> authorities = jwtUtil.getAuthorities(claims);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
