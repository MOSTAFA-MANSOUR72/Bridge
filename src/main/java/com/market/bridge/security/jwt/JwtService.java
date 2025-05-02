package com.market.bridge.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${security.mail.expiration}")
    private long mailExpiration; // 1 minute
    public String generateMailToken(UserDetails userDetails) {
        return generateMailToken(new HashMap<>(), userDetails);
    }
    public String generateMailToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildMailToken(extraClaims, userDetails);
    }
    private String buildMailToken(Map<String, Object> extraClaims,
                                  UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // User's meta data used in encoding the token ( email / username )
                .setIssuedAt(new Date(System.currentTimeMillis())) // when the token was created
                .setExpiration(new Date(System.currentTimeMillis() + mailExpiration)) // when the token will expire
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // algorithm used to sign the token
                .compact();
    }

    // Generating token
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              long jwtExpiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // User's meta data used in encoding the token ( email / username )
                .setIssuedAt(new Date(System.currentTimeMillis())) // when the token was created
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // when the token will expire
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // algorithm used to sign the token
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //**

    // Verifying token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T>T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
