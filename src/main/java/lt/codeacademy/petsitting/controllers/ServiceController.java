package lt.codeacademy.petsitting.controllers;

import lt.codeacademy.petsitting.payload.response.ServiceResponse;
import lt.codeacademy.petsitting.pojo.Service;
import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.services.ServiceProviderService;
import lt.codeacademy.petsitting.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/services")
@PreAuthorize( "hasRole('SERVICE_PROVIDER')" )
public class ServiceController {

    private final ServiceService serviceService;

    private final ServiceProviderService serviceProviderService;

    @Autowired
    public ServiceController(ServiceService serviceService, ServiceProviderService serviceProviderService) {
        this.serviceService = serviceService;
        this.serviceProviderService = serviceProviderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProvidersServices(){
        ServiceProvider serviceProvider = serviceProviderService.getAuthenticatedServiceProvider();

        ServiceResponse response = new ServiceResponse( serviceProvider.getServices() );
        return ResponseEntity.ok( response );
    }

    @PostMapping
    public ResponseEntity<?> saveService( @Valid @RequestBody Service service ){
        if( service.getId() != null ){
            return ResponseEntity.badRequest().body( "Error: Service id cannot be prefilled" );
        }
        ServiceProvider serviceProvider = serviceProviderService.getAuthenticatedServiceProvider();

        Service savedService = serviceService.save( service );

        if( serviceProvider.getServices() == null ){
            serviceProvider.setServices( new ArrayList<>() );
        }
        serviceProvider.getServices().add( savedService );
        serviceProviderService.save( serviceProvider );

        return ResponseEntity.ok( "Success: Service saved" );
    }

    @PutMapping
    public ResponseEntity<?> updateService( @Valid @RequestBody Service service ){
        if( service.getId() == null ){
            return ResponseEntity.badRequest().body( "Error: Service id cannot be empty" );
        }
        ServiceProvider serviceProvider = serviceProviderService.getAuthenticatedServiceProvider();

        Service serviceToUpdate = serviceProvider.getServices()
                .stream()
                .filter( v -> v.getId().equals( service.getId() ) )
                .findFirst()
                .orElse( null );

        if(serviceToUpdate == null ){
            return ResponseEntity.badRequest().body( "Error: Service not found" );
        } else {
            serviceProvider.getServices().remove( serviceToUpdate );
            serviceProvider.getServices().add( serviceService.save( service ));
            serviceProviderService.save( serviceProvider );

            return ResponseEntity.ok( "Success: Service Updated Successfully" );
        }
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<?> deleteService( @PathVariable Long id ){
        ServiceProvider serviceProvider = serviceProviderService.getAuthenticatedServiceProvider();

        Service serviceToDelete = serviceProvider.getServices()
                .stream()
                .filter( v -> v.getId().equals( id ) )
                .findFirst()
                .orElse( null );

        if( serviceToDelete == null ){
            return ResponseEntity.badRequest().body( "Error: Service not found" );
        } else {
            serviceProvider.getServices().remove( serviceToDelete );
            serviceProviderService.save( serviceProvider );
            serviceService.deleteById( id );

            return ResponseEntity.ok( "Success: Service deleted" );
        }
    }

}
