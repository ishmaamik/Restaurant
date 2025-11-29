package Auth.services;

import Auth.DTOs.AuthResponseDTO;
import Auth.DTOs.LoginRequestDTO;
import Auth.DTOs.RefreshRequestDTO;
import Security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

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
        long expiresIn= jwtService.getAccessExpiration();
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
        long expiresIn= jwtService.getAccessExpiration();
        return new AuthResponseDTO(newAccessToken, refreshToken, expiresIn, "Bearer");
    }
}
