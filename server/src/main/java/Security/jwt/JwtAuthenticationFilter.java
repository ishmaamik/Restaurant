package Security.jwt;

import Security.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JWTService jwtService;

    public JwtAuthenticationFilter(JWTService jwtService){
        this.jwtService= jwtService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader= request.getHeader("Authorization");

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token= authHeader.substring(7);

            if(jwtService.isAccessTokenValid(token)){
                String username= jwtService.extractUsernameFromAccessToken(token);
                Map<String, Object> claims= jwtService.extractAllClaims(token);
                @SuppressWarnings("unchecked")
                var roles=  (List<Map<String, Object>>) claims.get("roles");

                List<SimpleGrantedAuthority> authorities= roles == null ? List.of() : roles.stream().map(r-> new SimpleGrantedAuthority((String) r.get("authority"))).toList();

                UsernamePasswordAuthenticationToken auth= new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}
