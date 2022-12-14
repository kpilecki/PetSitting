package lt.codeacademy.petsitting.services;


import lt.codeacademy.petsitting.pojo.ServiceProvider;
import lt.codeacademy.petsitting.repositories.ServiceProviderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ServiceProviderServiceTest {

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    ServiceProviderService serviceProviderService;

    @AfterEach
    void cleanUp(){
        serviceProviderRepository.deleteAll();
    }

    @Test
    void save_whenValidServiceProviderIsSupplied_serviceProviderIsSaved(){
        ServiceProvider serviceProviderToSave = getValidServiceProvider();

        serviceProviderService.save( serviceProviderToSave );

        Assertions.assertEquals( 1, serviceProviderRepository.count() );
    }

    @Test
    void save_whenValidServiceProviderIsSupplied_idIsGenerated(){
        ServiceProvider serviceProviderToSave = getValidServiceProvider();

        ServiceProvider savedServiceProvider = serviceProviderService.save( serviceProviderToSave );

        Assertions.assertNotNull( savedServiceProvider.getId() );
    }

    @Test
    void save_whenValidServiceProviderIsSupplied_sameServiceProviderIsReturned(){
        ServiceProvider serviceProviderToSave = getValidServiceProvider();

        ServiceProvider savedServiceProvider = serviceProviderService.save( serviceProviderToSave );

        Assertions.assertEquals( serviceProviderToSave.getId(), savedServiceProvider.getId() );
        Assertions.assertEquals( serviceProviderToSave.getUsername(), savedServiceProvider.getUsername() );
        Assertions.assertEquals( serviceProviderToSave.getPassword(), savedServiceProvider.getPassword() );
        Assertions.assertEquals( serviceProviderToSave.getFirstName(), savedServiceProvider.getFirstName() );
        Assertions.assertEquals( serviceProviderToSave.getLastName(), savedServiceProvider.getLastName() );
    }

    private ServiceProvider getValidServiceProvider(){
        return ServiceProvider.builder()
                .username( "John" )
                .password( "P4ssword" )
                .firstName( "Johny" )
                .lastName( "Surname" )
                .email( "test@email.com" )
                .build();
    }
}
