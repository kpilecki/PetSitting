package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.error.ApiError;
import lt.codeacademy.petsitting.payload.response.MessageResponse;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.UserRoles;
import lt.codeacademy.petsitting.services.CustomerService;
import lt.codeacademy.petsitting.services.RoleService;
import lt.codeacademy.petsitting.services.UserService;
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


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final RoleService roleService;

    private final PasswordEncoder encoder;

    @Autowired
    public CustomerController(CustomerService customerService, RoleService roleService, PasswordEncoder encoder) {
        this.customerService = customerService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @GetMapping( "/get" )
    public Customer loadCustomer(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth != null ){
            return customerService.getByUsername( auth.getName() );
        }
        return null;
    }

    @PostMapping( "/signup")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody Customer customer ){

        Customer newCustomer = Customer.builder()
                .username( customer.getUsername() )
                .password(encoder.encode( customer.getPassword() ))
                .email( customer.getEmail() )
                .firstName( customer.getFirstName() )
                .lastName( customer.getLastName() )
                .roles( new HashSet<>() )
                .build();

        Role customerRole = roleService.findByName( UserRoles.ROLE_CUSTOMER ).orElse( null );

        if( customerRole == null ){
            customerRole = roleService.save( new Role( UserRoles.ROLE_CUSTOMER ));
        }

        newCustomer.getRoles().add( customerRole );
        customerService.save( newCustomer );

        return ResponseEntity.ok( new MessageResponse( "Customer registered successfully!" ) );
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
