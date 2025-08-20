package com.electronic.Security;
// this class is used to perform jwt operations


import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtHelper {

    //requirement
    //1 validity
    //2 secret key

    public static  final long token_Validity = 5* 60*60*1000;  // it is converted into milisecond

    public  static  final  String  SECRET_KEY= "asdogfsaogfnsanfosfhwasfnsofnsfsfosianfowehroiwgafsadfwiotbhwfsfnfwhoahwafnsfawofhwqohtwsnfsaf";


    // Get username from the token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Generic method to extract claims using a resolver function
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    // This method is assumed to parse the token and return all claims
    private Claims getAllClaimsFromToken(String token) {

        // 0.12.5
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getPayload();
//        SignatureAlgorithm hs512 = SignatureAlgorithm.HS512;
//       SecretKeySpec key =  new SecretKeySpec(secretkey.getBytes(), hs512.getJcaName());
//        return Jwts.parser().verifyWith(key).build().parseClaimsJws(token).getBody();
    }

    // get if the token is expired
    public  Boolean isTokenExpired(String token) {
        final  Date expiration = getExpirationDateFromtoken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromtoken(String token) {
        return  getClaimFromToken(token, Claims::getExpiration);
    }

    // generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + token_Validity))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }
}
