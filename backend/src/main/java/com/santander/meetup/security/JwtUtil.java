package com.santander.meetup.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil implements Serializable {

    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Token duration in seconds.
     */
    @Value("${jwt.token-duration}")
    private long tokenDuration;

    /**
     * Retrieves the username from the JWT.
     *
     * @param token the token from which the username will be obtained.
     * @return the username in the token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieves the expiration date from the JWT.
     *
     * @param token the token from which the expiration date will be obtained.
     * @return the expiration date in the token.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Retrieves a specific claim from the JWT.
     *
     * @param token          the token from which the claim will be obtained.
     * @param claimsResolver {@link Claims}
     * @param <T>            {@link Claims}
     * @return the requested claim in the token.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieves all the claims from the JWT.
     *
     * @param token the token from which all the claims will be obtained.
     * @return all the claims in the token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the JWT has expired.
     *
     * @param token the token to be checked.
     * @return {@code true} if the token has expired. {@code false} otherwise.
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generates a JWT for a user.
     *
     * @param userDetails the {@link UserDetails} from which the token will be generated.
     * @return the JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
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
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenDuration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Validates a JWT.
     *
     * @param token       the token to be validated.
     * @param userDetails the {@link UserDetails} which will be used to validate the token.
     * @return {@code true} if the token is valid. {@code false} otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
