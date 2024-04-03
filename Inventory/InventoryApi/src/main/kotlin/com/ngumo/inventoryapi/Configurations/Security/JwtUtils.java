package com.ngumo.inventoryapi.Configurations.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtUtils {

    private String jwtSigningKey = "R4jIWTh7T1mLpUe26wVE6VeSnmT5Ll9ByzC7nB3v+4w=";

    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }
    public  Date extractExpiration(String token){
        return extractClaims(token, Claims:: getExpiration);


    }

    public boolean hasClaim(String token, String claimName){
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;

    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token);
        return jws.getBody();
    }

    private Boolean isTokenExpired(String token ){
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        return createToken(userDetails);
    }

    private String createToken(UserDetails userDetails) {
        long millisecondsIn48Hours = TimeUnit.HOURS.toMillis(48);


        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + millisecondsIn48Hours))
                .signWith(SignatureAlgorithm.HS256, jwtSigningKey).compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
