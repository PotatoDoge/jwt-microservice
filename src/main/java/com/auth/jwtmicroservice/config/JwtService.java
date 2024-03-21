package com.auth.jwtmicroservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    /**
     * Extras username from token
     * @param token jwt
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token) { return extractClaim(token, Claims::getId);}

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

    }

    /**
     * Gets signing key based on secret key applied to encoding algorithm
     * @return signing key
     */
    private Key getSigningKey() {
        byte[] keyBites = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBites);
    }

    /**
     * Generates token based on user info
     * @param userDetails user info
     * @return jwt
     */
    public String generateToken(UserDetails userDetails, Long id){
        return generateToken(new HashMap<>(), userDetails, id);
    }

    /**
     * Check if the token's username is valid
     * @param token jwt
     * @param userDetails user info
     * @return true if token is valid, else false
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the token is expired or not
     * @param token jwt
     * @return true if token is not expired, else false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Gets token expiration info
     * @param token jwt
     * @return when is the token expiring
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Method that generates JWT (json web token)
     * @param extraClaims extra info
     * @param userDetails user info
     * @return jwt
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Long id){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setId(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60000 * 60 * 24)) // 1 minute * 60 * 24
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


}
