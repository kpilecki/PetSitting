package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.error.ApiError;
import lt.codeacademy.petsitting.payload.request.CustomerUpdateRequest;
import lt.codeacademy.petsitting.payload.response.MessageResponse;
import lt.codeacademy.petsitting.pojo.Address;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.UserRoles;
import lt.codeacademy.petsitting.services.AddressService;
import lt.codeacademy.petsitting.services.CustomerService;
import lt.codeacademy.petsitting.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;

import static lt.codeacademy.petsitting.error.ApiError.getApiError;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
@PreAuthorize( "hasRole('CUSTOMER')" )
public class CustomerController {

    private final CustomerService customerService;

    private final RoleService roleService;

    private final AddressService addressService;

    private final PasswordEncoder encoder;

    @Autowired
    public CustomerController(CustomerService customerService, RoleService roleService, AddressService addressService, PasswordEncoder encoder ) {
        this.customerService = customerService;
        this.roleService = roleService;
        this.addressService = addressService;
        this.encoder = encoder;
    }

    @GetMapping( "/get" ) //TODO Change Response type
    public Customer loadCustomer(){
        return customerService.getAuthenticatedCustomer();
    }

    @PostMapping
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest ){
        Customer customer = customerService.getAuthenticatedCustomer();
        assert customer != null;

        if( customerUpdateRequest.getUsername() != null
                && !customerUpdateRequest.getUsername().equals( "" ) ){
            customer.setUsername( customerUpdateRequest.getUsername() );
        }
        if( customerUpdateRequest.getFirstName() != null
                && !customerUpdateRequest.getFirstName().equals( "" )){
            customer.setFirstName( customerUpdateRequest.getFirstName() );
        }
        if( customerUpdateRequest.getLastName() != null
                && !customerUpdateRequest.getLastName().equals( "" ) ){
            customer.setLastName( customerUpdateRequest.getLastName() );
        }
        if( customerUpdateRequest.getEmail() != null
                && !customerUpdateRequest.getEmail().equals( "" ) ){
            customer.setEmail( customerUpdateRequest.getEmail() );
        }
        customerService.save( customer );

        return ResponseEntity.ok( new MessageResponse( "Customer updated successfully!" ) );
    }

    @PostMapping( "/signup")
    @PreAuthorize( "permitAll()")
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

    @PostMapping("/address")
    public Address addOrUpdateAddress( @Valid @RequestBody Address address ){
        Customer customer = customerService.getAuthenticatedCustomer();
        assert customer != null;

        if( customer.getAddress() == null ){
            Address savedAddress = addressService.save( address );
            customer.setAddress( savedAddress );
        } else {
            address.setId( customer.getAddress().getId() );
            customer.setAddress( addressService.save( address ));
        }
        customerService.save( customer );
        return customer.getAddress();
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteAddress( @PathVariable( name = "id" ) Long id ){
        Customer customer = customerService.getAuthenticatedCustomer();
        assert customer != null;

        if( customer.getAddress() == null || !customer.getAddress().getId().equals( id ) ){
           return ResponseEntity.badRequest().body( "Error: Address does not exist" );
        } else {
            customer.setAddress( null );
            customerService.save( customer );
            addressService.deleteById( id );
            return ResponseEntity.ok().body( "Address deleted successfully");
        }
    }

    @GetMapping("/address")
    public Address getAddress(){
        Customer customer = customerService.getAuthenticatedCustomer();
        assert customer != null;

        return customer.getAddress();
    }

    @ExceptionHandler( {MethodArgumentNotValidException.class} )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    ApiError handleValidationException( MethodArgumentNotValidException exception, HttpServletRequest request ){
        return getApiError(exception, request);
    }
}
