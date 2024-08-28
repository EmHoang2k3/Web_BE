package com.project.shopapp.components;


import com.project.shopapp.models.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoder;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private Long expiration; //save to an environment variable
    @Value("${jwt.secretKey}")
    private String secretKey;
    public String generateToken(UserModel user){
        //this.generateSecretKey();
        //properties => claims
        Map<String,Object> claims = new HashMap<>();
        claims.put("phoneNumber",user.getPhoneNumber());
        claims.put("userid",user.getId());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(SignatureAlgorithm.HS256, getSignInKey())
                    .compact();
            return token;
        }catch (Exception e){
            throw new InvalidParameterException("Cannot create jwt token, error: "+e.getMessage());
        }
    }
    private Key getSignInKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);//Keys.hmacShaKeyFor(Decoders.BASE64.decode("v6sr0P/7Ux1oEUNcEkD2MAJvF166duvoxmIqRTV3Hi4="))
        return Keys.hmacShaKeyFor(bytes);
    }

    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    private Claims extractAllClams(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
       final Claims claims = this.extractAllClams(token);
      return claimsResolver.apply(claims);
    }

    //Check expiration
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractPhoneNumber (String token){
        return extractClaim(token,Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }

}
