package lt.codeacademy.petsitting.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lt.codeacademy.petsitting.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger( JwtUtils.class );
    private final SecretKey jwtSecret = Keys.secretKeyFor( SignatureAlgorithm.HS512 );

    private int jwtExpirationMs = 86400000;

    public String generateJwtToken( Authentication authentication ) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject( userPrincipal.getUsername() )
                .setIssuedAt( new Date() )
                .setExpiration( new Date( (new Date()).getTime() + jwtExpirationMs ))
                .signWith( jwtSecret, SignatureAlgorithm.HS512 )
                .compact();
    }

    public String getUserNameFromJwtToken( String token ) {
        return Jwts
                .parserBuilder()
                .setSigningKey( jwtSecret )
                .build()
                .parseClaimsJws( token )
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken( String authToken ) {
        System.out.println( jwtSecret );
        try {
            Jwts
                .parserBuilder()
                .setSigningKey( jwtSecret )
                .build()
                .parseClaimsJws( authToken );
            return true;
        } catch ( SignatureException e ) {
            logger.error( "Invalid JWT signature: {}", e.getMessage() );
        } catch ( MalformedJwtException e ) {
            logger.error( "Invalid JWT token: {}", e.getMessage() );
        } catch ( ExpiredJwtException e ) {
            logger.error( "JWT token is expired: {}", e.getMessage() );
        } catch ( UnsupportedJwtException e ) {
            logger.error( "JWT token is unsupported: {}", e.getMessage() );
        } catch ( IllegalArgumentException e ) {
            logger.error( "JWT claims string is empty: {}", e.getMessage() );
        }
        return false;
    }
}