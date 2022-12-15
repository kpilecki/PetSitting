package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.error.ApiError;
import lt.codeacademy.petsitting.payload.response.MessageResponse;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.pojo.UserRoles;
import lt.codeacademy.petsitting.services.RoleService;
import lt.codeacademy.petsitting.services.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/providers")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    private final RoleService roleService;

    private final PasswordEncoder encoder;

    @Autowired
    public ServiceProviderController( ServiceProviderService serviceProviderService, RoleService roleService, PasswordEncoder encoder) {

        this.serviceProviderService = serviceProviderService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @GetMapping( "/get" )
    public ServiceProvider loadServiceProvider(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth != null ){
            return serviceProviderService.getByUsername( auth.getName() );
        }
        return null;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerServiceProvider(@Valid @RequestBody ServiceProvider serviceProvider ){

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

    @ExceptionHandler( {MethodArgumentNotValidException.class} )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request ){
        ApiError apiError = new ApiError( 400, "Validation error", request.getServletPath() );

        BindingResult result = exception.getBindingResult();

        Map<String, String> validationErrors = new HashMap<>();

        for( FieldError fieldError : result.getFieldErrors() ){
            validationErrors.put( fieldError.getField(), fieldError.getDefaultMessage() );
        }
        apiError.setValidationErrors( validationErrors );

        return apiError;
    }
}
