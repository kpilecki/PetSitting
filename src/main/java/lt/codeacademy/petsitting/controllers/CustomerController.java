package lt.codeacademy.petsitting.controllers;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lt.codeacademy.petsitting.error.ApiError;
import lt.codeacademy.petsitting.payload.request.CustomerUpdateRequest;
import lt.codeacademy.petsitting.payload.response.ImageResponse;
import lt.codeacademy.petsitting.payload.response.MessageResponse;
import lt.codeacademy.petsitting.pojo.Address;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.Role;
import lt.codeacademy.petsitting.pojo.UserRoles;
import lt.codeacademy.petsitting.services.AddressService;
import lt.codeacademy.petsitting.services.CustomerService;
import lt.codeacademy.petsitting.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
@PreAuthorize( "hasRole('CUSTOMER')" )
public class CustomerController {

    private static final long IMAGE_MAX_FILESIZE = 2000000;

    private final CustomerService customerService;

    private final RoleService roleService;

    private final AddressService addressService;

    private final PasswordEncoder encoder;

    private final Storage storage;

    @Autowired
    public CustomerController(CustomerService customerService, RoleService roleService, AddressService addressService, PasswordEncoder encoder, Storage storage) {
        this.customerService = customerService;
        this.roleService = roleService;
        this.addressService = addressService;
        this.encoder = encoder;
        this.storage = storage;
    }

    @GetMapping( "/get" )
    public Customer loadCustomer(){
        return getAuthenticatedCustomer();
    }

    @PostMapping
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest ){
        Customer customer = getAuthenticatedCustomer();
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
        Customer customer = getAuthenticatedCustomer();
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
        Customer customer = getAuthenticatedCustomer();
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
        Customer customer = getAuthenticatedCustomer();
        assert customer != null;

        return customer.getAddress();
    }

    @PostMapping("/image" )
    public ResponseEntity<?> uploadImage( @RequestBody MultipartFile file ) throws IOException {

        if( file == null || file.isEmpty() ) {
            return ResponseEntity.badRequest().body( "Error: File is empty" );
        } else if( file.getSize() > IMAGE_MAX_FILESIZE ){
            return ResponseEntity.badRequest().body( "Error: File size limit exceeded, max image size is 2MB" );
        }

        Customer customer = getAuthenticatedCustomer();
        assert customer != null;
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;

        String fileName = customer.getId()
                + "_"
                + customer.getUsername()
                + "_"
                + originalFileName.substring( originalFileName.lastIndexOf( "." ) + 1 );

        BlobId id = BlobId.of( "pet_sitting", fileName);
        BlobInfo info = BlobInfo.newBuilder( id ).build();
        byte[] fileAsByteArray = file.getBytes();
        storage.create( info, fileAsByteArray );

        customer.setProfileImageId( id );
        customerService.save( customer );

        return ResponseEntity.ok( "Success: Image saved" );
    }

    @GetMapping( "/image")
    public ImageResponse getImage() throws IOException {
        Customer customer = getAuthenticatedCustomer();
        assert customer != null;
        if( customer.getProfileImageId() == null ){

            File file = new ClassPathResource("/static/images/profile_image_placeholder.png").getFile();
            return new ImageResponse( Files.readAllBytes( file.toPath() ));
        }
        return  new ImageResponse( storage.get( customer.getProfileImageId() ).getContent() );

    }

    @ExceptionHandler( {MethodArgumentNotValidException.class} )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    ApiError handleValidationException( MethodArgumentNotValidException exception, HttpServletRequest request ){
        ApiError apiError = new ApiError( 400, "Validation error", request.getServletPath() );

        BindingResult result = exception.getBindingResult();

        Map<String, String> validationErrors = new HashMap<>();

        for( FieldError fieldError : result.getFieldErrors() ){
            validationErrors.put( fieldError.getField(), fieldError.getDefaultMessage() );
        }
        apiError.setValidationErrors( validationErrors );

        return apiError;
    }

    private Customer getAuthenticatedCustomer(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth != null ){
            return customerService.getByUsername( auth.getName() );
        }
        return null;
    }


}
