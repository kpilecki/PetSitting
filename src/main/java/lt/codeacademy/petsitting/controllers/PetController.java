package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.error.ApiError;
import lt.codeacademy.petsitting.payload.response.PetsResponse;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.Pet;
import lt.codeacademy.petsitting.services.CustomerService;
import lt.codeacademy.petsitting.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static lt.codeacademy.petsitting.error.ApiError.getApiError;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pets")
@PreAuthorize( "hasRole('CUSTOMER')" )
public class PetController {

    private final PetService petService;

    private final CustomerService customerService;

    @Autowired
    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<?> getPets(){
        Customer customer = customerService.getAuthenticatedCustomer();
        PetsResponse response = new PetsResponse( customer.getPets() );

        return ResponseEntity.ok( response );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById( @PathVariable Long id ){
        Customer customer = customerService.getAuthenticatedCustomer();
        PetsResponse response = new PetsResponse( customer.getPets().stream()
                .filter( pet -> pet.getId().equals( id ) ).toList() );
        if( response.getPets().isEmpty() ){
            return ResponseEntity.badRequest().body( "Error: No pets found" );
        }
        return ResponseEntity.ok( response );
    }

    @PostMapping
    public ResponseEntity<?> savePet(@Valid @RequestBody Pet petToSave ){
        Customer customer = customerService.getAuthenticatedCustomer();
        Pet savedPet = petService.save( petToSave );

        if( customer.getPets() == null ){
            customer.setPets( List.of( savedPet ) );
        } else {
            customer.getPets().add( savedPet );
        }
        customerService.save( customer );

        return ResponseEntity.ok( "Success: Pet Saved Successfully" );
    }

    @PutMapping
    public ResponseEntity<?> updatePet(@Valid @RequestBody Pet petToSave ){
        Customer customer = customerService.getAuthenticatedCustomer();
        Pet petToUpdate = customer.getPets()
                .stream()
                .filter( v -> v.getId().equals( petToSave.getId() ) )
                .findFirst()
                .orElse( null );

        if( petToUpdate == null ){
            return ResponseEntity.badRequest().body( "Error: Pet not found" );
        } else {
            petToSave.setProfileImageId( petToUpdate.getProfileImageId() );
            customer.getPets().remove( petToUpdate );
            customer.getPets().add( petService.save( petToSave ) );
            customerService.save( customer );
            return ResponseEntity.ok( "Success: Pet Saved Successfully" );
        }
    }

    @ExceptionHandler( {MethodArgumentNotValidException.class} )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request ){
        return getApiError(exception, request);
    }
}
