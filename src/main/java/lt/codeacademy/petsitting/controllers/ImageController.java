package lt.codeacademy.petsitting.controllers;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lt.codeacademy.petsitting.payload.response.ImageResponse;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.Pet;
import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.services.CustomerService;
import lt.codeacademy.petsitting.services.PetService;
import lt.codeacademy.petsitting.services.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/images")
@PreAuthorize( "hasRole('CUSTOMER')" )
public class ImageController {

    private static final long IMAGE_MAX_FILESIZE = 1048576;

    private final Storage storage;

    private final CustomerService customerService;

    private final PetService petService;

    private final ServiceProviderService serviceProviderService;

    @Autowired
    public ImageController(Storage storage, CustomerService customerService, PetService petService, ServiceProviderService serviceProviderService) {
        this.storage = storage;
        this.customerService = customerService;
        this.petService = petService;
        this.serviceProviderService = serviceProviderService;
    }

    @PostMapping("/customer" )
    public ResponseEntity<?> uploadImage(@RequestBody MultipartFile file ) throws IOException {

        if( file == null || file.isEmpty() ) {
            return ResponseEntity.badRequest().body( "Error: File is empty" );
        } else if( file.getSize() > IMAGE_MAX_FILESIZE ){
            return ResponseEntity.badRequest().body( "Error: File size limit exceeded, max image size is 2MB" );
        }

        Customer customer = customerService.getAuthenticatedCustomer();
        assert customer != null;
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;

        String fileName = customer.getId()
                + "_"
                + customer.getUsername()
                + "."
                + originalFileName.substring( originalFileName.lastIndexOf( "." ) + 1 );

        BlobId id = BlobId.of( "pet_sitting", fileName);
        BlobInfo info = BlobInfo.newBuilder( id ).build();
        byte[] fileAsByteArray = file.getBytes();
        storage.create( info, fileAsByteArray );

        if( customer.getProfileImageId() != null ){
            storage.delete( customer.getProfileImageId() );
        }
        customer.setProfileImageId( id );
        customerService.save( customer );

        return ResponseEntity.ok( "Success: Image saved" );
    }

    @GetMapping( "/customer")
    public ImageResponse getImage() throws IOException {
        Customer customer = customerService.getAuthenticatedCustomer();
        assert customer != null;

        if( customer.getProfileImageId() != null ){
            Blob image =  storage.get( customer.getProfileImageId() );

            if( image == null ){
               return getPlaceholderImage();
            }
            return  new ImageResponse( image.getContent() );
        }
       return getPlaceholderImage();
    }

    @GetMapping( "/pet/{id}")
    public ImageResponse getPetImage( @PathVariable Long id ) throws IOException {
        Optional<Pet> optionalPet = petService.findById( id );

        if( optionalPet.isPresent() && optionalPet.get().getProfileImageId() != null ) {
            Blob image = storage.get(optionalPet.get().getProfileImageId());
            return new ImageResponse(image.getContent());
        } else {
            File file = new ClassPathResource("/static/images/pet_placeholder.png").getFile();
            return new ImageResponse( Files.readAllBytes( file.toPath() ));
        }
    }

    @PostMapping("/pet/{id}" )
    public ResponseEntity<?> uploadPetImage(@PathVariable Long id, @RequestBody MultipartFile file ) throws IOException {

        if( file == null || file.isEmpty() ) {
            return ResponseEntity.badRequest().body( "Error: File is empty" );
        } else if( file.getSize() > IMAGE_MAX_FILESIZE ){
            return ResponseEntity.badRequest().body( "Error: File size limit exceeded, max image size is 2MB" );
        }
        Optional<Pet> optionalPet = petService.findById( id );

        if( optionalPet.isPresent() ){
            Pet pet = optionalPet.get();

            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;

            String fileName = pet.getId()
                    + "_"
                    + pet.getName()
                    + "."
                    + originalFileName.substring( originalFileName.lastIndexOf( "." ) + 1 );

            BlobId blobId = BlobId.of( "pet_sitting", fileName);
            BlobInfo info = BlobInfo.newBuilder( blobId ).build();
            byte[] fileAsByteArray = file.getBytes();
            storage.create( info, fileAsByteArray );

            if( pet.getProfileImageId() != null ){
                storage.delete( pet.getProfileImageId() );
            }
            pet.setProfileImageId( blobId );
            petService.save( pet );
            return ResponseEntity.ok( "Success: Image saved" );
        }
        return ResponseEntity.badRequest().body( "Error: Pet not found" );
    }

    @GetMapping( "/{id}")
    public ResponseEntity<?> getProviderImageById( @PathVariable Long id ) throws IOException {
        Optional<ServiceProvider> provider = serviceProviderService.findById( id );

        if( provider.isPresent() ){
            if( provider.get().getProfileImageId() == null ){
                return ResponseEntity.ok( getPlaceholderImage() );
            } else {
                Blob image = storage.get( provider.get().getProfileImageId() );
                return ResponseEntity.ok( new ImageResponse( image.getContent() ) );
            }
        }
        return ResponseEntity.badRequest().body( "Error: Customer not found" );
    }

    private ImageResponse getPlaceholderImage() throws IOException {
        File file = new ClassPathResource("/static/images/profile_image_placeholder.png").getFile();
        return new ImageResponse( Files.readAllBytes( file.toPath() ));
    }

}
