package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.payload.response.MessageResponse;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.UserRoles;
import lt.codeacademy.petsitting.services.CustomerService;
import lt.codeacademy.petsitting.services.RoleService;
import lt.codeacademy.petsitting.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder encoder;

    @Autowired
    public CustomerController(CustomerService customerService, UserService userService, RoleService roleService, PasswordEncoder encoder) {
        this.customerService = customerService;
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @PostMapping( "/signup")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody Customer customer ){
        if( userService.existsByUsername( customer.getUsername() )){
            return ResponseEntity
                    .badRequest()
                    .body( new MessageResponse( "Error: Username is already taken!" ));
        } else if( userService.existsByEmail( customer.getEmail() )){
            return ResponseEntity
                    .badRequest()
                    .body( new MessageResponse( "Error: Email is already in use!" ));
        }

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


}
