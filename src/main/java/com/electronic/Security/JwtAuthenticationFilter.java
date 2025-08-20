package com.electronic.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtHelper jwtHelper;

    // ye har api se pahle chalega token verify karne ke liye
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Authetication Bearer  jshfkjsdfkljhfslkj
        String authorization = request.getHeader("Authorization");
        // header aana jaroori hai
        logger.info("Header: {}", authorization);

        String username = null;
        String token = null;

        if (authorization != null && authorization.startsWith("Bearer")) {
            // thik hai sab
            token = authorization.substring(7);
            try {
                username = jwtHelper.getUsernameFromToken(token);
                logger.info("Username: {}", username);
            }catch (IllegalArgumentException e){
                logger.info("Illegal argument while fetching the username "+e.getMessage());
            }catch (ExpiredJwtException ex){
                logger.info("Expired JWT Token");
            }catch (MalformedJwtException ex){
                logger.info("some cahanges has done in JWT Token");
            }catch (Exception ex){
               ex.printStackTrace();
            }
        }

        else {
            logger.error("Authorization header is invalid");
        }
        // agr username me null nahi hai to kaam karenge
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // username me kuch ahi to
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(username.equals(userDetails.getUsername()) &&!jwtHelper.isTokenExpired(token)){
                // token valid hai ab
                // security conetxt ke ander authnetication set karenge
                UsernamePasswordAuthenticationToken authetication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

               authetication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authetication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
