package lt.codeacademy.petsitting.services;


import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.repositories.CustomerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CustomerServiceTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    @AfterEach
    void cleanUp(){
        customerRepository.deleteAll();
    }

    @Test
    void save_whenValidCustomerIsSupplied_customerIsSaved(){
        Customer customerToSave = getValidCustomer();

        customerService.save( customerToSave );

        Assertions.assertEquals( 1, customerRepository.count() );
    }

    @Test
    void save_whenValidCustomerIsSupplied_idIsGenerated(){
        Customer customerToSave = getValidCustomer();

        Customer savedCustomer = customerService.save( customerToSave );

        Assertions.assertNotNull( savedCustomer.getId() );
    }

    @Test
    void save_whenValidCustomerIsSupplied_sameCustomerIsReturned(){
        Customer customerToSave = getValidCustomer();

        Customer savedCustomer = customerService.save( customerToSave );

        Assertions.assertEquals( customerToSave.getId(), savedCustomer.getId() );
        Assertions.assertEquals( customerToSave.getUsername(), savedCustomer.getUsername() );
        Assertions.assertEquals( customerToSave.getPassword(), savedCustomer.getPassword() );
        Assertions.assertEquals( customerToSave.getFirstName(), savedCustomer.getFirstName() );
        Assertions.assertEquals( customerToSave.getLastName(), savedCustomer.getLastName() );
    }

    public Customer getValidCustomer(){
        return Customer.builder()
                .username( "John" )
                .password( "P4ssword" )
                .firstName( "Johny" )
                .lastName( "Surname" )
                .email( "test@email.com" )
                .build();
    }


}
