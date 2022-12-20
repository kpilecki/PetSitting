package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.error.ApiError;
import lt.codeacademy.petsitting.payload.request.ServiceProviderAboutRequest;
import lt.codeacademy.petsitting.payload.response.MessageResponse;
import lt.codeacademy.petsitting.pojo.*;
import lt.codeacademy.petsitting.services.AddressService;
import lt.codeacademy.petsitting.services.RoleService;
import lt.codeacademy.petsitting.services.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize( "hasRole('SERVICE_PROVIDER')" )
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    private final RoleService roleService;

    private final AddressService addressService;

    private final PasswordEncoder encoder;

    @Autowired
    public ServiceProviderController(ServiceProviderService serviceProviderService, RoleService roleService, AddressService addressService, PasswordEncoder encoder) {

        this.serviceProviderService = serviceProviderService;
        this.roleService = roleService;
        this.addressService = addressService;
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
    @PreAuthorize( "permitAll()" )
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

        return ResponseEntity.ok( new MessageResponse( "Service Provider registered successfully!" ) );
    }

    @PostMapping("/address")
    public Address addOrUpdatePublicAddress(@Valid @RequestBody Address address ){
        ServiceProvider serviceProvider = getAuthenticatedServiceProvider();
        assert serviceProvider != null;

        if( serviceProvider.getPublicAddress() == null ){
            Address savedAddress = addressService.save( address );
            serviceProvider.setPublicAddress( savedAddress );
        } else {
            address.setId( serviceProvider.getPublicAddress().getId() );
            serviceProvider.setPublicAddress( addressService.save( address ));
        }
        serviceProviderService.save( serviceProvider );
        return serviceProvider.getPublicAddress();
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteAddress( @PathVariable( name = "id" ) Long id ){
        ServiceProvider serviceProvider = getAuthenticatedServiceProvider();
        assert serviceProvider != null;

        if( serviceProvider.getPublicAddress() == null || !serviceProvider.getPublicAddress().getId().equals( id ) ){
            return ResponseEntity.badRequest().body( "Error: Address does not exist" );
        } else {
            serviceProvider.setPublicAddress( null );
            serviceProviderService.save( serviceProvider );
            addressService.deleteById( id );
            return ResponseEntity.ok().body( "Address deleted successfully");
        }
    }

    @GetMapping("/address")
    public Address getAddress(){
        ServiceProvider serviceProvider = getAuthenticatedServiceProvider();
        assert serviceProvider != null;

        return serviceProvider.getPublicAddress();
    }

    @GetMapping( "/about")
    public String getAbout(){
        ServiceProvider serviceProvider = getAuthenticatedServiceProvider();
        assert serviceProvider != null;

        return serviceProvider.getAbout();
    }

    @PostMapping("/about")
    public ResponseEntity<?> updateAbout( @Valid @RequestBody ServiceProviderAboutRequest request ){
        ServiceProvider serviceProvider = getAuthenticatedServiceProvider();
        assert serviceProvider != null;

        serviceProvider.setAbout( request.getAbout() );
        serviceProviderService.save( serviceProvider );

        return ResponseEntity.ok( "Success: About updated successfully" );
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

    private ServiceProvider getAuthenticatedServiceProvider(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth != null ){
            return serviceProviderService.getByUsername( auth.getName() );
        }
        return null;
    }
}
