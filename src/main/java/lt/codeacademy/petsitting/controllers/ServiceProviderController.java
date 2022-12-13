package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.payload.response.MessageResponse;
import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.pojo.UserRoles;
import lt.codeacademy.petsitting.services.RoleService;
import lt.codeacademy.petsitting.services.ServiceProviderService;
import lt.codeacademy.petsitting.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/providers")
public class ServiceProviderController {

    private final UserService userService;

    private final ServiceProviderService serviceProviderService;

    private final RoleService roleService;

    private final PasswordEncoder encoder;

    @Autowired
    public ServiceProviderController(UserService userService, ServiceProviderService serviceProviderService, RoleService roleService, PasswordEncoder encoder) {
        this.userService = userService;
        this.serviceProviderService = serviceProviderService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerServiceProvider(@Valid @RequestBody ServiceProvider serviceProvider ){
        if( userService.existsByUsername( serviceProvider.getUsername() )){
            return ResponseEntity
                    .badRequest()
                    .body( new MessageResponse( "Error: Username is already taken!" ));
        } else if( userService.existsByEmail( serviceProvider.getEmail() )){
            return ResponseEntity
                    .badRequest()
                    .body( new MessageResponse( "Error: Email is already in use!" ));
        }
        ServiceProvider newServiceProvider = ServiceProvider.builder()
                .username( serviceProvider.getUsername() )
                .password( encoder.encode( serviceProvider.getPassword() ))
                .email( serviceProvider.getEmail() )
                .firstName( serviceProvider.getFirstName() )
                .lastName( serviceProvider.getLastName() )
                .roles( new HashSet<>() )
                .build();

        Role customerRole = roleService.findByName( UserRoles.ROLE_CUSTOMER ).orElse( null );

        if( customerRole == null ){
            customerRole = roleService.save( new Role( UserRoles.ROLE_CUSTOMER ));
        }

        Role serviceProviderRole = roleService.findByName( UserRoles.ROLE_SERVICE_PROVIDER ).orElse( null );

        if ( serviceProviderRole == null ){
            serviceProviderRole = roleService.save( new Role( UserRoles.ROLE_SERVICE_PROVIDER ));
        }

        newServiceProvider.getRoles().addAll( Set.of( customerRole, serviceProviderRole ));
        serviceProviderService.save( newServiceProvider );

        return ResponseEntity.ok( new MessageResponse( "User registered successfully!" ) );
    }
}
