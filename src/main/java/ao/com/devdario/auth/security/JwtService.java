package ao.com.devdario.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private  long jwtExpiration;

    // Two alternatives to generate the token
    public String generateToken(UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails,jwtExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims,UserDetails userDetails){
        return buildToken(extraClaims,userDetails,jwtExpiration);
    }

    // Builds the JWT Token
    public String buildToken(Map<String,Object> extraClaims, UserDetails userDetails,long expiration){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date (System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Checks is the token is still valid
    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extract the user email from the claims
    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public long getExpirationTime(){
        return jwtExpiration;
    }

    // Checks if the token has already expired
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration date from the Claims
    private Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    // Extract claims (information on the JWT Token)
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // signs our JWT Token
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
