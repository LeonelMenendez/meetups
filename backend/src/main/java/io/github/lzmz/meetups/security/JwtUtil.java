package io.github.lzmz.meetups.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil implements Serializable {

    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String AUTHORITIES_KEY = "authorities";

    /**
     * Secret that will be used to sign the JWT.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Token duration in seconds.
     */
    @Value("${jwt.token-duration}")
    private long tokenDuration;

    /**
     * Retrieves all the claims from the JWT.
     *
     * @param token the token from which all the claims will be obtained.
     * @return all the claims in the token.
     * @throws UnsupportedJwtException  if the {@code claimsJws} argument does not represent an Claims JWS.
     * @throws MalformedJwtException    if the {@code claimsJws} string is not a valid JWS.
     * @throws SignatureException       if the {@code claimsJws} JWS signature validation fails.
     * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                                  before the time this method is invoked.
     * @throws IllegalArgumentException if the {@code claimsJws} string is {@code null} or empty or only whitespace.
     */
    public Claims parseClaims(String token) throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Retrieves the authorities from the claims.
     *
     * @param claims the claims from which the authorities will be obtained.
     * @return the authorities in the claims.
     * @see #parseClaims(String)
     */
    public Collection<GrantedAuthority> getAuthorities(Claims claims) {
        Collection<?> claimsAuthorities = claims.get(AUTHORITIES_KEY, Collection.class);
        return claimsAuthorities
                .stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Generates a JWT for a user.
     *
     * @param userDetails the {@link UserDetails} from which the token will be generated.
     * @return the JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Generates a JWT.
     * <p>Defines claims of the token, like Issuer, Expiration, Subject, and the ID</p>
     * <p>Signs the JWT using the HS512 algorithm and secret key.</p>
     * <p>According to JWS Compact <a href="URL#https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1">Serialization</a>
     * compaction of the JWT to a URL-safe string</p>
     *
     * @param claims  the claims from which the token will be generated.
     * @param subject the subject from which the token will be generated.
     * @return the JWT.
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenDuration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
