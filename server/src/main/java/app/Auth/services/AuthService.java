package app.Auth.services;

import app.Auth.DTOs.AuthResponseDTO;
import app.Auth.DTOs.LoginRequestDTO;
import app.Auth.DTOs.RefreshRequestDTO;
import app.Security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class AuthService {

    private JWTService jwtService;
    private AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsService userDetailsService;
    public AuthService(JWTService jwtService, AuthenticationManager authenticationManager){
        this.jwtService= jwtService;
        this.authenticationManager= authenticationManager;
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO){
        Authentication auth= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password())
        );

        //authentication manager is delegating it to Dao authentication provider behind the scenes

        //auth object
        var user= (UserDetails) auth.getPrincipal();

        Map<String, Object> claims= Map.of("roles", user.getAuthorities());

        String accessToken= jwtService.generateAccessToken(user.getUsername(), claims);
        String refreshToken= jwtService.generateRefreshToken(user.getUsername());
        Date expirationDate = jwtService.getAccessExpiration(accessToken);
        long expiresIn = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
        return new AuthResponseDTO(accessToken, refreshToken, expiresIn, "Bearer");
    }

    public AuthResponseDTO refresh(RefreshRequestDTO refreshRequestDTO){
        String refreshToken= refreshRequestDTO.refreshToken();

        if(!jwtService.isRefreshTokenValid(refreshToken)){
            throw new BadCredentialsException("Refresh token is invalid");
        }

        String username= jwtService.extractUsernameFromRefreshToken(refreshToken);

        UserDetails user= userDetailsService.loadUserByUsername(username);

        Map <String, Object> claims= Map.of("roles", user.getAuthorities());

        String newAccessToken= jwtService.generateAccessToken(username, claims);
        Date expirationDate = jwtService.getAccessExpiration(newAccessToken);
        long expiresIn = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
        return new AuthResponseDTO(newAccessToken, refreshToken, expiresIn, "Bearer");
    }
}
