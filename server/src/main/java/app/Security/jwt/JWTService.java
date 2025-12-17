package app.Security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@Data
public class JWTService {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;
    private final long accessExpiration;
    private final long refreshExpiration;
    private final JwtParser accessParser;
    private final JwtParser refreshParser;

    public JWTService(
        @Value("${security.jwt.access-secret}")
        String accessSecret,
        @Value("${security.jwt.refresh-secret}")
        String refreshSecret,
        @Value("${security.jwt.access-validity-secs}")
        long accessValidity,
        @Value("${security.jwt.refresh-validity-secs}")
        long refreshValidity
    ){
        this.accessKey= Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshKey= Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
        this.accessExpiration= accessValidity;
        this.refreshExpiration= refreshValidity;
        this.accessParser= Jwts.parser().verifyWith(accessKey).build();
        this.refreshParser= Jwts.parser().verifyWith(refreshKey).build();
    }

    public String buildToken(String username, Map<String, Object> claims, Key key, Long ttlSeconds){
        long now= System.currentTimeMillis();
        Date iat= new Date(now);
        Date exp= new Date(now + ttlSeconds * 1000L);
        return Jwts.builder()
                .subject(username)
                .issuedAt(iat)
                .claims(claims)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public String generateAccessToken(String username, Map<String, Object> extraClaims){
        return buildToken(username, extraClaims, accessKey, accessExpiration);
    }

    public String generateRefreshToken(String username){
        return buildToken(username, Collections.emptyMap(), refreshKey, refreshExpiration);
    }

    public <T> T extractClaim(JwtParser parser,String token, Function<Claims,T> claimsResolver){
        Claims claims= parser.parseSignedClaims(token).getPayload();
        return claimsResolver.apply(claims);
    }

    public Map<String, Object> extractAllClaims(String token){
        Claims claims= accessParser.parseSignedClaims(token).getPayload();
        return new HashMap<> (claims);
    }

    public String extractUsernameFromAccessToken(String accessToken){
        return extractClaim(accessParser, accessToken, Claims::getSubject);
    }

    public String extractUsernameFromRefreshToken(String refreshToken){
        return extractClaim(refreshParser, refreshToken, Claims::getSubject);
    }


    public boolean validateToken(JwtParser parser, String token){
        try{
            parser.parseSignedClaims(token);
            return true;
        }
        catch(JwtException | IllegalStateException e){
            return false;
        }
    }

    public boolean isAccessTokenValid(String accessToken){
        return validateToken(accessParser, accessToken);
    }

    public boolean isRefreshTokenValid(String refreshToken){
        return validateToken(refreshParser, refreshToken);
    }

    public Date getAccessExpiration(String accessToken){
        return extractClaim(accessParser, accessToken, Claims::getExpiration);
    }

    public Date getRefreshExpiration(String refreshToken){
        return extractClaim(refreshParser, refreshToken, Claims::getExpiration);
    }
}
