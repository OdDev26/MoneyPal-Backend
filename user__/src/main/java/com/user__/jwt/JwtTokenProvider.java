package com.user__.jwt;





import com.user__.entity.User;
import com.user__.exception.JWTException;
import com.user__.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    @Autowired
    private UserRepository userRepository;
    // generate token
    public String generateToken(Authentication authentication){
      String username = authentication.getName();
//        User user= (User) authentication.getPrincipal();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);
//        User user= userRepository.findByUsernameOrEmail(username,"").get();
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return token;
    }

    // get username from the token
    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            throw new JWTException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JWTException( "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JWTException( "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JWTException( "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JWTException( "JWT claims string is empty.");
        }
    }
}
