package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.payload.request.LoginRequest;
import lt.codeacademy.petsitting.payload.response.JwtResponse;
import lt.codeacademy.petsitting.security.jwt.JwtUtils;
import lt.codeacademy.petsitting.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping( "/login" )
    public ResponseEntity<?> authenticateUser( @Valid @RequestBody LoginRequest loginRequest ) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken( loginRequest.getUsername(), loginRequest.getPassword() ));

            SecurityContextHolder.getContext().setAuthentication( authentication );
            String jwt = jwtUtils.generateJwtToken( authentication );

            UserDetailsImpl userDetails = ( UserDetailsImpl ) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } catch ( AuthenticationException e ){
            return ResponseEntity.badRequest().body( e.getMessage() );
        }
    }



}