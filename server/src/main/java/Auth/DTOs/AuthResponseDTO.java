package Auth.DTOs;

public record AuthResponseDTO (String accessToken, String refreshToken, long ttlSeconds, String tokenType){
}
