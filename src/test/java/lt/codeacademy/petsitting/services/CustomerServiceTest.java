package lt.codeacademy.petsitting.services;


import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    void setUp(){
        customerRepository.deleteAll();
    }

    @Test
    void save_whenValidCustomerIsSupplied_customerIsSaved(){
        Customer customerToSave = Customer.builder()
                .username( "John" )
                .password( "P4ssword" )
                .firstName( "Johny" )
                .lastName( "Surname" )
                .build();

        customerService.save( customerToSave );

        Assertions.assertEquals( 1, customerRepository.count() );
    }

    @Test
    void save_whenValidCustomerIsSupplied_idIsGenerated(){
        Customer customerToSave = Customer.builder()
                .username( "John" )
                .password( "P4ssword" )
                .firstName( "Johny" )
                .lastName( "Surname" )
                .build();

        Customer savedCustomer = customerService.save( customerToSave );

        Assertions.assertNotNull( savedCustomer.getId() );
    }

    @Test
    void save_whenValidCustomerIsSupplied_sameCustomerIsReturned(){
        Customer customerToSave = Customer.builder()
                .username( "John" )
                .password( "P4ssword" )
                .firstName( "Johny" )
                .lastName( "Surname" )
                .build();

        Customer savedCustomer = customerService.save( customerToSave );

        Assertions.assertEquals( customerToSave.getId(), savedCustomer.getId() );
        Assertions.assertEquals( customerToSave.getUsername(), savedCustomer.getUsername() );
        Assertions.assertEquals( customerToSave.getPassword(), savedCustomer.getPassword() );
        Assertions.assertEquals( customerToSave.getFirstName(), savedCustomer.getFirstName() );
        Assertions.assertEquals( customerToSave.getLastName(), savedCustomer.getLastName() );
    }


}